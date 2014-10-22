package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import main.Database.DatabaseException;
import main.Database.NoSuchUserException;

/**
 * @author The Bomb Squad
 * @version October 21, 2014
 * @purpose UserManagment is the controller of Lyon password management system.
 */

public class UserManagement
{

	private Database db;
	private PasswordSecurity ps = new PasswordSecurity();
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
			this.db = new Database();

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
				changePasswordUsingOldPassword(args);
				break;
			case "resetpassword":
				changePasswordUsingSecurityAnswer(args);
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
		}

		return toDo + " completed successfully.";
	}

	public void delete(List<String> args) throws NoSuchUserException,
			DatabaseException {
		String adminUsername = args.get(0);
		String adminPasswordHashed = ps.createHash(args.get(1));

		// An exception will be thrown if this fails
		db.deleteUser(args.get(0));

		success = true;
	}

	public void signIn(List<String> args) throws NoSuchUserException,
			DatabaseException {

		String userName = args.get(0);
		String hashedPassword = args.get(1);
		if (ps.verifyPassword(userName, hashedPassword)) {
			success = true;
		} else {
			// TODO Display message if error occurred
		}

		// executes shell command to send back information to customers
		// application
		// esc.executeCommand(customerAPI);

	}

	public void signUp(List<String> args) throws DatabaseException,
			InvalidPasswordException {

		String userName = args.get(0);
		User user = new User(userName);

		if (!validatePassword(args.get(1))) {
			throw new InvalidPasswordException();
		}

		user.setPasswordHash(ps.createHash(args.get(1)));
		user.setFullName(args.get(2));
		user.setSecurityQuestion(args.get(3));
		user.setSecurityAnswer(args.get(4)); // TODO: Hash security answer?

		db.addUser(user);
	}

	private boolean validatePassword(String password) {

		// TODO Auto-generated method stub
		return false;
	}

	public void changePasswordUsingSecurityAnswer(List<String> args)
			throws NoSuchUserException, DatabaseException,
			InvalidPasswordException {
		String userName = args.get(0);
		String securityAnswer = args.get(1);
		String newPassword = args.get(2);

		// Make sure the security answer matches what is in the DB
		if (securityAnswer.equals(db.getSecurityAnswer(userName))) {
			changePassword(userName, newPassword);
		}
	}

	public void changePasswordUsingOldPassword(List<String> cmd)
			throws NoSuchUserException, DatabaseException,
			InvalidPasswordException {
		String userName = cmd.get(0);
		String hashedOldPassword = ps.createHash(cmd.get(1));

		String newPassword = cmd.get(2);

		if (hashedOldPassword.equals(db.getPassword(userName))) {
			changePassword(userName, newPassword);
		}

	}

	private void changePassword(String userName, String newPassword)
			throws NoSuchUserException, DatabaseException,
			InvalidPasswordException {

		if (!validatePassword(newPassword)) {
			throw new InvalidPasswordException();
		}

		String hashedNewPassword = ps.createHash(newPassword);

		db.changePassword(userName, hashedNewPassword);

		// Verify the password was succcesfully changed
		if (db.getPassword(userName).equals(hashedNewPassword)) {
			success = true;
		}
	}

	public boolean checkSuccess() {
		return success;
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
	 * Used to display an error when the password doesn't match the username.
	 */
	public class IncorrectPasswordException extends Exception
	{
		private static final long serialVersionUID = 8723806537728501163L;

	}

	private class PasswordSecurity
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

		public boolean verifyPassword(String userName, String password)
				throws NoSuchUserException, DatabaseException {
			return db.getPassword(userName).equals(createHash(password));
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
