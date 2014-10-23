package main;

/**
 * This class contains the entry point to the program. It will either accept
 * commands from the command line or launch the GUI if no commands are provided.
 * 
 * @author The Bomb Squad (Samantha Beaston, Jacob Coleman, Jin Cho, Austin
 *         Harris, Tim Zorca)
 * @version October 23, 2014
 * 
 */
public class Run
{
	public static void main(String[] args) {
		if (args.length > 0)
			new UserManagement(args);
		else {
			GraphicalUserInterface.displayLoginFrame(true);
		}
	}
}
