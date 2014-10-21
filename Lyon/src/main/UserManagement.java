package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Database.NoSuchUserException;

/**
 * @author The Bomb Squad Created : October 14, 2014 Purpose : UserManagment is
 *         the view of Lyon password management system. Interactions : Being the
 *         controller is pulls data from the database and pushes changes and
 *         updates.
 * 
 */

public class UserManagement
{
	private Database db = new Database();
	private PasswordSecurity ps = new PasswordSecurity();
	private ExecuteShellComand esc = new ExecuteShellComand();

	private boolean success = false;
	private String outputMessage = "";

	private final String MESSAGE_PASSWORD_REQUIREMENTS = /**/
	"Passwords must be 8-10 characters in length.\n"
			+ "Passwords must have at least one digit and at least one capital letter.\n"
			+ "Passwords allow only digits, letters, and these symbols: ! & * ?";

	// Receives from other java classes
	private String customerAPI = "empty";

	// Receives commands from command line
	public UserManagement(String[] args) {
		this.command(Arrays.asList(args));
	}

	// Receives commands from Java API
	public UserManagement(List<String> cmd) {
		command(cmd);
	}

	public void command(List<String> cmd) {
		String toDo = cmd.remove(0);
		switch (toDo) {
		case "delete":
			// what to do when deleting
			delete(cmd);
			break;
		case "signin":
			// Check verification
			signIn(cmd);
			break;
		case "signup":
			// Check verification
			signUp(cmd);
			break;
		case "resetpassword":
			changePasswordUsingSecurityAnswer(cmd);
			break;
		case "changepassword":
			changePasswordUsingOldPassword(cmd);
			break;
		}
	}

	public void delete(List<String> cmd) {
		// TODO Display message if error occurred
		if (db.deleteUser(cmd.get(0))) {
			success = true;
		} else {
			outputMessage = "Could not delete user. Error was "
					+ db.getLastError();
		}
	}

	public void signIn(List<String> cmd) {

		String userName = cmd.get(0);
		String hashedPassword = cmd.get(1);
		if (ps.verifyPassword(userName, hashedPassword)) {
			success = true;
		} else {
			// TODO Display message if error occurred
		}

		// executes shell command to send back information to customers
		// application
		// esc.executeCommand(customerAPI);

	}

	public void signUp(List<String> cmd) {

		String userName = cmd.get(0);
		User user = new User(userName);

		// Validate that the password meets password requirements
		if (!validatePassword(cmd.get(1))) {

			outputMessage = "Could not register user. "
					+ MESSAGE_PASSWORD_REQUIREMENTS;
			return;
		}

		user.setPasswordHash(ps.createHash(cmd.get(1)));
		user.setFullName(cmd.get(2));
		user.setSecurityQuestion(cmd.get(3));
		user.setSecurityAnswer(cmd.get(4)); // TODO: Hash security answer?

		success = db.addUser(user);

		if (!success) {
			outputMessage = "Failed to add user to the database. Error was "
					+ db.getLastError();
		}
	}

	private boolean validatePassword(String password) {

		// TODO Auto-generated method stub
		return false;
	}

	public void changePasswordUsingSecurityAnswer(List<String> cmd) {
		String userName = cmd.get(0);
		String securityAnswer = cmd.get(1);

		String newPassword = cmd.get(2);

		// Make sure the security answer matches what is in the DB
		try {
			if (securityAnswer.equals(db.getSecurityAnswer(userName))) {

				changePassword(userName, newPassword);
			}
		} catch (NoSuchUserException e) {
			outputMessage = "User " + userName + " does not exist.";
		}
	}

	private void changePasswordUsingOldPassword(List<String> cmd) {
		String userName = cmd.get(0);
		String hashedOldPassword = ps.createHash(cmd.get(1));

		String newPassword = cmd.get(2);

		try {
			if (hashedOldPassword.equals(db.getPassword(userName))) {
				changePassword(userName, newPassword);
			}
		} catch (NoSuchUserException e) {
			outputMessage = "User " + userName + " does not exist.";
		}

	}

	private void changePassword(String userName, String newPassword) {

		// Validate that the new password meets password requirements
		if (!validatePassword(newPassword)) {
			outputMessage = "Could not change password. "
					+ MESSAGE_PASSWORD_REQUIREMENTS;
			return;
		}

		String hashedNewPassword = ps.createHash(newPassword);

		try {
			db.changePassword(userName, hashedNewPassword);

			// Verify the password was succcesfully changed
			if (db.getPassword(userName).equals(hashedNewPassword)) {
				success = true;
			}
		} catch (NoSuchUserException e) {
			outputMessage = "User " + userName + " does not exist.";
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

		public boolean verifyPassword(String userName, String password) {
			try {
				if (db.getPassword(userName).equals(createHash(password))) {
					return true;
				} else {
					return false;
				}
			} catch (NoSuchUserException e) {
				outputMessage = "User " + userName + " does not exist.";
				return false;
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
