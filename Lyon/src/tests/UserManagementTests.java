package tests;

import static org.junit.Assert.*;
import main.UserManagement;

import org.junit.Test;

public class UserManagementTests
{

	@Test
	public void testPasswordValidation() {
		assertTrue(UserManagement.validatePassword("Orange123"));
		assertFalse(UserManagement.validatePassword("OrangeApp"));
		assertFalse(UserManagement.validatePassword("orange123"));
		assertFalse(UserManagement.validatePassword("!!!!!!!!!!"));
		assertTrue(UserManagement.validatePassword("Orange!1"));
		assertTrue(UserManagement.validatePassword("Orange?1"));
		assertTrue(UserManagement.validatePassword("Orang**1"));
		assertTrue(UserManagement.validatePassword("Orang&&1"));
		assertFalse(UserManagement.validatePassword("&rang123"));
		assertFalse(UserManagement.validatePassword("Orange12345"));
		assertFalse(UserManagement.validatePassword("^Orange123"));
		assertFalse(UserManagement.validatePassword("orange123?"));
		assertFalse(UserManagement.validatePassword("O123"));
	}

}
