package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Database.DatabaseException;
import main.Database.NoSuchUserException;
import main.User;
import main.UserManagement;
import main.UserManagement.IncorrectPasswordException;
import main.UserManagement.InsufficientRightsException;

import org.junit.Test;

/**
 * This class uses JUnit testing methods to test user management functionality.
 * 
 * @author The Bomb Squad (Samantha Beaston, Jacob Coleman, Jin Cho, Austin
 *         Harris, Tim Zorca)
 * @version October 23, 2014
 * 
 */
public class UserManagementTests
{
	@Test
	public void testPasswordValidation() {
		assertTrue(UserManagement.validatePassword("Orange123"));
		assertTrue(UserManagement.validatePassword("Orange!1"));
		assertTrue(UserManagement.validatePassword("Orange?1"));
		assertTrue(UserManagement.validatePassword("Orang**1"));
		assertTrue(UserManagement.validatePassword("Orang&&1"));

		assertFalse(UserManagement.validatePassword("OrangeApp"));
		assertFalse(UserManagement.validatePassword("orange123"));
		assertFalse(UserManagement.validatePassword("!!!!!!!!!!"));
		assertFalse(UserManagement.validatePassword("&rang123"));
		assertFalse(UserManagement.validatePassword("Orange12345"));
		assertFalse(UserManagement.validatePassword("^Orange123"));
		assertFalse(UserManagement.validatePassword("orange123?"));
		assertFalse(UserManagement.validatePassword("O123"));
	}

	@Test
	public void testSignUp() throws NoSuchUserException, DatabaseException,
			IncorrectPasswordException, InsufficientRightsException {
		UserManagement um = new UserManagement();

		um.useInMemoryTestDB(true);

		assertFalse(um.checkSuccess());

		String[] signUpArgs = new String[] { "signup", "username", "password",
				"full name", "securityQ", "securityA" };
		System.out.println(um.command(new ArrayList<String>(Arrays
				.asList(signUpArgs))));
		assertFalse(um.checkSuccess());

		signUpArgs = new String[] { "signup", "username", "Password1",
				"full name", "securityQ", "securityA" };
		System.out.println(um.command(new ArrayList<String>(Arrays
				.asList(signUpArgs))));
		assertTrue(um.checkSuccess());

	}

	@Test
	public void testSignUpAndSignIn() {
		UserManagement um = new UserManagement();

		um.useInMemoryTestDB(true);

		// Sign up
		String[] args = new String[] { "signup", "username", "Password1",
				"full name", "securityQ", "securityA" };
		System.out
				.println(um.command(new ArrayList<String>(Arrays.asList(args))));
		assertTrue(um.checkSuccess());

		// Sign in with valid password
		args = new String[] { "signin", "username", "Password1" };
		System.out
				.println(um.command(new ArrayList<String>(Arrays.asList(args))));
		assertTrue(um.checkSuccess());

		// Sign in with invalid password
		args = new String[] { "signin", "username", "Passwodr1" };
		System.out
				.println(um.command(new ArrayList<String>(Arrays.asList(args))));
		assertFalse(um.checkSuccess());

	}
}
