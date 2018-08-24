package sql.query;

public enum SqlAction {
	loginUser, registerUser, checkUsername;
	

	public static SqlAction getActionByName(String name) {
		for (SqlAction sa : SqlAction.values())
			if (sa.name().equals(name))
				return sa;

		return null;
	}
}
