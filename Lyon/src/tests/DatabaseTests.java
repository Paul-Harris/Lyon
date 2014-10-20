package tests;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

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

		User testUser = createTestUserWithRandomInfo();

		db.addUser(testUser);

		List<User> users = db.getUsers();

		// There should be only 1 user
		assertEquals(users.size(), 1);
		User user1 = users.get(0);

		// Make sure all the fields are equal
		assertUserFieldsEqual(testUser, user1);
	}

	@Test
	public void testAddAndDeleteOneUser() {
		// Create an in-memory test database
		Database db = new Database(null);

		User testUser = createTestUserWithRandomInfo();

		db.addUser(testUser);

		// There should be one user
		assertEquals(db.getUsers().size(), 1);

		db.deleteUser(testUser.getUserName());

		// There should be zero users
		assertEquals(db.getUsers().size(), 0);
	}
	
	@Test
	public void testAddThreeUsersAndDeleteTwo() {
		// Create an in-memory test database
		Database db = new Database(null);

		// Create three random test users
		User testUserA = createTestUserWithRandomInfo();
		User testUserB = createTestUserWithRandomInfo();
		User testUserC = createTestUserWithRandomInfo();

		// Add the three users
		db.addUser(testUserB);
		db.addUser(testUserC);
		db.addUser(testUserA);

		// There should be three users
		assertEquals(db.getUsers().size(), 3);

		// Delete two of them
		db.deleteUser(testUserC.getUserName());
		db.deleteUser(testUserA.getUserName());

		// There should be one user
		List<User> users = db.getUsers();
		assertEquals(users.size(), 1);
		
		// The one user left should have testUserB's information
		assertUserFieldsEqual(users.get(0), testUserB);
	}

	private void assertUserFieldsEqual(User userA, User userB) {
		assertEquals(userA.getUserName(), userB.getUserName());
		assertEquals(userA.getFullName(), userB.getFullName());
		assertEquals(userA.getPasswordHash(), userB.getPasswordHash());
		assertEquals(userA.getRole(), userB.getRole());
		assertEquals(userA.getSecurityAnswer(), userB.getSecurityAnswer());
		assertEquals(userA.getSecurityQuestion(), userB.getSecurityQuestion());
	}

	private User createTestUserWithRandomInfo() {
		User testUser = new User(randomString());
		testUser.setFullName(randomString());
		testUser.setPasswordHash(randomString());
		testUser.setRole(Role.USER);
		testUser.setSecurityAnswer(randomString());
		testUser.setSecurityQuestion(randomString());

		return testUser;
	}
	
	private String randomString() {
		return UUID.randomUUID().toString();
	}

}
