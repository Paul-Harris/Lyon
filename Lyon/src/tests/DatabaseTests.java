package tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import main.Database;
import main.User;
import main.User.Role;

import org.junit.Test;

public class DatabaseTests
{
	@Test
	public void testDatabaseConnection() {
		Database db = new Database();

		assertEquals(db.isConnected(), true);
	}

	@Test
	public void testAddUserAndGetUsers() {
		// Create an in-memory test database
		Database db = new Database(null);

		// Make a test user
		User testUser = new User("username1");
		testUser.setFullName("fullname1");
		testUser.setPasswordHash("passwordHash1");
		testUser.setRole(Role.USER);
		testUser.setSecurityAnswer("securityAnswer1");
		testUser.setSecurityQuestion("securityQuestion1");

		// Add the test user
		db.addUser(testUser);

		// Get the users in the database
		List<User> users = db.getUsers();

		// There should be only 1 user
		assertEquals(users.size(), 1);
		User user1 = users.get(0);

		// Make sure all the fields are equal
		assertEquals(testUser.getUserName(), user1.getUserName());
		assertEquals(testUser.getFullName(), user1.getFullName());
		assertEquals(testUser.getPasswordHash(), user1.getPasswordHash());
		assertEquals(testUser.getRole(), user1.getRole());
		assertEquals(testUser.getSecurityAnswer(), user1.getSecurityAnswer());
		assertEquals(testUser.getSecurityQuestion(),
				user1.getSecurityQuestion());
	}

}
