package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Scanner;

public class SqlPool {

	private LinkedList<SqlQuery> queue;
	private boolean alive;
	private final static int DEFAULT_SIZE = 3;

	public SqlPool(int numOfThreads) {

		queue = new LinkedList<>();

		for (int i = 0; i < numOfThreads; i++) {
			new Thread(() -> {
				try {
					runCommands();
				} catch (Exception e) {
				}
			}).start();
		}

		alive = true;
	}

	public SqlPool() {
		this(DEFAULT_SIZE);
	}

	private void runCommands() {
		Scanner reader = new Scanner(ClassLoader.getSystemResourceAsStream("sql/ppass.txt"));
		String url = reader.nextLine();
		String username = reader.nextLine();
		String password = reader.nextLine();
		reader.close();

		try {
			Connection conn = DriverManager.getConnection(url, username, password);

			while (alive && !conn.isClosed()) {

				SqlQuery query;

				emptyCheck();

				synchronized (queue) {
					query = queue.poll();
					System.out.println("pulled query: " + query.getQuery());
				}

				if (query == null)
					continue;

				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

				if (query.isSelect()) {
					ResultSet rs = stmt.executeQuery(query.getQuery());
					query.setRes(rs);
				} else {
					stmt.executeUpdate(query.getQuery());
				}

				System.out.println("query executed");
				query.setExecuted();
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// stmt.close();

	}

	public synchronized void emptyCheck() {
		if (queue.isEmpty())
			try {
				System.out.println(Thread.currentThread().getName() + ":waiting");
				wait();
				System.out.println(Thread.currentThread().getName() + ":awaken");
			} catch (InterruptedException e) {
			}
	}

	public synchronized void addToQueue(SqlQuery sql) {

		synchronized (queue) {
			queue.add(sql);
			System.out.println("added query: " + sql.getQuery());
		}

		if (queue.size() == 1)
			notify();
	}
}
