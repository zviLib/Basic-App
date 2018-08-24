package server;

import java.net.ServerSocket;
import java.net.Socket;

import sql.SqlPool;

public class Server {

	public static void main(String[] args) throws Exception {
		SqlPool pool = new SqlPool(3);
		ServerSocket welcomeSocket = new ServerSocket(8125);
		System.out.println("listening for clients");
		  while (true) {
		   Socket connectionSocket = welcomeSocket.accept();
		   System.out.println("client connected");
		   ClientHandler ch = new ClientHandler(connectionSocket, pool);
		   new Thread(()->{
			try {
				ch.handleClient();
			} catch (Exception e) {
			}
		}).start();
		   
		  }
		  
	}

}
