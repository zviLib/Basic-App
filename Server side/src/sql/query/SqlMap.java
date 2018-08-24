package sql.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SqlMap {
	private static Map<SqlAction, String> map;

	private SqlMap() {
	}

	public static String getActionQuery(SqlAction action) {
		if (map == null)
			initialize();
		
		return map.get(action);
	}

	private static void initialize() {
		map = new HashMap<>();
		try {
			Scanner reader = new Scanner(ClassLoader.getSystemResourceAsStream("sql/actions.txt"));
			String line;
			String[] split;
			while (reader.hasNext()) {
				line = reader.nextLine();
				split = line.split(";");
				SqlAction sa = SqlAction.getActionByName(split[0]);
				if (sa == null)
					continue;
				map.put(sa, split[1]);
			}

			reader.close();

		} catch (Exception e) {
			System.out.println("Cannot find file: sql/actions.txt");
			return;
		}
	}
}
