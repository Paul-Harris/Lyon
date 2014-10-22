package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import main.User;
import main.UserManagement;
import main.UserManagement.InvalidPasswordException;

import org.junit.Test;

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
	public void testSignUp() {
		UserManagement um = new UserManagement();
		
		assertFalse(um.checkSuccess());

		String[] signUpArgs = new String[] {
				"signup", "username", "password", "full name", "securityQ",
				"securityA"
		};
		System.out.println(um.command(
				new ArrayList<String>(Arrays.asList(signUpArgs)), true));
		assertFalse(um.checkSuccess());

		signUpArgs = new String[] {
				"signup", "username", "Password1", "full name", "securityQ",
				"securityA"
		};
		System.out.println(um.command(
				new ArrayList<String>(Arrays.asList(signUpArgs)), true));
		assertTrue(um.checkSuccess());

	}
}
