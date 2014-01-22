package pe.edu.unsaac.in.qillqana.client.swing;

import org.apache.log4j.Logger;

import pe.edu.unsaac.in.qillqana.client.swing.ui.mediator.ConsoleSuscriptor;
import pe.edu.unsaac.in.qillqana.client.swing.ui.mediator.MessageMediador;
import pe.edu.unsaac.in.qillqana.client.swing.ui.mediator.SocketSuscriptor;
import pe.edu.unsaac.in.qillqana.common.command.Command;
import pe.edu.unsaac.in.qillqana.common.mediator.Mediator;

public class ClientAppTest {

	public static final Logger logger=Logger.getLogger(ClientAppTest.class);
	
	public ClientAppTest() {
	}

	public void setUp() throws Exception {

	}

	public void tearDown() throws Exception {
	}


	public void test() {
		SocketSuscriptor client01;
		SocketSuscriptor client02;
		
		ConsoleSuscriptor console01;
		ConsoleSuscriptor console02;

		String host = "localhost";
		int port = 1234;

		Mediator mediator01;
		Mediator mediator02;
		
		mediator01 = new MessageMediador();
		mediator02 = new MessageMediador();

		client01 = new SocketSuscriptor(host, port, mediator01);
		client02 = new SocketSuscriptor(host, port, mediator02);
		
		console01=new ConsoleSuscriptor(mediator01);
		console02=new ConsoleSuscriptor(mediator02);

		mediator01.addSession(client01);
		mediator02.addSession(client02);
		
		mediator01.addSession(console01);
		mediator02.addSession(console02);
		

		client01.start();
		client02.start();
		
		Command command=new Command();
		command.setName("message");
		command.addParameter("value", "Hola");
		console01.sendCommand(command);
		command.addParameter("value", "Adios");
		console02.sendCommand(command);
		
		command.setName("exit");
		command.setName("exit");
		
		console01.sendCommand(command);
		console02.sendCommand(command);
		logger.info("Hola");
	}

}