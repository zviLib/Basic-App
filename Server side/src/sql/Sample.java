package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Sample {

	public static void main(String[] args) {

		Connection conn = null;

		String url = "jdbc:mysql://localhost:3336/BasketGM?serverTimezone=UTC&useSSL=false";
		String username = "root";
		String password = "opp7GNvY0QZZr3mw";
		try {
			conn = DriverManager.getConnection(url, username, password);

			Statement stmt;
			String query;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 8; j++) {
					stmt = conn.createStatement();
					query = String.format("insert into league_teams (league_id,team_id) values (%d,%d)", i,0);
					stmt.executeUpdate(query);
					stmt.close();
				}
			}

		} catch (Exception ex) {
			System.out.println("SQLException: " + ex.getMessage());
			ex.printStackTrace();
		}

	}

}
