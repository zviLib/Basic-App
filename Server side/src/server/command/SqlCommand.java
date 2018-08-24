package server.command;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import server.ClientHandler;
import sql.SqlPool;
import sql.SqlQuery;
import sql.query.SqlMap;
import sql.query.SqlAction;

public class SqlCommand implements Command {

	private SqlPool pool;
	private ClientHandler client;
	
	public SqlCommand(ClientHandler client,SqlPool pool) {
		this.pool = pool;
		this.client = client;
	}
	
	@Override
	public void execute() {
		SqlAction type = SqlAction.getActionByName(client.readString());
		String query;
		switch (type) {
		case loginUser:
		case registerUser:
			query = String.format(SqlMap.getActionQuery(type), client.readString(), client.readInt());
			break;
		case checkUsername:
			query = String.format(SqlMap.getActionQuery(type), client.readString());
			break;
		default:
			System.out.println("Cannot find action:" + type.name());
			return;
		}
		// add task to sql pool
		SqlQuery sq = new SqlQuery(query);
		pool.addToQueue(sq);
		// wait for task
		sq.waitForExecution();
		// confirm action finished
		client.write(1);
		// if type is select - return result
		if (sq.isSelect())
			sendResultSet(sq.getRes());
	}
	
	private void sendResultSet(ResultSet rs) {
		try {
			int rowcount = 0;

			if (rs.last()) {
				rowcount = rs.getRow();
				// Move to beginning
				rs.beforeFirst();
			}

			// write number of rows
			client.write(rowcount);

			// write number of columns
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsCount = rsmd.getColumnCount();
			client.write(columnsCount);
			// write col headers
			for (int i = 1; i <= columnsCount; i++) {
				client.write(rsmd.getColumnName(i));
			}
			// write results
			while (rs.next()) {
				for (int i = 1; i <= columnsCount; i++) {
					client.write(rs.getString(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
