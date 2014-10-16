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
	private verification verify;
	
	public UserManagement()
	{
		
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
		case  "sign in":
			signIn(cmd);
		//Check verification
		break;
		case  "sign up":
			signUp(cmd);
		//Check verification
		break;
		}	
	}
	
	public void delete(ArrayList<String> cmd)
	{
		
	}
	
	public void signIn(ArrayList<String> cmd)
	{
		
	}
	
	public void signUp(ArrayList<String> cmd)
	{
		
	}
	
	
	private class verification()
	{
		public verification()
		{
			
		}
		
		
	}
}
