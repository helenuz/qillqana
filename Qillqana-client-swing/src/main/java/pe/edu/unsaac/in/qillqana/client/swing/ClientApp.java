package pe.edu.unsaac.in.qillqana.client.swing;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import pe.edu.unsaac.in.qillqana.client.swing.ui.mediator.ConsoleSuscriptor;
import pe.edu.unsaac.in.qillqana.client.swing.ui.mediator.MessageMediador;
import pe.edu.unsaac.in.qillqana.client.swing.ui.mediator.SocketSuscriptor;
import pe.edu.unsaac.in.qillqana.client.swing.ui.windows.DlgBase;
import pe.edu.unsaac.in.qillqana.client.swing.ui.windows.TeacherLessonsWindow;
import pe.edu.unsaac.in.qillqana.client.swing.ui.windows.LoginWindow;
import pe.edu.unsaac.in.qillqana.client.swing.ui.windows.MainFrm;
import pe.edu.unsaac.in.qillqana.client.swing.ui.windows.StudentLessonWindow;
import pe.edu.unsaac.in.qillqana.common.mediator.Mediator;
import pe.edu.unsaac.in.qillqana.common.mediator.Session;

public class ClientApp {
	private static final Logger logger = Logger.getLogger(ClientApp.class
			.getName());

	public static void main(String[] args) throws IOException {
		PropertyConfigurator.configure("log4j.properties");
		// Here we are changing the Look&Feel of our application
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException
				| javax.swing.UnsupportedLookAndFeelException ex) {
			logger.error(ex);
		}
		logger.info("Reading configuration parameters");
		Properties conf = new Properties();
		FileInputStream fis = new FileInputStream(
				"conf/configuration.properties");
		conf.load(fis);
		String host = conf.getProperty("server.host");
		int port = Integer.parseInt(conf.getProperty("server.port"));
		logger.info("Configuration parameters readed");

		Mediator mediator = new MessageMediador();
		Session console = new ConsoleSuscriptor(mediator);
		mediator.addSession(console);
		//
		SocketSuscriptor socketClient = new SocketSuscriptor(host, port,
				mediator);
		mediator.addSession(socketClient);
		socketClient.start();

		MainFrm frm = new MainFrm(mediator);
		frm.setVisible(true);

		LoginWindow loginWindow = new LoginWindow(frm, true, mediator);
		mediator.addSession(loginWindow);
		loginWindow.setVisible(true);

		if (loginWindow.isLogin() == true) {
			frm.setSessionType(loginWindow.isTeacher());

			DlgBase dlgLessonList;
			if (loginWindow.isTeacher()) {
				dlgLessonList = new TeacherLessonsWindow(frm, true,mediator);
				dlgLessonList.setVisible(true);
			} else {
				dlgLessonList = new StudentLessonWindow(frm, true);
				dlgLessonList.setVisible(true);
			}
		} else {
			System.exit(0);
		}
	}
}
