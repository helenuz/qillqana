package pe.edu.unsaac.in.qillqana.client.swing.ui.windows;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import pe.edu.unsaac.in.qillqana.client.swing.locale.Messages;
import pe.edu.unsaac.in.qillqana.common.command.Command;
import pe.edu.unsaac.in.qillqana.common.command.LoginCommand;
import pe.edu.unsaac.in.qillqana.common.gson.GsonUtils;
import pe.edu.unsaac.in.qillqana.common.mediator.Mediator;
import pe.edu.unsaac.in.qillqana.common.mediator.Session;

public class LoginWindow extends JDialog implements Session {
	/**
	 * s
	 */
	private static final long serialVersionUID = 1L;
	public static final Logger logger = Logger.getLogger(LoginWindow.class);
	private JPanel pnlLogo;
	private JPanel pnlContent;
	private JPanel pnlButtons;
	private JLabel lblImage;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnLogin;
	private JButton btnExit;
	private JButton btnConfig;

	private boolean login = false;
	private boolean teacher = false;
	private String user_id="";
	private Mediator mediator;

	public LoginWindow(Frame owner, boolean modal, Mediator mediator) {
		super(owner, modal);
		this.mediator = mediator;
		initGUI();
	}

	private void initGUI() {
		setTitle(Messages.getString("login.title")); //$NON-NLS-1$
		getContentPane().setLayout(new BorderLayout(0, 0));

		pnlLogo = new JPanel();
		getContentPane().add(pnlLogo, BorderLayout.NORTH);

		lblImage = new JLabel(""); //$NON-NLS-1$
		lblImage.setIcon(new ImageIcon("images/whiteboard.png")); //$NON-NLS-1$
		pnlLogo.add(lblImage);

		pnlContent = new JPanel();
		getContentPane().add(pnlContent, BorderLayout.CENTER);
		pnlContent.setLayout(new MigLayout("", "[][grow]", "[][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		lblUsername = new JLabel(Messages.getString("label.username")); //$NON-NLS-1$
		pnlContent.add(lblUsername, "cell 0 0,alignx left"); //$NON-NLS-1$

		txtUsername = new JTextField();
		pnlContent.add(txtUsername, "cell 1 0,growx"); //$NON-NLS-1$
		txtUsername.setColumns(10);

		lblPassword = new JLabel(Messages.getString("label.password")); //$NON-NLS-1$
		pnlContent.add(lblPassword, "cell 0 1,alignx left"); //$NON-NLS-1$

		txtPassword = new JPasswordField();
		pnlContent.add(txtPassword, "cell 1 1,growx"); //$NON-NLS-1$

		btnConfig = new JButton(""); //$NON-NLS-1$
		btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnConfig_actionPerformed(arg0);
			}
		});
		btnConfig.setIcon(new ImageIcon("images/_16_preferences.png")); //$NON-NLS-1$
		pnlContent.add(btnConfig, "cell 1 2,alignx right"); //$NON-NLS-1$

		pnlButtons = new JPanel();
		getContentPane().add(pnlButtons, BorderLayout.SOUTH);

		btnLogin = new JButton(Messages.getString("label.login")); //$NON-NLS-1$
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btnLogin_actionPerformed(e);
			}
		});
		pnlButtons.add(btnLogin);

		btnExit = new JButton(Messages.getString("label.exit")); //$NON-NLS-1$
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnExit_actionPerformed(arg0);
			}
		});
		pnlButtons.add(btnExit);
		pack();
		setLocationRelativeTo(getParent());
	}

	protected void do_btnConfig_actionPerformed(ActionEvent arg0) {
		ConfigWindow configWindow = new ConfigWindow(new JFrame(), true);
		configWindow.setVisible(true);
	}

	protected void do_btnExit_actionPerformed(ActionEvent arg0) {
		int option = JOptionPane.showConfirmDialog(getParent(),
				Messages.getString("label.question.exit"),
				Messages.getString("label.exit"), JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	protected void do_btnLogin_actionPerformed(ActionEvent e) {

		String username = txtUsername.getText().trim();
		String password = arrayToString(txtPassword.getPassword());

		if (!username.isEmpty() && !password.isEmpty()) {
			// TODO Encode or crypt the password here

			LoginCommand lcmd = new LoginCommand();

			lcmd.setUser(username);
			lcmd.setPassword(password);

			sendCommand(lcmd);

			txtUsername.setEnabled(false);
			txtPassword.setEnabled(false);
			btnLogin.setEnabled(false);

		} else {
			JOptionPane.showMessageDialog(getParent(),
					Messages.getString("label.blank"),
					Messages.getString("label.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean isLogin() {
		return login;
	}

	public boolean isTeacher() {
		return teacher;
	}

	private String arrayToString(char[] array) {
		String str = "";
		for (int i = 0; i < array.length; i++) {
			str += array[i];
		}
		return str;
	}
	
	public String getUserId(){
		return user_id;
	}

	// The Session Interface Methods
	@Override
	public void receiveCommand(Command command) {
		logger.info("command received" + GsonUtils.toJson(command));
		if ((login = processCommad(command))) {
			mediator.removeSession(this);
			this.dispose();
		} else {
			txtUsername.setText("");
			txtPassword.setText("");
			txtUsername.setEnabled(true);
			txtPassword.setEnabled(true);
			btnLogin.setEnabled(true);
			JOptionPane.showMessageDialog(getParent(),
					Messages.getString("login.failed"),
					Messages.getString("label.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void sendCommand(Command command) {
		mediator.sendMessage(command, this);
	}

	@Override
	public boolean processCommad(Command command) {
		boolean result = false;
		if (command.getName().equals("login_result")) {
			if (command.getParameter("status").equals("ok")) {
				if (command.getParameter("type").equals("teacher")) {
					teacher = true;
				} else {
					teacher = false;
				}
				user_id=command.getParameter("user_id").toString();
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public String getIdSession() {
		return null;
	}

	@Override
	public void run() {
	}

	@Override
	public void start() {
	}
}
