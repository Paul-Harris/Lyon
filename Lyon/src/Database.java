import java.util.ArrayList;

/**
 * @author The Bomb Squad Created : October 14, 2014 Purpose : Database is the
 *         model of the Lyon password management system that connects to the
 *         sqllite database to push and retrive values Interactions :
 *  Note: All passwords passed in should be hashes.
 * 
 */

public class Database {
	public Database() {

	}

	public boolean userExists(String userName) {
		return false;
	}

	/**
	 * 
	 * @param parameters
	 * @return true if user added successfully
	 */
	public boolean addUser(ArrayList<String> parameters) {
		return false;
	}

	/**
	 * 
	 * @param userName
	 * @return true if user removed successfully
	 */
	public boolean deleteUser(String userName) {
		return false;
	}

	public boolean securityAnswerCorrect(String userName, String securityAnswer) {
		return false;
	}

	public boolean passwordCorrect(String userName, String password) {
		return false;
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
	 * @param newName
	 * @return true if name change successful
	 */
	public boolean changeName(String userName, String newName) {
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
}
