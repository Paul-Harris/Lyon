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
 * @author The Bomb Squad
 * Created : October 14, 2014
 * Purpose : UserManagment is the view of Lyon password management system.
 * Interactions : Being the controller is pulls data from the database and pushes changes and updates.
 * 
 */


public class UserManagement
{
	private Database db = new Database();
	private PasswordSecurity ps = new PasswordSecurity();
	private ExecuteShellComand esc = new ExecuteShellComand();

	private boolean success = false;
	private String outputMessage = "";
	private String customerAPI = "emtpy";

	private final String MESSAGE_PASSWORD_REQUIREMENTS = /**/
	"Passwords must be at least 8 characters and have at least one capital letter. \n"
			+ "They may use digits, letters, and any of the following symbols: ! & * ?";

	// Receives from other java classes

	// Receives commands from command line
	public UserManagement(String[] args) {
		this.command(Arrays.asList(args));
	}

	// Receives commands from java api
	public UserManagement(List<String> cmd) {
		command(cmd);

	}

	public void command(List<String> cmd) {
		String toDo = cmd.remove(0);
		switch (toDo) {
		case "delete":
			delete(cmd);
			// what to do when deleting
			break;
		case "signin":

			signIn(cmd);
			// Check verification

			break;
		case "signup":
			signUp(cmd);
			// Check verification
			break;
		case "resetpassword":
			resetPassword(cmd);
			break;
		case "changepassword":
			changePassword(cmd);
			break;
		}
	}

	private void changePassword(List<String> cmd) {
		// TODO Auto-generated method stub

	}

	public void delete(List<String> cmd) {
		// TODO Display message if error occurred
		db.deleteUser(cmd.get(0));
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

	public void resetPassword(List<String> cmd) {
		String userName = cmd.get(0);
		String securityAnswer = cmd.get(1);

		String password = cmd.get(2);

		if (!validatePassword(password)) {
			outputMessage = "Could not change password. "
					+ MESSAGE_PASSWORD_REQUIREMENTS;
			return;
		}

		String hashedNewPassword = ps.createHash(password);

		// checks to see if the security answer matches what is in the DB
		try {
			if (securityAnswer.equals(db.getSecurityAnswer(userName))) {

				db.changePassword(userName, hashedNewPassword);

				// Verifies that the password was succcesfully changed
				if (db.getPassword(userName).equals(hashedNewPassword)) {
					success = true;
				}
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
