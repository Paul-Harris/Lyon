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
import javax.swing.JTextField;

public class GraphicalUserInterface {

	public static JFrame loginFrame = new JFrame("Password Manager");
	public static JFrame registerFrame = new JFrame("Password Manager");
	public static JFrame adminFrame = new JFrame("Password Manager");
	public static JFrame editFrame = new JFrame("Password Manager");
	public static JFrame forgotFrame = new JFrame("Password Manager");
	static UserManagement um;
	static ArrayList<String> cmd;
	private static JTextField roleText;

	public static void main(String[] args) {
		loginFrame(true);
	}

	static void loginFrame(boolean visibility) {

		loginFrame.setSize(300, 200);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setVisible(visibility);

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

		final JTextField passwordText = new JTextField(20);
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
				forgotFrame(true);
			}
		});

		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				cmd = new ArrayList<String>(Arrays.asList("signin",
						userText.getText(), passwordText.getText()));
				um = new UserManagement();
				String message = um.command(cmd);

				JButton source = (JButton) e.getSource();
				if (um.checkSuccess() == true) {

					JOptionPane.showMessageDialog(source, message);
					registerFrame.dispose();
				} else
					JOptionPane.showMessageDialog(source, message);

			}
		});

		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				registerFrame(true);
			}
		});
	}

	private static void registerFrame(boolean visibility) {

		registerFrame.setSize(350, 300);
		registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		registerFrame.setVisible(visibility);

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

		final JTextField passwordText = new JTextField(20);
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

				cmd = new ArrayList<String>(Arrays.asList("register",
						userNameText.getText(), passwordText.getText(),
						nameText.getText(), questionText.getText(),
						answerText.getText()));
				um = new UserManagement();
				String message = um.command(cmd);

				JButton source = (JButton) e.getSource();
				if (um.checkSuccess() == true) {

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

	}

	static void adminFrame(boolean visibility) {

		adminFrame.setSize(350, 150);
		adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		adminFrame.setVisible(visibility);

		adminFrame.getContentPane().setLayout(null);

		JLabel userLabel = new JLabel("User Name");
		userLabel.setBounds(10, 10, 80, 25);
		adminFrame.getContentPane().add(userLabel);

		final JTextField userText = new JTextField(20);
		userText.setBounds(150, 10, 160, 25);
		adminFrame.getContentPane().add(userText);

		JLabel pwLabel = new JLabel("User Password");
		pwLabel.setBounds(10, 40, 100, 25);
		adminFrame.getContentPane().add(pwLabel);

		final JTextField pwText = new JTextField(20);
		pwText.setBounds(150, 40, 160, 25);
		adminFrame.getContentPane().add(pwText);

		JButton editButton = new JButton("Edit");
		editButton.setBounds(10, 80, 100, 25);
		adminFrame.getContentPane().add(editButton);

		JButton deleteButton = new JButton("Delete");
		deleteButton.setBounds(160, 80, 100, 25);
		adminFrame.getContentPane().add(deleteButton);

		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editFrame(true, userText.getText(), pwText.getText());
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// RUN DELETE
			}
		});
	}

	static void editFrame(boolean visibility, String username, String password) {

		editFrame.setSize(350, 200);
		editFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		editFrame.setVisible(visibility);

		editFrame.getContentPane().setLayout(null);

		JLabel userLabel = new JLabel("User Name");
		userLabel.setBounds(10, 10, 80, 25);
		editFrame.getContentPane().add(userLabel);

		final JTextField userText = new JTextField(20);
		userText.setBounds(150, 10, 160, 25);
		userText.setText(username);
		editFrame.getContentPane().add(userText);

		JLabel pwLabel = new JLabel("Old Password");
		pwLabel.setBounds(10, 40, 120, 25);
		editFrame.getContentPane().add(pwLabel);

		final JTextField pwText = new JTextField(20);
		pwText.setBounds(150, 40, 160, 25);
		pwText.setText(password);
		editFrame.getContentPane().add(pwText);

		JLabel newpwLabel = new JLabel("New Password");
		newpwLabel.setBounds(10, 70, 120, 25);
		editFrame.getContentPane().add(newpwLabel);

		final JTextField newpwText = new JTextField(20);
		newpwText.setBounds(150, 70, 160, 25);
		editFrame.getContentPane().add(newpwText);

		JButton saveButton = new JButton("Save");
		saveButton.setBounds(10, 100, 100, 25);
		editFrame.getContentPane().add(saveButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(160, 100, 100, 25);
		editFrame.getContentPane().add(cancelButton);

		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ADD CHANGE PASSWORD WITH OLD PASSWORD
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editFrame.dispose();
			}
		});
	}

	static void forgotFrame(boolean visibility) {

		forgotFrame.setSize(350, 250);
		forgotFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		forgotFrame.setVisible(visibility);

		forgotFrame.getContentPane().setLayout(null);

		JLabel userLabel = new JLabel("User Name");
		userLabel.setBounds(10, 10, 120, 25);
		forgotFrame.getContentPane().add(userLabel);

		final JTextField userText = new JTextField(20);
		userText.setBounds(150, 10, 160, 25);
		forgotFrame.getContentPane().add(userText);

		JLabel questionLabel = new JLabel("Security Question");
		questionLabel.setBounds(10, 40, 120, 25);
		forgotFrame.getContentPane().add(questionLabel);

		final JTextField questionText = new JTextField(20);
		questionText.setBounds(150, 40, 160, 25);
		forgotFrame.getContentPane().add(questionText);

		JLabel answerLabel = new JLabel("Security Answer");
		answerLabel.setBounds(10, 80, 120, 25);
		forgotFrame.getContentPane().add(answerLabel);

		final JTextField answerText = new JTextField(20);
		answerText.setBounds(150, 80, 160, 25);
		forgotFrame.getContentPane().add(answerText);

		JLabel newpwLabel = new JLabel("New Password");
		newpwLabel.setBounds(10, 120, 120, 25);
		forgotFrame.getContentPane().add(newpwLabel);

		final JTextField newpwText = new JTextField(20);
		newpwText.setBounds(150, 120, 160, 25);
		forgotFrame.getContentPane().add(newpwText);

		JButton saveButton = new JButton("Save");
		saveButton.setBounds(10, 150, 100, 25);
		forgotFrame.getContentPane().add(saveButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(160, 150, 100, 25);
		forgotFrame.getContentPane().add(cancelButton);

		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmd = new ArrayList<String>(Arrays.asList("resetpassword",
						userText.getText(), answerText.getText(),
						newpwText.getText()));
				um = new UserManagement();
				String message = um.command(cmd);

				JButton source = (JButton) e.getSource();
				if (um.checkSuccess() == true) {

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
	}

}
