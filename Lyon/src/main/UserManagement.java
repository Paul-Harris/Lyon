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
	private Database db;
	private PasswordSecurity ps = new PasswordSecurity();
	private ExecuteShellComand esc = new ExecuteShellComand();

	private boolean success = false;
	private String outputMessage = "";

	private final String MESSAGE_PASSWORD_REQUIREMENTS = /**/
	"Passwords must be 8-10 characters in length.\n"
			+ "They must have at least one digit and at least one capital letter.\n"
			+ "They allow only digits, letters, and these symbols: ! & * ?";

	// Receives from other java classes
	private String customerAPI = "empty";

	// Receives commands from command line
	public UserManagement(String[] args) {

		this.command(Arrays.asList(args));
	}

	// Receives commands from Java API
	public UserManagement(List<String> args) {
		command(args);
	}

	public void command(List<String> args) {
		try {
			this.db = new Database();

			String toDo = args.remove(0);

			switch (toDo) {
			case "signin":
				signIn(args);
				break;
			case "signup":
				signUp(args);
				break;
			case "resetpassword":
				changePasswordUsingSecurityAnswer(args);
				break;
			case "delete":
				delete(args);
				break;
			case "changepassword":
				changePasswordUsingOldPassword(args);
				break;
			default:
				outputMessage = "Invalid command. Type help for a list of possible commands.";
				break;
			}
		} catch (NoSuchUserException e) {
			outputMessage = "User " + e.getUserName() + " does not exist.";

		} catch (DatabaseException e) {
			outputMessage = "An error occured in the database: " + e.getMessage();
		}
	}

	// private void changeInfo(List<String> args) throws NoSuchUserException {
	// String affectedUserName = args.get(0);
	//
	// String loginUserName = args.get(1);
	// String loginPassword = args.get(2);
	//
	// if (!affectedUserName.equals(loginUserName)) {
	// if (db.getRole(loginUserName).equals(Role.USER.toString())) {
	//
	// }
	// }
	// // TODO Auto-generated method stub
	//
	// }

	public void delete(List<String> args) throws NoSuchUserException, DatabaseException {
		// TODO: Require admin credentials

		// TODO Display message if error occurred
		if (db.deleteUser(args.get(0))) {
			success = true;
		} else {
			outputMessage = "Could not delete user. Error was "
					+ db.getLastError();
		}
	}

	public void signIn(List<String> args) throws NoSuchUserException, DatabaseException {

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

	public void signUp(List<String> args) throws DatabaseException {

		String userName = args.get(0);
		User user = new User(userName);

		// Validate that the password meets password requirements
		if (!validatePassword(args.get(1))) {

			outputMessage = "Could not register user. "
					+ MESSAGE_PASSWORD_REQUIREMENTS;
			return;
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
			throws NoSuchUserException, DatabaseException {
		String userName = args.get(0);
		String securityAnswer = args.get(1);
		String newPassword = args.get(2);

		// Make sure the security answer matches what is in the DB
		if (securityAnswer.equals(db.getSecurityAnswer(userName))) {
			changePassword(userName, newPassword);
		}
	}

	public void changePasswordUsingOldPassword(List<String> cmd)
			throws NoSuchUserException, DatabaseException {
		String userName = cmd.get(0);
		String hashedOldPassword = ps.createHash(cmd.get(1));

		String newPassword = cmd.get(2);

		if (hashedOldPassword.equals(db.getPassword(userName))) {
			changePassword(userName, newPassword);
		}

	}

	private void changePassword(String userName, String newPassword)
			throws NoSuchUserException, DatabaseException {

		// Validate that the new password meets password requirements
		if (!validatePassword(newPassword)) {
			outputMessage = "Could not change password. "
					+ MESSAGE_PASSWORD_REQUIREMENTS;
			return;
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
