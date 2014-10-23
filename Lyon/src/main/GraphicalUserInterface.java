package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * GraphicalUserInterface is the graphical human usable interface for the Lyon
 * password management system.
 * 
 * @author The Bomb Squad (Samantha Beaston, Jacob Coleman, Jin Cho, Austin
 *         Harris, Tim Zorca)
 * @version October 23, 2014
 * 
 */
public class GraphicalUserInterface
{
	private static JFrame loginFrame, registerFrame, forgotFrame;
	private static UserManagement userManagement = new UserManagement();

	public static void main(String[] args) {
		displayLoginFrame(true);
	}

	static void displayLoginFrame(boolean visibility) {
		loginFrame = new JFrame("Password Manager");
		loginFrame.setSize(300, 200);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		loginFrame.getContentPane().setLayout(null);

		JLabel userLabel = new JLabel("User Name");
		userLabel.setBounds(10, 10, 80, 25);
		loginFrame.getContentPane().add(userLabel);

		final JTextField userText = new JTextField(20);
		userText.setBounds(100, 10, 160, 25);
		loginFrame.getContentPane().add(userText);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 40, 80, 25);
		loginFrame.getContentPane().add(passwordLabel);

		final JPasswordField passwordText = new JPasswordField(20);
		passwordText.setBounds(100, 40, 160, 25);
		loginFrame.getContentPane().add(passwordText);

		JButton forgotButton = new JButton("Forgot Password");
		forgotButton.setBounds(70, 80, 150, 25);
		loginFrame.getContentPane().add(forgotButton);

		JButton loginButton = new JButton("Login");
		loginButton.setBounds(10, 110, 100, 25);
		loginFrame.getContentPane().add(loginButton);

		JButton registerButton = new JButton("Register");
		registerButton.setBounds(160, 110, 100, 25);
		loginFrame.getContentPane().add(registerButton);

		forgotButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayForgotFrame(true);
			}
		});

		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ArrayList<String> cmd = new ArrayList<String>(Arrays.asList(
						"signin", userText.getText(),
						String.valueOf(passwordText.getPassword())));
				String message = userManagement.command(cmd);

				JButton source = (JButton) e.getSource();
				if (userManagement.checkSuccess() == true) {

					JOptionPane.showMessageDialog(source, message);

					if (registerFrame != null) {
						registerFrame.dispose();
					}
				} else
					JOptionPane.showMessageDialog(source, message);

			}
		});

		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayRegisterFrame(true);
			}
		});

		loginFrame.setLocationRelativeTo(null); // center on screen
		loginFrame.setVisible(visibility);
	}

	private static void displayRegisterFrame(boolean visibility) {
		registerFrame = new JFrame("Password Manager");
		registerFrame.setSize(350, 250);
		registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		registerFrame.getContentPane().setLayout(null);

		// Username
		JLabel userNameLabel = new JLabel("UserName");
		userNameLabel.setBounds(10, 10, 80, 25);
		registerFrame.getContentPane().add(userNameLabel);

		final JTextField userNameText = new JTextField(20);
		userNameText.setBounds(150, 10, 160, 25);
		registerFrame.getContentPane().add(userNameText);

		// Password
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 40, 80, 25);
		registerFrame.getContentPane().add(passwordLabel);

		final JPasswordField passwordText = new JPasswordField(20);
		passwordText.setBounds(150, 40, 160, 25);
		registerFrame.getContentPane().add(passwordText);

		// Full Name
		JLabel nameLabel = new JLabel("Full Name");
		nameLabel.setBounds(10, 70, 80, 30);
		registerFrame.getContentPane().add(nameLabel);

		final JTextField nameText = new JTextField(20);
		nameText.setBounds(150, 70, 160, 25);
		registerFrame.getContentPane().add(nameText);

		// Security Question
		JLabel questionLabel = new JLabel("Security Question");
		questionLabel.setBounds(10, 100, 120, 25);
		registerFrame.getContentPane().add(questionLabel);

		final JTextField questionText = new JTextField(20);
		questionText.setBounds(150, 100, 160, 25);
		registerFrame.getContentPane().add(questionText);

		// Security Answer
		JLabel answerLabel = new JLabel("Security Answer");
		answerLabel.setBounds(10, 130, 100, 25);
		registerFrame.getContentPane().add(answerLabel);

		final JTextField answerText = new JTextField(20);
		answerText.setBounds(150, 130, 160, 25);
		registerFrame.getContentPane().add(answerText);

		// register
		JButton registerButton = new JButton("Register");
		registerButton.setBounds(50, 160, 100, 25);
		registerFrame.getContentPane().add(registerButton);

		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton source = (JButton) e.getSource();

				if (userNameText.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(source,
							"Username cannot be blank.");
					return;
				}

				ArrayList<String> cmd = new ArrayList<String>(Arrays.asList(
						"register", userNameText.getText(),
						String.valueOf(passwordText.getPassword()),
						nameText.getText(), questionText.getText(),
						answerText.getText()));
				String message = userManagement.command(cmd);

				if (userManagement.checkSuccess() == true) {

					JOptionPane.showMessageDialog(source, message);
					registerFrame.dispose();
				} else
					JOptionPane.showMessageDialog(source, message);
			}
		});

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(180, 160, 100, 25);
		registerFrame.getContentPane().add(cancelButton);

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				registerFrame.dispose();
			}
		});

		registerFrame.setLocationRelativeTo(null); // center on screen
		registerFrame.setVisible(visibility);
	}

	static void displayForgotFrame(boolean visibility) {
		forgotFrame = new JFrame("Forgot Password");
		forgotFrame.setSize(350, 260);
		forgotFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		forgotFrame.getContentPane().setLayout(null);

		JLabel userLabel = new JLabel("User Name");
		userLabel.setBounds(10, 10, 120, 25);
		forgotFrame.getContentPane().add(userLabel);

		final JTextField userText = new JTextField(20);
		userText.setBounds(150, 10, 160, 25);
		forgotFrame.getContentPane().add(userText);

		JButton questionButton = new JButton("Show Question");
		questionButton.setBounds(10, 40, 120, 25);
		questionButton.setHorizontalAlignment(SwingConstants.LEFT);
		forgotFrame.getContentPane().add(questionButton);

		final JTextArea questionText = new JTextArea();
		questionText.setEditable(false);
//		questionText.setEnabled(false);
		questionText.setBorder(userText.getBorder());
		questionText.setBounds(150, 40, 160, 60);
		questionText.setForeground(Color.BLACK);
		forgotFrame.getContentPane().add(questionText);

		JLabel answerLabel = new JLabel("Security Answer");
		answerLabel.setBounds(10, 110, 120, 25);
		forgotFrame.getContentPane().add(answerLabel);

		final JTextField answerText = new JTextField(20);
		answerText.setBounds(150, 110, 160, 25);
		forgotFrame.getContentPane().add(answerText);

		JLabel newpwLabel = new JLabel("New Password");
		newpwLabel.setBounds(10, 150, 120, 25);
		forgotFrame.getContentPane().add(newpwLabel);

		final JPasswordField newpwText = new JPasswordField(20);
		newpwText.setBounds(150, 150, 160, 25);
		forgotFrame.getContentPane().add(newpwText);

		JButton saveButton = new JButton("Save");
		saveButton.setBounds(10, 190, 100, 25);
		forgotFrame.getContentPane().add(saveButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(160, 190, 100, 25);
		forgotFrame.getContentPane().add(cancelButton);

		questionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton source = (JButton) e.getSource();
				if (userText.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(source,
							"Please enter your username.");
					return;
				}

				ArrayList<String> cmd = new ArrayList<String>(Arrays.asList(
						"showsecurityquestion", userText.getText()));

				String message = userManagement.command(cmd);

				if (userManagement.checkSuccess() == true) {
					questionText.setText(message);
				} else {
					JOptionPane.showMessageDialog(source, message);
				}
			}
		});

		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> cmd = new ArrayList<String>(Arrays.asList(
						"resetpassword", userText.getText(),
						answerText.getText(),
						String.valueOf(newpwText.getPassword())));
				String message = userManagement.command(cmd);

				JButton source = (JButton) e.getSource();
				if (userManagement.checkSuccess() == true) {

					JOptionPane.showMessageDialog(source, message);
					forgotFrame.dispose();
				} else
					JOptionPane.showMessageDialog(source, message);
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				forgotFrame.dispose();
			}
		});

		forgotFrame.setLocationRelativeTo(null); // center on screen
		forgotFrame.setVisible(visibility);
	}
}
