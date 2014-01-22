package pe.edu.unsaac.in.qillqana.server.implementation

import java.net.ServerSocket
import java.net.Socket
import org.apache.log4j.*

class Server {
	private static final Logger logger=Logger.getInstance(Server.class)
	int port

	Server(int port){
		this.port=port
	}

	void initServer(){
		ServerSocket server=new ServerSocket(port)
		logger.info "Server started"

		logger.info "Waiting for connections"
		while(true) {
			Socket s=server.accept()
			ServerThread st=new ServerThread(s)
			st.start()
		}
	}
}