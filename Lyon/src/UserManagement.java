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
	private verify vy;
	
	public UserManagement()
	{
		
	}
	
	public void command(ArrayList<String> cmd)
	{
		String toDo = cmd.get(0);
		ArrayList<String> nextToDo = cmd.remove(0);
		switch (toDo)
		{
		case "add":
		//what to do when adding user
			cmd.remove(0);
			add(cmd);
		break;
		case "delete":
			cmd.remove(0);
		//what to do when deleting 
		break;
		case  "sign in":
			cmd.remove(0);
			
		//Check verification
		break;
		case  "sign up":
			cmd.remove(0);
			
		//Check verification
		break;
		}
		
		
	}
	
	public void add(ArrayList<String> cmd)
	{
		
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

}
