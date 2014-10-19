package main;
/**
 * @author The Bomb Squad
 * Created : October 14, 2014
 * Purpose : GraphicalUserInterface is the graphical human usable interface to the Lyon password management system 
 * Interactions : 
 * 
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class GraphicalUserInterface {
	
	public static JFrame loginFrame = new JFrame("Password Manager");
	public static JFrame registerFrame = new JFrame("Password Manager");
	static UserManagement um;
	static ArrayList<String> cmd;

	public static void main(String[] args) {
		loginFrame(true);
	}

	static void loginFrame(boolean visibility) {
		
		loginFrame.setSize(300, 150);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setVisible(visibility);
		
		loginFrame.setLayout(null);

		JLabel userLabel = new JLabel("User Name");
		userLabel.setBounds(10, 10, 80, 25);
		loginFrame.add(userLabel);

		final JTextField userText = new JTextField(20);
		userText.setBounds(100, 10, 160, 25);
		loginFrame.add(userText);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 40, 80, 25);
		loginFrame.add(passwordLabel);

		final JPasswordField passwordText = new JPasswordField(20);
		passwordText.setBounds(100, 40, 160, 25);
		loginFrame.add(passwordText);

		JButton loginButton = new JButton("Login");
		loginButton.setBounds(10, 80, 100, 25);
		loginFrame.add(loginButton);

		JButton registerButton = new JButton("Register");
		registerButton.setBounds(160, 80, 100, 25);
		loginFrame.add(registerButton);


		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//DUMMY CODE
			//THIS IS WHERE THE API DOES IT'S WORK
			cmd = new ArrayList<String>(Arrays.asList("signin",userText.getText(),passwordText.getText()));
			
			
			um = new UserManagement(cmd);
			
			JButton source = (JButton) e.getSource();
			if(um.checkSuccess() == true)
			{
				
				JOptionPane.showMessageDialog(source, "Login successful. You may now proceed to your destination.");
				loginFrame.dispose();
			}
			else
				JOptionPane.showMessageDialog(source, "Incorrect Username or Password");
			
			
		}
	});
		
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				registerFrame(true);
			}
		});
	}
	
	
	private static void registerFrame(boolean visibility)
	{
		
		registerFrame.setSize(350, 300);
		registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		registerFrame.setVisible(visibility);
		
		registerFrame.setLayout(null);

		//First Name
		JLabel firstNameLabel = new JLabel("First Name");
		firstNameLabel.setBounds(10, 10, 80, 30);
		registerFrame.add(firstNameLabel);

		final JTextField firstNameText = new JTextField(20);
		firstNameText.setBounds(150, 10, 160, 25);
		registerFrame.add(firstNameText);
		
		//Last Name
		JLabel lastNameLabel = new JLabel("Last Name");
		lastNameLabel.setBounds(10, 40, 80, 25);
		registerFrame.add(lastNameLabel);

		final JTextField lastNameText = new JTextField(20);
		lastNameText.setBounds(150, 40, 160, 25);
		registerFrame.add(lastNameText);
		
		//Username
		JLabel userNameLabel = new JLabel("UserName");
		userNameLabel.setBounds(10, 70, 80, 25);
		registerFrame.add(userNameLabel);

		final JTextField userNameText = new JTextField(20);
		userNameText.setBounds(150, 70, 160, 25);
		registerFrame.add(userNameText);
		
		//Password
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 100, 80, 25);
		registerFrame.add(passwordLabel);

		final JPasswordField passwordText = new JPasswordField(20);
		passwordText.setBounds(150, 100, 160, 25);
		registerFrame.add(passwordText);
		
		//Security Question
		JLabel questionLabel = new JLabel("Security Question");
		questionLabel.setBounds(10, 130, 120, 25);
		registerFrame.add(questionLabel);

		final JTextField questionText = new JTextField(20);
		questionText.setBounds(150, 130, 160, 25);
		registerFrame.add(questionText);
		
		//Security Answer
		JLabel answerLabel = new JLabel("Security Answer");
		answerLabel.setBounds(10, 160, 100, 25);
		registerFrame.add(answerLabel);

		final JTextField answerText = new JTextField(20);
		answerText.setBounds(150, 160, 160, 25);
		registerFrame.add(answerText);

		//register
		JButton registerButton = new JButton("Register");
		registerButton.setBounds(50, 200, 100, 25);
		registerFrame.add(registerButton);
		
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//DUMMY CODE
			//THIS IS WHERE THE API DOES IT'S WORK
			
			cmd = new ArrayList<String>(Arrays.asList("signup",firstNameText.getText(),lastNameText.getText(),userNameText.getText(),
														passwordText.getText(),questionText.getText(),answerText.getText()));	
			um = new UserManagement(cmd);
			
			JButton source = (JButton) e.getSource();
			if (um.checkSuccess() ==true)
			{	
				
				JOptionPane.showMessageDialog(source, "Registration successful. You can now login.");
				registerFrame.dispose();
			}
			else
				JOptionPane.showMessageDialog(source, "Could Not Create Account");
		}
	});		
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(180, 200, 100, 25);
		registerFrame.add(cancelButton);
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				registerFrame.dispose();
			}
		});
		
		
	}
}

