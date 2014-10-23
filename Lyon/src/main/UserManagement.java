package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Database.DatabaseException;
import main.Database.NoSuchUserException;
import main.User.Role;

/**
 * @author The Bomb Squad
 * @version October 21, 2014
 * @purpose UserManagment is the controller of Lyon password management system.
 */

public class UserManagement
{
	private Database database;
	private SecurityHandler security = new SecurityHandler();

	private boolean useInMemoryTestDB = false;

	private boolean success = false;

	/**
	 * Receives commands from command line
	 * 
	 * @param args
	 */
	public UserManagement(String[] args) {
		ArrayList<String> cmd = new ArrayList<String>(Arrays.asList(args));

		command(cmd);
	}

	/**
	 * Creates a new UserManagement object without invoking a command.
	 */
	public UserManagement() {
	}

	/**
	 * Receives commands from Java API
	 * 
	 * @param args
	 */
	public UserManagement(ArrayList<String> args) {
		// TODO: Send back command output message to caller
		// OR allow UserManagement class to be created without arguments
		command(args);
	}

	public String command(ArrayList<String> args) {
		String commandName = args.get(0).toLowerCase();

		ArrayList<String> params = new ArrayList<String>(args);
		// remove the command name from the parameters
		params.remove(0);

		// success should always be false at the start of a new command
		success = false;

		try {
			if (this.database == null) {
				this.database = useInMemoryTestDB ? new Database(null)
						: new Database();
			}

			switch (commandName) {
			case "signin":
				signIn(params);
				break;
			case "signup":
			case "register":
				signUp(params);
				break;
			case "delete":
			case "deleteuser":
				delete(params);
				break;
			case "changepassword":
				changePasswordWithOldPassword(params);
				break;
			case "resetpassword":
				resetPasswordWithSecurityAnswer(params);
				break;
			case "changesecurityquestionandanswer":
				changeSecurityQuestionAndAnswer(params);
				break;
			case "changefullname":
				changeFullName(params);
				break;
			default:
				return "Invalid command. Type help for a list of possible commands.";
			}

		} catch (DatabaseException e) {
			return "An error occured in the database: " + e.getMessage();

		} catch (NoSuchUserException e) {
			return "User " + e.getUserName() + " does not exist.";

		} catch (InvalidPasswordException e) {
			return "Invalid password. Passwords must be 8-10 characters in length.\n"
					+ "They must have at least one digit and at least one capital letter.\n"
					+ "They can only have digits, letters, and these symbols: ! & * ?";
		} catch (InsufficientRightsException e) {
			return "That operation requires you to be an administrator.";
		} catch (IncorrectPasswordException e) {
			return "Incorrect password.";
		} catch (IncorrectSecurityAnswerException e) {
			return "Incorrect security answer.";
		}

		return commandName + " completed successfully.";
	}

	/**
	 * Returns a list of all users in the database, given a username and
	 * password belonging to an administrator
	 */
	public List<User> getUsers(String loginUserName, String loginPassword)
			throws NoSuchUserException, DatabaseException,
			IncorrectPasswordException, InsufficientRightsException {

		if (!security.verifyAdminRole(loginUserName)) {
			return new ArrayList<User>();
		}
		if (!security.verifyPassword(loginUserName, loginPassword)) {
			return new ArrayList<User>();
		}

		return database.getUsers();
	}

	/**
	 * Returns a User object when given a correct username and password password
	 * belonging to an administrator
	 */
	public User getUser(String userName, String password)
			throws NoSuchUserException, DatabaseException,
			IncorrectPasswordException, InsufficientRightsException {

		if (!security.verifyPassword(userName, password)) {
			return null;
		}

		return database.getUser(userName);
	}

	/**
	 * Tells whether a given user is an admin
	 */
	public boolean isAdmin(String userName) throws NoSuchUserException,
			DatabaseException {
		return database.getRole(userName).equals(Role.ADMIN);
	}

	private void delete(List<String> params) throws NoSuchUserException,
			DatabaseException, InsufficientRightsException,
			IncorrectPasswordException {
		String userName = params.get(0);
		String password = params.get(1);

		if (!security.verifyAdminRole(userName)) {
			return;
		}

		if (!security.verifyPassword(userName, password)) {
			return;
		}

		database.deleteUser(params.get(0));

		success = true;
	}

	private void signIn(List<String> params) throws NoSuchUserException,
			DatabaseException, IncorrectPasswordException {

		String userName = params.get(0);
		String password = params.get(1);

		if (!security.verifyPassword(userName, password)) {
			return;
		}

		success = true;

		// TODO: Get this working this if there is time.
		// executes shell command to send back information to customers
		// application
		// esc.executeCommand(customerAPI);

	}

	private void signUp(List<String> params) throws DatabaseException,
			InvalidPasswordException {

		String userName = params.get(0);
		User user = new User(userName);

		if (!validatePassword(params.get(1))) {
			throw new InvalidPasswordException();
		}

		user.setPasswordHash(security.createHash(params.get(1)));
		user.setFullName(params.get(2));
		user.setSecurityQuestion(params.get(3));
		user.setSecurityAnswer(params.get(4));

		// By default, the first user added will be an admin.
		if (database.getUsers().size() == 0) {
			user.setRole(Role.ADMIN);
		}

		database.addUser(user);

		// if no exception was thrown by the database
		success = true;

		// TODO: Fix this if there is time.
		// esc.executeCommand(customerAPI);
	}

	public static boolean validatePassword(String password) {
		// Passwords must be 8-10 characters in length
		if (password == null || password.length() < 8 || password.length() > 10) {
			return false;
		}

		// They must have at least one digit and at least one capital letter
		// They can only have digits, letters, and these symbols: ! & * ?
		boolean hasCapitalLetter = false;
		boolean hasDigit = false;
		boolean allCharactersValid = true;
		for (Character c : password.toCharArray()) {
			if (Character.isUpperCase(c)) {
				hasCapitalLetter = true;
			} else if (Character.isDigit(c)) {
				hasDigit = true;
			} else if (Character.isLowerCase(c)) {
				// Lowercase characters are okay.
			} else if (c == '!' || c == '&' || c == '*' || c == '?') {
				// These symbols are okay.
			} else {
				// Invalid character found.
				allCharactersValid = false;
			}
		}

		if (!allCharactersValid) {
			return false;
		}

		if (!hasCapitalLetter || !hasDigit) {
			return false;
		}

		return true;
	}

	private void resetPasswordWithSecurityAnswer(List<String> params)
			throws NoSuchUserException, DatabaseException,
			InvalidPasswordException, IncorrectSecurityAnswerException {
		String userName = params.get(0);
		String securityAnswer = params.get(1);
		String newPassword = params.get(2);

		if (!security.verifySecurityAnswer(userName, securityAnswer)) {
			return;
		}

		changePassword(userName, newPassword);
	}

	private void changePasswordWithOldPassword(List<String> params)
			throws NoSuchUserException, DatabaseException,
			InvalidPasswordException, IncorrectPasswordException {
		String userName = params.get(0);
		String oldPassword = params.get(1);
		String newPassword = params.get(2);

		if (!security.verifyPassword(userName, oldPassword)) {
			return;
		}

		changePassword(userName, newPassword);
	}

	private void changePassword(String userName, String newPassword)
			throws NoSuchUserException, DatabaseException,
			InvalidPasswordException {

		// Make sure the password is valid
		if (!validatePassword(newPassword)) {
			throw new InvalidPasswordException();
		}

		String hashedNewPassword = security.createHash(newPassword);

		database.changePassword(userName, hashedNewPassword);

		// Verify the password was successfully changed
		if (database.getPassword(userName).equals(hashedNewPassword)) {
			success = true;
		}
	}

	private void changeSecurityQuestionAndAnswer(List<String> args)
			throws DatabaseException, NoSuchUserException,
			IncorrectPasswordException {
		String userName = args.get(0);
		String password = args.get(1);
		String newSecurityQuestion = args.get(2);
		String newSecurityAnswer = args.get(3);

		if (!security.verifyPassword(userName, password)) {
			return;
		}

		database.changeSecurityQuestionAndAnswer(userName, newSecurityQuestion,
				newSecurityAnswer);
	}

	private void changeFullName(List<String> params) throws DatabaseException,
			NoSuchUserException, IncorrectPasswordException {
		String userName = params.get(0);
		String password = params.get(1);
		String newFullName = params.get(2);

		if (!security.verifyPassword(userName, password)) {
			return;
		}

		database.changeFullName(userName, newFullName);
	}

	public boolean checkSuccess() {
		return success;
	}

	/**
	 * Used to display an error when a user tries to perform an
	 * administrator-only operation
	 */
	public class InsufficientRightsException extends Exception
	{
		private static final long serialVersionUID = 2171510130999870636L;
	}

	/**
	 * Used to display an error when a password doesn't meet the requirements
	 * for a password.
	 */
	public class InvalidPasswordException extends Exception
	{
		private static final long serialVersionUID = 8723806537728501163L;
	}

	/**
	 * Used to display an error when the wrong password is specified
	 */
	public class IncorrectPasswordException extends Exception
	{
		private static final long serialVersionUID = 6920171054845730558L;
	}

	/**
	 * Used to display an error when the wrong security answer is specified
	 */
	public class IncorrectSecurityAnswerException extends Exception
	{
		private static final long serialVersionUID = 825562424873839368L;
	}

	private class SecurityHandler
	{
		public String createHash(String password) {

			MessageDigest md;
			String hash = "empty";
			try {
				md = MessageDigest.getInstance("SHA-512");

				byte[] bytes = md.digest(password.getBytes());
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < bytes.length; i++) {
					sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
							.substring(1));
				}

				// returns the hash of the password
				hash = sb.toString();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return hash;

		}

		public boolean verifySecurityAnswer(String userName,
				String securityAnswer) throws IncorrectSecurityAnswerException,
				NoSuchUserException, DatabaseException {
			if (!securityAnswer.equals(database.getSecurityAnswer(userName))) {
				throw new IncorrectSecurityAnswerException();
			} else {
				return true;
			}
		}

		public boolean verifyPassword(String userName, String unhashedPassword)
				throws NoSuchUserException, DatabaseException,
				IncorrectPasswordException {
			if (!database.getPassword(userName).equals(
					createHash(unhashedPassword))) {
				throw new IncorrectPasswordException();
			} else {
				return true;
			}
		}

		public boolean verifyAdminRole(String userName)
				throws InsufficientRightsException, NoSuchUserException,
				DatabaseException {
			if (!database.getRole(userName).equals(Role.ADMIN.toString())) {
				throw new InsufficientRightsException();
			} else {
				return true;
			}
		}

	}

	public void useInMemoryTestDB(boolean value) {
		useInMemoryTestDB = value;
	}

}
