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
	private Database db;
	private PasswordSecurity ps;
		
	//Receives from other java classes
	
	//Receives commands from command line
	public UserManagement(String[] args) {
		ArrayList<String> cmd = null;
		
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
		}	
	}
	
	public void delete(ArrayList<String> cmd)
	{
		
	}
	
	public boolean signIn(ArrayList<String> cmd)
	{
		System.out.println(cmd.toString());
		//passes userName 0 and password 1 from cmd array
		//BREAKS HERE! IT GIVES NULL POINTER BUT TO THE AWT WINDOW THAT 
		//COMES UP TO GIVE A SUCCESS DIALOG AFER ONE LOGS IN 
		
		return ps.verifyPassword(cmd.get(0),cmd.get(1));
	}
	
	public void signUp(ArrayList<String> cmd)
	{
		
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
			
			if(db.getPassword(userName) == createHash(password))
				return true;
			else
				return false;
			
		}
		
		
	}
}
