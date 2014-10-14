/**
 * @author The Bomb Squad
 * Created : October 14, 2014
 * Purpose : UserManagment is the view of Lyon password management system.
 * Interactions : Being the controller is pulls data from the database and pushes changes and updates.
 * 
 */

import java.security.*;

public class UserManagement {
	
	
	
	

	private class PasswordManagement
	{
		
		
		protected createHash(String pwd)
		{
	
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			try { md.update(pwd);
			return hash
		}
		
	}

}
