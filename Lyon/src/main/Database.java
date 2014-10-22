package main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.User.Role;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

/**
 * @author The Bomb Squad
 * @version October 20, 2014
 * @purpose Database is the model of the Lyon password management system that
 *          connects to the SQLite database to push and retrive values.
 */

public class Database
{

	public Database() throws DatabaseException {
		initialize(DEFAULT_DB_FILE);
	}

	public Database(File databaseFile) throws DatabaseException {
		initialize(databaseFile);
	}

	public List<User> getUsers() throws DatabaseException {
		return getUsers(null);
	}

	public User getUser(String userName) throws NoSuchUserException,
			DatabaseException {
		List<User> users = getUsers(userName);

		if (users.isEmpty()) {
			throw new NoSuchUserException(userName);
		}

		return users.get(0);
	}

	public boolean userExists(String userName) throws DatabaseException {
		String userNameInDB = null;
		try {
			userNameInDB = getUserData(userName, FIELD_USERNAME);
		} catch (NoSuchUserException e) {
			return false;
		}

		return (userNameInDB != null);
	}

	/**
	 * Update this method if user fields change.
	 * 
	 * @return true if user added successfully
	 */
	public void addUser(User user) throws DatabaseException {
		String sql = "INSERT INTO " + TABLE_USER + " (" + CSV_USER_TABLE_FIELDS
				+ ")  values (?, ?, ?, ?, ?, ?);";

		SQLiteStatement statement = null;
		try {
			statement = dbConnection.prepare(sql);

			statement.bind(1, user.getUserName());
			statement.bind(2, user.getPasswordHash());
			statement.bind(3, user.getFullName());
			statement.bind(4, user.getSecurityQuestion());
			statement.bind(5, user.getSecurityAnswer());
			statement.bind(6, user.getRole().toString());

			exec(statement);
		} catch (SQLiteException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			disposeStatementIfNotNull(statement);
		}
	}

	/**
	 * 
	 * @return True if user removed successfully
	 */
	public boolean deleteUser(String userName) throws NoSuchUserException,
			DatabaseException {
		if (!userExists(userName)) {
			throw new NoSuchUserException(userName);
		}

		String sql = "DELETE FROM " + TABLE_USER + " WHERE " + FIELD_USERNAME
				+ " = ?;";
		SQLiteStatement statement = null;
		try {
			statement = dbConnection.prepare(sql);

			statement.bind(1, userName);

			exec(statement);
			return true;
		} catch (SQLiteException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			disposeStatementIfNotNull(statement);
		}
	}

	public String getSecurityQuestion(String userName)
			throws NoSuchUserException, DatabaseException {
		return getUserData(userName, FIELD_SECURITY_QUESTION);
	}

	public String getSecurityAnswer(String userName)
			throws NoSuchUserException, DatabaseException {
		return getUserData(userName, FIELD_SECURITY_ANSWER);
	}

	public String getPassword(String userName) throws NoSuchUserException,
			DatabaseException {
		return getUserData(userName, FIELD_PASSWORD);
	}

	public String getRole(String userName) throws NoSuchUserException,
			DatabaseException {
		return getUserData(userName, FIELD_ROLE);
	}

	public String getFullName(String userName) throws NoSuchUserException,
			DatabaseException {
		return getUserData(userName, FIELD_FULLNAME);
	}

	public void changePassword(String userName, String newPassword)
			throws DatabaseException, NoSuchUserException {
		changeUserData(userName, FIELD_PASSWORD, newPassword);
	}

	public void changeFullName(String userName, String newFullName)
			throws DatabaseException, NoSuchUserException {
		changeUserData(userName, FIELD_FULLNAME, newFullName);
	}

	public void changeSecurityQuestionAndAnswer(String userName,
			String newSecurityQuestion, String newSecurityAnswer)
			throws DatabaseException, NoSuchUserException {

		changeSecurityQuestion(userName, newSecurityQuestion);
		changeSecurityAnswer(userName, newSecurityAnswer);
	}

	private void changeSecurityQuestion(String userName,
			String newSecurityQuestion) throws DatabaseException,
			NoSuchUserException {
		changeUserData(userName, FIELD_SECURITY_QUESTION, newSecurityQuestion);
	}

	private void changeSecurityAnswer(String userName, String newSecurityAnswer)
			throws DatabaseException, NoSuchUserException {
		changeUserData(userName, FIELD_SECURITY_ANSWER, newSecurityAnswer);
	}

	public boolean isConnected() {
		return dbConnection.isOpen();
	}

	/**
	 * Used to close the database connection and free up resources used.
	 */
	public void dispose() {
		dbConnection.dispose();
	}

	public class DatabaseException extends Exception
	{
		private static final long serialVersionUID = 7020222729193699736L;
		private String message;

		public DatabaseException(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	public class NoSuchUserException extends Exception
	{
		private static final long serialVersionUID = 5654784800867083023L;
		private String userName;

		public NoSuchUserException(String username) {
			this.userName = username;
		}

		public String getUserName() {
			return userName;
		}
	}

	private void initialize(File dbFile) throws DatabaseException {
		connect(dbFile);
		createTableIfNotExisting(TABLE_USER, SCHEMA_USER_TABLE);
	}

	private void connect(File dbFile) throws DatabaseException {
		dbConnection = new SQLiteConnection(dbFile);
		try {
			dbConnection.open(true);
		} catch (SQLiteException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	private void createTableIfNotExisting(String tableName, String schema)
			throws DatabaseException {
		try {
			dbConnection.exec("CREATE TABLE IF NOT EXISTS " + tableName + " ("
					+ schema + ")");
		} catch (SQLiteException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/**
	 * Update this method if user fields change.
	 * 
	 * @param userName
	 *            UserName of the user to be retrieved. Use null to get all
	 *            users.
	 * @return List of Users
	 * @throws DatabaseException
	 */
	private List<User> getUsers(String userName) throws DatabaseException {
		List<User> users = new ArrayList<User>();

		String sql = "SELECT " + CSV_USER_TABLE_FIELDS + " FROM " + TABLE_USER;
		sql += (userName == null) ? ";" : " WHERE " + FIELD_USERNAME + " = ?;";

		try {
			SQLiteStatement statement = dbConnection.prepare(sql);

			if (userName != null) {
				statement.bind(1, userName);
			}

			while (statement.step()) {
				Map<String, String> fields = new HashMap<String, String>();

				int columnCount = statement.columnCount();
				for (int i = 0; i < columnCount; i++) {
					String columnName = statement.getColumnName(i);
					fields.put(columnName, statement.columnString(i));
				}

				User user = new User(fields.get(FIELD_USERNAME));
				user.setPasswordHash(fields.get(FIELD_PASSWORD));
				user.setRole(Role.valueOf(fields.get(FIELD_ROLE)));
				user.setFullName(fields.get(FIELD_FULLNAME));
				user.setSecurityQuestion(fields.get(FIELD_SECURITY_QUESTION));
				user.setSecurityAnswer(fields.get(FIELD_SECURITY_ANSWER));

				users.add(user);
			}

			statement.dispose();
		} catch (SQLiteException e) {
			throw new DatabaseException(e.getMessage());
		}

		return users;
	}

	private String getUserData(String userName, String fieldName)
			throws NoSuchUserException, DatabaseException {
		SQLiteStatement statement = null;

		String sql = "SELECT " + fieldName + " FROM " + TABLE_USER + " WHERE "
				+ FIELD_USERNAME + " = ?;";

		try {
			statement = dbConnection.prepare(sql);

			statement.bind(1, userName);

			while (statement.step()) {
				return statement.columnString(0);
			}
			throw new NoSuchUserException(userName);
		} catch (SQLiteException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			disposeStatementIfNotNull(statement);
		}
	}

	private boolean changeUserData(String userName, String fieldName,
			String newValue) throws DatabaseException, NoSuchUserException {
		SQLiteStatement statement = null;

		if (!userExists(userName)) {
			throw new NoSuchUserException(userName);
		}

		String sql = "UPDATE " + TABLE_USER + " SET " + fieldName
				+ " = ? WHERE " + FIELD_USERNAME + " = ?;";

		try {
			statement = dbConnection.prepare(sql);

			statement.bind(1, newValue);
			statement.bind(2, userName);

			while (statement.step()) {
			}
			return true;
		} catch (SQLiteException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			disposeStatementIfNotNull(statement);
		}
	}

	private void disposeStatementIfNotNull(SQLiteStatement statement) {
		if (statement != null) {
			statement.dispose();
		}
	}

	private static void exec(SQLiteStatement st) throws SQLiteException {
		while (st.step()) {
		}
	}

	private SQLiteConnection dbConnection;

	public static final String TABLE_USER = "User";
	public static final String FIELD_USERNAME = "Username";
	public static final String FIELD_PASSWORD = "Password";
	public static final String FIELD_FULLNAME = "FullName";
	public static final String FIELD_SECURITY_QUESTION = "SecurityQuestion";
	public static final String FIELD_SECURITY_ANSWER = "SecurityAnswer";
	public static final String FIELD_ROLE = "Role";

	private static final File DEFAULT_DB_FILE = new File("users.sqlite");

	private static final String FIELD_USER_ID = "UserID";
	private static final String SCHEMA_USER_TABLE =
	/*  */FIELD_USER_ID + " INTEGER PRIMARY KEY, " +
	/*  */FIELD_USERNAME + " TEXT UNIQUE, " +
	/*  */FIELD_PASSWORD + " TEXT, " +
	/*  */FIELD_FULLNAME + " TEXT, " +
	/*  */FIELD_SECURITY_QUESTION + " TEXT, " +
	/*  */FIELD_SECURITY_ANSWER + " TEXT, " +
	/*  */FIELD_ROLE + " text";

	/**
	 * Update this string if user fields change.
	 */
	private static final String CSV_USER_TABLE_FIELDS = FIELD_USERNAME + ", "
			+ FIELD_PASSWORD + ", " + FIELD_FULLNAME + ", "
			+ FIELD_SECURITY_QUESTION + ", " + FIELD_SECURITY_ANSWER + ", "
			+ FIELD_ROLE;

	public String getLastError() {
		// TODO Auto-generated method stub
		return null;
	}

}
