/**
 * @author The Bomb Squad
 * Created : October 74, 2014
 * Purpose : Run will allow the system to accepts commands from the command line or
 * will launch the graphics user interface when no commands are present
 * Interactions : Will start either the GUI for testing or will pass parameters to the UserManagement
 * controller class
 * 
 */
public class run {

	public static void main(String[] args) {
		
		UserManagement um;
		GraphicalUserInterface gui;
		if(args.length > 0)
			um = new UserManagement(args);
		else
		{
			gui = new GraphicalUserInterface();
			GraphicalUserInterface.loginFrame(true);
		}
		

	}

}
