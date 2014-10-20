package main;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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
	private String customerAPI = "emtpy";	
	//Receives from other java classes
	
	//Receives commands from command line
	public UserManagement(String[] args) {
		ArrayList<String> cmd = new ArrayList<String>();
		
		for (int i = 0; i > args.length; i ++)
			cmd.add(args[i]);
		
		this.command(cmd);
	}
	
	//Receives commands from java api
	public UserManagement(ArrayList<String> cmd)
	{
		command(cmd);
		
	}
	public void command(ArrayList<String> cmd)
	{
		String toDo = cmd.remove(0);
		switch (toDo)
		{
		case "delete":
			delete(cmd);
		//what to do when deleting 
		break;
		case  "signin":
			
			signIn(cmd);
		//Check verification
			
		break;
		case  "signup":
			signUp(cmd);
		//Check verification
		break;
		case "resetpassword":
			resetPassword(cmd);
		break;
		}	
	}
	
	public void delete(ArrayList<String> cmd)
	{
		
		
	}
	
	public void signIn(ArrayList<String> cmd)
	{
		if( ps.verifyPassword(cmd.get(0), cmd.get(1)) == true)
		{
			success = true;
		}
		
		//executes shell command to send back information to customers application 
		esc.executeCommand(customerAPI);
		
	}
	

	public void signUp(ArrayList<String> cmd)
	{
		
		ArrayList<String> newUser = new ArrayList<String>();
		
		for(int i = 0; cmd.size() > i; i ++)
		{
			if(i == 3)
				newUser.add(ps.createHash(cmd.get(i)));
			else
				newUser.add(cmd.get(i));
		

		}
		//db.addUser(newUser);
		success = true;
	}

	
	public void resetPassword(ArrayList<String> cmd)
	{
		//checks to see if the security answer matches what is in the BD
		if(cmd.get(1).equals(db.getSecurityAnswer(cmd.get(0))) == true)
		{
			db.changePassword(cmd.get(0), ps.createHash(cmd.get(2)));
			
			//Verifies that the password was succcesfully changed
			if( db.getPassword(cmd.get(0)).equals(ps.createHash(cmd.get(2))) == true)
				success = true;
		}
		
	}

	public boolean checkSuccess() 
	{
		return success;
	}
	
	
	private class PasswordSecurity
	{
		public String createHash(String password)
		{
			
	        MessageDigest md;
	        String hash = "empty";
			try {
				md = MessageDigest.getInstance("SHA-512");
			
	        byte[] bytes = md.digest(password.getBytes());
	        StringBuilder sb = new StringBuilder();
	        for(int i=0; i< bytes.length ;i++)
	        {
	            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        
	       //returns the hash of the password
	        hash =  sb.toString();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return hash;
			
		}
		public boolean verifyPassword(String userName, String password)
		{	
			if(db.getPassword(userName).equals(createHash(password))==true)
			{
				return true;
			}
			else
				return false;
		}
		
		
	}
	
	//Sends responses to users custom program via shell
	public class ExecuteShellComand {
	
		private String executeCommand(String command) {
	 
			StringBuffer output = new StringBuffer();
	 
			Process p;
			try {
				p = Runtime.getRuntime().exec(command);
				p.waitFor();
				BufferedReader reader = 
	                            new BufferedReader(new InputStreamReader(p.getInputStream()));
	 
	                        String line = "";			
				while ((line = reader.readLine())!= null) {
					output.append(line + "\n");
				}
	 
			} catch (Exception e) {
				e.printStackTrace();
			}
	 
			return output.toString();
	 
		}
	 
	}
	
	
}

