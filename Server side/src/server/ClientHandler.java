package server;

import java.io.IOException;
import java.net.Socket;

import dataTransfer.DataReader;
import dataTransfer.DataWriter;
import server.command.CommandEnum;
import server.command.SqlCommand;
import sql.SqlPool;

/**
 * 
 * @author Zvi Liebskind
 *
 * A class that handles the communication with the client
 */
public class ClientHandler {

	private SqlPool pool; // communication with the sql database
	private Socket client; // communication with the client
	private DataReader reader; // client input stream
	private DataWriter writer; // client output stream

	/**
	 * @param connectionSocket - socket to the connected client
	 * @param pool - connection to the sql database
	 */
	public ClientHandler(Socket connectionSocket, SqlPool pool) {
		this.pool = pool;
		try {
			reader = new DataReader(connectionSocket.getInputStream());
			writer = new DataWriter(connectionSocket.getOutputStream());
		} catch (IOException e) {
			client = null;
		}
	}

	/**
	 * read commands sent by the client
	 * @throws Exception
	 */
	public void handleClient() throws Exception {
		CommandEnum command = CommandEnum.values()[reader.readInt()];

		switch (command) {
		case sql:
			new SqlCommand(this, pool).execute();
			break;
		}

		reader.close();
		writer.close();
		client.close();
	}

	/**
	 * @return a string read from the client
	 */
	public String readString() {
		return reader.readString();
	}

	/**
	 * @return an integer read from the client
	 */
	public int readInt() {
		return reader.readInt();
	}

	/**
	 * @param i - an integer to write to the client
	 */ 
	public void write(int i) {
		writer.write(i);
	}

	/**
	 * @param s - a string to write to the client
	 */ 
	public void write(String s) {
		writer.write(s);
	}
}
