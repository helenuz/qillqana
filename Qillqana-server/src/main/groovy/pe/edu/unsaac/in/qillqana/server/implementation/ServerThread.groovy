package pe.edu.unsaac.in.qillqana.server.implementation

import java.net.Socket
import org.apache.log4j.*

class ServerThread extends Thread{
	private final static Logger logger=Logger.getInstance(ServerThread.class)
	Socket socket

	ServerThread(Socket socket){
		this.socket=socket
	}
	@Override
	public void run(){
		logger.info "Thread started"
		socket.withStreams {  input, output ->
			output << "Hello world\n"
		}
	}
}