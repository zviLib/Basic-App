package server;

import java.io.IOException;
import java.net.Socket;

import dataTransfer.DataReader;
import dataTransfer.DataWriter;
import server.command.CommandEnum;
import server.command.SqlCommand;
import sql.SqlPool;

public class ClientHandler {

	private SqlPool pool;
	private Socket client;
	private DataReader reader;
	private DataWriter writer;

	public ClientHandler(Socket connectionSocket, SqlPool pool) {
		this.pool = pool;
		try {
			reader = new DataReader(connectionSocket.getInputStream());
			writer = new DataWriter(connectionSocket.getOutputStream());
		} catch (IOException e) {
			client = null;
		}
	}

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

	public String readString() {
		return reader.readString();
	}

	public int readInt() {
		return reader.readInt();
	}

	public void write(int i) {
		writer.write(i);
	}

	public void write(String s) {
		writer.write(s);
	}
}
