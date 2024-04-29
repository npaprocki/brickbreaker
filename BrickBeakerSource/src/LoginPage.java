import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPage implements ActionListener {
	
	// some global variables for creating the login page
	JFrame frame = new JFrame();
	
	// login and reset buttons
	JButton loginButton = new JButton("Login");
	JButton resetButton = new JButton("Reset");
	
	// text fields for input
	JTextField userIDField = new JTextField();
	JPasswordField userPasswordField = new JPasswordField();
	
	// labels for text fields
	JLabel userIDLabel = new JLabel("User ID:");
	JLabel userPasswordLabel = new JLabel("Password:");
	
	// label for message that pops up if username or password is not correct
	JLabel messageLabel = new JLabel();
	
	// an extra map for storing the usernames and passwords so we can change this one
	// and leave the original map alone.
	Map<String, String> logins = new HashMap<>();

	/*
	 * constructor that takes the map of usernames and passwords as a parameter. It
	 * stores the original Map (loginsOriginal) into the extra one (logins). Then it
	 * sets up the JFrame with the variables above and puts them in their spots on the frame.
	 */
	public LoginPage(Map<String, String> loginsOriginal) {
		logins = loginsOriginal;
		
		// sets up the labels for the frame
		userIDLabel.setBounds(50, 100, 75, 25);
		userPasswordLabel.setBounds(50, 150, 75, 25);
		messageLabel.setBounds(125, 250, 250, 35);
		messageLabel.setFont(new Font(null, Font.ITALIC, 25));
		
		// sets up the text fields
		userIDField.setBounds(125, 100, 200, 25);
		userPasswordField.setBounds(125, 150, 200, 25);
		
		// sets up buttons and adds an action listener so it knows when its pressed
		loginButton.setBounds(125, 200, 100, 25);
		loginButton.setFocusable(false);
		loginButton.addActionListener(this);
		resetButton.setBounds(225, 200, 100, 25);
		resetButton.setFocusable(false);
		resetButton.addActionListener(this);
		
		// adds all that stuff into the JFrame
		frame.add(userIDLabel);
		frame.add(userPasswordLabel);
		frame.add(messageLabel);
		frame.add(userIDField);
		frame.add(userPasswordField);
		frame.add(loginButton);
		frame.add(resetButton);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(420, 420);
		frame.setLayout(null);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// resets the text fields if the reset button is pressed
		if (e.getSource() == resetButton) {
			userIDField.setText("");
			userPasswordField.setText("");
		}
		// if the login button is pressed, it gets the values from each text field
		if (e.getSource() == loginButton) {
			String userID = userIDField.getText();
			String password = String.valueOf(userPasswordField.getPassword());
			
			// If the username is in the Map...
			if (logins.containsKey(userID)) {
				
				// and if the password matches what is in the map, it makes the game JFrame
				if (logins.get(userID).equals(password)) {
					messageLabel.setForeground(Color.green);
					messageLabel.setText("Login Successful");
					
					// gets rid of the login JFrame since the login was successful
					frame.dispose();
					
					// creates the game JFrame with the user's ID as the parameter so we can
					// keep track of their personal high score and record their name on leaderboard
					JFrame obj = new JFrame();
					Gameplay gamePlay = new Gameplay(userID);
					obj.setBounds(10, 10, 700, 600);
					obj.setTitle("Brick Breaker");
					obj.setResizable(false);
					obj.setVisible(true);
					obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					obj.add(gamePlay);
					
				// and if the password does not match (is incorrect), it displays a "login unsuccessful" message
				} else {
					messageLabel.setForeground(Color.red);
					messageLabel.setText("Login Unsuccessful");
				}
			// if the Map does not contain the user ID (is a new user), it will add their userID and password to
			// the map and write the updated map back to users.txt
			} else {
				logins.put(userID, password);
				
				// writing updated Map back to users.txt
				File outFile = new File("users.txt");
				PrintWriter output = null;
				try {
					output = new PrintWriter(outFile);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				List<String> keys = new ArrayList<String>(logins.keySet());
				for (String s : keys) {
					output.println(s + " " + logins.get(s));
				}
				output.close();
				
				// then gets rid of login page and opens up game JFrame like above
				frame.dispose();
				JFrame obj = new JFrame();
				Gameplay gamePlay = new Gameplay(userID);
				obj.setBounds(10, 10, 700, 600);
				obj.setTitle("Brick Breaker");
				obj.setResizable(false);
				obj.setVisible(true);
				obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				obj.add(gamePlay);
			}
		}
	}
	
}
