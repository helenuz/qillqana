package pe.edu.unsaac.in.qillqana.client.swing.ui.mediator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

import pe.edu.unsaac.in.qillqana.client.swing.locale.Messages;
import pe.edu.unsaac.in.qillqana.common.command.Command;
import pe.edu.unsaac.in.qillqana.common.gson.GsonUtils;
import pe.edu.unsaac.in.qillqana.common.mediator.Mediator;
import pe.edu.unsaac.in.qillqana.common.mediator.Session;

public class SocketSuscriptor extends Thread implements Session {
	public static final Logger logger = Logger.getLogger(SocketSuscriptor.class
			.getName());

	private Mediator mediator;
	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;

	public SocketSuscriptor(String host, int port, Mediator mediator) {
		initStreams(host, port);
		this.mediator = mediator;

	}
	/**
	 * Here we are receiving a command from the mediator to send it to the server
	 * The supported command are:
	 * ShapeCommand
	 * LoginCommand
	 * ............
	 */
	@Override
	public void receiveCommand(Command command) {
		String json_string=GsonUtils.toJson(command);
		logger.info(Messages.getString("command.send")+json_string);
		out.println(json_string);
	}
	
	
	@Override
	public void sendCommand(Command command) {
		logger.info(Messages.getString("command.send.mediator")+GsonUtils.toJson(command));
		mediator.sendMessage(command, this);
	}

	@Override
	public boolean processCommad(Command command) {
		sendCommand(command);
		return false;
	}

	@Override
	public String getIdSession() {
		return null;
	}

	@Override
	public void run() {
		try {
			String line = "";
			Command command=null;
			while ((line = in.readLine()) != null) {
				command=GsonUtils.toCommand(line);
				processCommad(command);
			}
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	private void initStreams(String host, int port) {
		try {
			socket = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException ex) {
			logger.error(ex);
			System.exit(0);
		}
	}
}
