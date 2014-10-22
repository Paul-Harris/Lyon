package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	private ExecuteShellComand esc = new ExecuteShellComand();

	private boolean success = false;

	// Receives from other java classes
	private String customerAPI = "empty";

	// Receives commands from command line
	public UserManagement(String[] args) {
		System.out.println(command(Arrays.asList(args)));
	}

	// Receives commands from Java API
	public UserManagement(List<String> args) {
		// TODO: Send back command output message to caller
		// OR allow UserManagement class to be created without arguments
		command(args);
	}

	public String command(List<String> args) {
		String toDo = args.remove(0);

		toDo = toDo.toLowerCase();

		try {
			this.database = new Database();

			switch (toDo) {
			case "signin":
				signIn(args);
				break;
			case "signup":
			case "register":
				signUp(args);
				break;
			case "delete":
			case "deleteuser":
				delete(args);
				break;
			case "changepassword":
				changePasswordWithOldPassword(args);
				break;
			case "resetpassword":
				resetPasswordWithSecurityAnswer(args);
				break;
			case "changesecurityquestionandanswer":
				changeSecurityQuestionAndAnswer(args);
				break;
			case "changefullname":
				changeFullName(args);
				break;
			default:
				return "Invalid command. Type help for a list of possible commands.";
			}

		} catch (NoSuchUserException e) {
			return "User " + e.getUserName() + " does not exist.";

		} catch (DatabaseException e) {
			return "An error occured in the database: " + e.getMessage();
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

		return toDo + " completed successfully.";
	}

	private void delete(List<String> args) throws NoSuchUserException,
			DatabaseException, InsufficientRightsException,
			IncorrectPasswordException {
		String userName = args.get(0);
		String password = args.get(1);

		if (!security.verifyAdminRole(userName)) {
			return;
		}

		if (!security.verifyPassword(userName, password)) {
			return;
		}

		database.deleteUser(args.get(0));

		success = true;
	}

	private void signIn(List<String> args) throws NoSuchUserException,
			DatabaseException, IncorrectPasswordException {

		String userName = args.get(0);
		String password = args.get(1);

		if (!security.verifyPassword(userName, password)) {
			return;
		}

		success = true;

		// executes shell command to send back information to customers
		// application
		// esc.executeCommand(customerAPI);

	}

	private void signUp(List<String> args) throws DatabaseException,
			InvalidPasswordException {

		String userName = args.get(0);
		User user = new User(userName);

		if (!validatePassword(args.get(1))) {
			throw new InvalidPasswordException();
		}

		user.setPasswordHash(security.createHash(args.get(1)));
		user.setFullName(args.get(2));
		user.setSecurityQuestion(args.get(3));
		user.setSecurityAnswer(args.get(4));

		database.addUser(user);
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

	private void resetPasswordWithSecurityAnswer(List<String> args)
			throws NoSuchUserException, DatabaseException,
			InvalidPasswordException, IncorrectSecurityAnswerException {
		String userName = args.get(0);
		String securityAnswer = args.get(1);
		String newPassword = args.get(2);

		if (!security.verifySecurityAnswer(userName, securityAnswer)) {
			return;
		}

		changePassword(userName, newPassword);
	}

	private void changePasswordWithOldPassword(List<String> cmd)
			throws NoSuchUserException, DatabaseException,
			InvalidPasswordException, IncorrectPasswordException {
		String userName = cmd.get(0);
		String oldPassword = cmd.get(1);
		String newPassword = cmd.get(2);

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

	private void changeFullName(List<String> args) throws DatabaseException,
			NoSuchUserException, IncorrectPasswordException {
		String userName = args.get(0);
		String password = args.get(1);
		String newFullName = args.get(2);

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
			if (!database.getRole(userName).equals(Role.ADMIN)) {
				throw new InsufficientRightsException();
			} else {
				return true;
			}
		}

	}

	// Sends responses to users custom program via shell
	public class ExecuteShellComand
	{

		private String executeCommand(String command) {

			StringBuffer output = new StringBuffer();

			Process p;
			try {
				p = Runtime.getRuntime().exec(command);
				p.waitFor();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(p.getInputStream()));

				String line = "";
				while ((line = reader.readLine()) != null) {
					output.append(line + "\n");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return output.toString();

		}

	}

}
