package tests;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import main.Database;
import main.Database.NoSuchUserException;
import main.User;
import main.User.Role;

import org.junit.Test;

public class DatabaseTests
{
	@Test
	public void testDatabaseConnection() {
		Database db = new Database();

		assertTrue(db.isConnected());
	}

	@Test
	public void testAddUserAndGetUsers() {
		// Create an in-memory test database
		Database db = new Database(null);

		User testUser = createTestUserWithRandomInfo();

		db.addUser(testUser);

		List<User> users = db.getUsers();

		// There should be only one user
		assertEquals(users.size(), 1);
		User user1 = users.get(0);

		// All the fields should be equal
		assertUserFieldsEqual(testUser, user1);
	}

	@Test
	public void testAddUserAndGetUser() {
		// Create an in-memory test database
		Database db = new Database(null);

		User testUser = createTestUserWithRandomInfo();

		db.addUser(testUser);

		User userInDB;
		try {
			userInDB = db.getUser(testUser.getUserName());
		} catch (NoSuchUserException e) {
			fail();
			return;
		}

		// userInDB should not be null
		assertFalse(userInDB == null);

		// All the fields should be equal
		assertUserFieldsEqual(testUser, userInDB);
	}

	@Test
	public void testAddAndDeleteOneUser() {
		// Create an in-memory test database
		Database db = new Database(null);

		User testUser = createTestUserWithRandomInfo();

		db.addUser(testUser);

		// There should be one user
		assertEquals(db.getUsers().size(), 1);

		try {
			db.deleteUser(testUser.getUserName());
		} catch (NoSuchUserException e) {
			e.printStackTrace();
			fail();
		}

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
		try {
			db.deleteUser(testUserC.getUserName());
			db.deleteUser(testUserA.getUserName());
		} catch (NoSuchUserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}

		// There should be one user
		List<User> users = db.getUsers();
		assertEquals(users.size(), 1);

		// That user should have the same information as testUserB
		assertUserFieldsEqual(users.get(0), testUserB);
	}

	@Test
	public void testUserExists() {
		// Create an in-memory test database
		Database db = new Database(null);

		User testUser = createTestUserWithRandomInfo();

		db.addUser(testUser);

		assertTrue(db.userExists(testUser.getUserName()));
		assertFalse(db.userExists(testUser.getUserName() + "0"));
	}

	@Test
	public void testPasswordMatches() {
		// Create an in-memory test database
		Database db = new Database(null);

		User testUser = createTestUserWithRandomInfo();

		db.addUser(testUser);

		try {
			assertEquals(db.getPassword(testUser.getUserName()),
					testUser.getPasswordHash());
		} catch (NoSuchUserException e) {
			fail();
		}

		try {
			assertNotEquals(db.getPassword(testUser.getUserName()),
					testUser.getPasswordHash() + "0");
		} catch (NoSuchUserException e) {
			fail();
		}
	}

	@Test
	public void testChangePassword() {
		// Create an in-memory test database
		Database db = new Database(null);

		User testUser = createTestUserWithRandomInfo();

		db.addUser(testUser);

		testUser.setPasswordHash(randomString());

		db.changePassword(testUser.getUserName(), testUser.getPasswordHash());

		User userInDB = null;
		try {
			userInDB = db.getUser(testUser.getUserName());
		} catch (NoSuchUserException e) {
			fail();
		}

		assertUserFieldsEqual(testUser, userInDB);
	}

	@Test
	public void testChangeSecurityQuestionAndAnswer() {
		// Create an in-memory test database
		Database db = new Database(null);

		User testUser = createTestUserWithRandomInfo();

		db.addUser(testUser);

		testUser.setSecurityQuestion(randomString());
		testUser.setSecurityAnswer(randomString());

		db.changeSecurityQuestionAndAnswer(testUser.getUserName(),
				testUser.getSecurityQuestion(), testUser.getSecurityAnswer());

		User userInDB = null;
		try {
			userInDB = db.getUser(testUser.getUserName());
		} catch (NoSuchUserException e) {
			fail();
		}

		assertUserFieldsEqual(testUser, userInDB);
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
