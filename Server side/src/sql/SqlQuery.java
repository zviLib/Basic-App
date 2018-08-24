package sql;

import java.sql.ResultSet;

public class SqlQuery {
	private String query;
	private ResultSet res;
	private boolean executed;
	private final boolean select;

	public SqlQuery(String query) {
		this.query = query;
		res = null;
		executed = false;
		select = query.split(" ")[0].equals("select");
	}

	public synchronized void setExecuted() {
		executed = true;
		notifyAll();
	}

	public boolean isSelect() {
		return select;
	}

	public String getQuery() {
		return query;
	}

	public ResultSet getRes() {
		return res;
	}

	public synchronized void waitForExecution() {
		if (executed == false)
			try {
				wait();
			} catch (InterruptedException e) {
			}
	}

	public void setRes(ResultSet res) {
		this.res = res;
	}

}
