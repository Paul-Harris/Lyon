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
 * @version October 19, 2014
 * @purpose Database is the model of the Lyon password management system that
 *          connects to the SQLite database to push and retrive values.
 */

public class Database
{
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

	private static final String CSV_USER_TABLE_FIELDS = FIELD_USERNAME + ", "
			+ FIELD_PASSWORD + ", " + FIELD_FULLNAME + ", "
			+ FIELD_SECURITY_QUESTION + ", " + FIELD_SECURITY_ANSWER + ", "
			+ FIELD_ROLE;

	public Database() {
		initialize(DEFAULT_DB_FILE);
	}

	public Database(File databaseFile) {
		initialize(databaseFile);
	}

	/**
	 * Update this method if CSV_USER_TABLE_FIELDS changes.
	 * 
	 * @return A list of all users.
	 */
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();

		String sql = "SELECT " + CSV_USER_TABLE_FIELDS + " FROM " + TABLE_USER
				+ ";";

		try {
			SQLiteStatement statement = dbConnection.prepare(sql);

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
			showError(e, "Failed to get users. SQL used was\n" + sql);
		}

		return users;
	}

	public boolean userExists(String userName) {
		String sql = "SELECT " + FIELD_USERNAME + " FROM " + TABLE_USER
				+ " WHERE " + FIELD_USERNAME + " = ?;";
		SQLiteStatement statement = null;
		try {
			statement = dbConnection.prepare(sql);

			statement.bind(1, userName);

			while (statement.step()) {
				if (statement.columnString(0).equals(userName)) {
					return true;
				}
			}
			return false;
		} catch (SQLiteException e) {
			showError(e, "Error checking whether user " + userName + " exists.");
			return false;
		} finally {
			disposeIfNotNull(statement);
		}
	}

	/**
	 * Update this method if CSV_USER_TABLE_FIELDS changes.
	 * 
	 * @return true if user added successfully
	 */
	public boolean addUser(User user) {
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
			return true;
		} catch (SQLiteException e) {
			showError(e, "Could not add user " + user.getUserName()
					+ " to the database.");
			return false;
		} finally {
			disposeIfNotNull(statement);
		}
	}

	/**
	 * 
	 * @return true if user removed successfully
	 */
	public boolean deleteUser(String userName) {
		String sql = "DELETE FROM " + TABLE_USER + " WHERE " + FIELD_USERNAME
				+ " = ?;";
		SQLiteStatement statement = null;
		try {
			statement = dbConnection.prepare(sql);

			statement.bind(1, userName);

			exec(statement);
			return true;
		} catch (SQLiteException e) {
			showError(e, "Could not remove user " + userName);
			return false;
		} finally {
			disposeIfNotNull(statement);
		}
	}

	public boolean securityAnswerCorrect(String userName, String securityAnswer) {
		return false;
	}

	public boolean passwordCorrect(String userName, String password) {
		return false;
	}

	public String getPassword(String userName) {
		// testing only this hash is for "cats"
		String password = "1c0fb5008c573315e7b1e1af5ab41d0ce9b8d4469e41c4d59c3041bd99671208c415fcb0359418dd6bc481863d3d5d030a75364318afbec54cdba082df3f9577";
		return password;
	}

	/**
	 * 
	 * @param userName
	 * @param newPassword
	 * @return true if password change successful
	 */
	public boolean changePassword(String userName, String newPassword) {
		return false;
	}

	/**
	 * 
	 * @param userName
	 * @param newFullName
	 * @return true if name change successful
	 */
	public boolean changeFullName(String userName, String newFullName) {
		return false;
	}

	/**
	 * 
	 * @param userName
	 * @param newSecurityQuestion
	 * @param newSecurityAnswer
	 * @return true if security question and answer were successfully changed
	 */
	public boolean changeSecurityQuestionAndAnswer(String userName,
			String newSecurityQuestion, String newSecurityAnswer) {
		return false;
	}

	public boolean isConnected() {
		return dbConnection.isOpen();
	}

	public SQLiteConnection getConnection() {
		return dbConnection;
	}

	private void initialize(File dbFile) {
		connect(dbFile);
		createTableIfNotExisting(TABLE_USER, SCHEMA_USER_TABLE);
	}

	private void connect(File dbFile) {
		dbConnection = new SQLiteConnection(dbFile);
		try {
			dbConnection.open(true);
		} catch (SQLiteException e) {
			showError(e, "Could not connect to database.");
		}
	}

	private void createTableIfNotExisting(String tableName, String schema) {
		try {
			dbConnection.exec("CREATE TABLE IF NOT EXISTS " + tableName + " ("
					+ schema + ")");
		} catch (SQLiteException e) {
			showError(e, "Could not create table " + tableName);
		}
	}

	private void showError(Exception e, String message) {
		System.err.println(message);
		e.printStackTrace();
	}

	private void disposeIfNotNull(SQLiteStatement statement) {
		if (statement != null) {
			statement.dispose();
		}
	}

	private static void exec(SQLiteStatement st) throws SQLiteException {
		while (st.step()) {
		}
	}

}
