package main;

/**
 * This class encapsulates information to be stored about a user.
 * 
 * @author The Bomb Squad (Samantha Beaston, Jacob Coleman, Jin Cho, Austin
 *         Harris, Tim Zorca)
 * @version October 23, 2014
 * 
 */
public class User
{
	private String userName, passwordHash, fullName, securityQuestion,
			securityAnswer;

	private Role role = Role.USER;

	public enum Role {
		USER, ADMIN
	}

	public User(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSecurityQuestion() {
		return securityQuestion;
	}

	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}

	public String getSecurityAnswer() {
		return securityAnswer;
	}

	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
