package View;

import Controller.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * This class is the LoginForm where users can sign in or register.
 */
public class LoginForm implements ActionListener{

    /**
     * Declare variables
     */
    private JPanel panel;
    private JButton signInButton;
    private JTextField passwordTextField;
    private JTextField usernameTextField;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JButton registerButton;
    private JLabel errorLabel;
    private Client c;

    /**
     * Sets up the panel
     */
    public LoginForm(Client c) {
        //Assign the client variable to the local one.
        this.c = c;

        //construct components
        signInButton = new JButton("Sign in");
        passwordTextField = new JTextField (5);
        usernameTextField = new JTextField (5);
        userNameLabel = new JLabel ("Username:");
        passwordLabel = new JLabel ("Password:");
        registerButton = new JButton ("Register");
        errorLabel = new JLabel();

        //adjust size and set layout
        panel = new JPanel();
        panel.setPreferredSize (new Dimension(944, 569));
        panel.setLayout (null);

        //add components
        panel.add (signInButton);
        panel.add (passwordTextField);
        panel.add (usernameTextField);
        panel.add (userNameLabel);
        panel.add (passwordLabel);
        panel.add (registerButton);
        panel.add(errorLabel);

        //set component bounds (only needed by Absolute Positioning)
        signInButton.setBounds (410, 325, 120, 25);
        passwordTextField.setBounds (385, 270, 165, 25);
        usernameTextField.setBounds (385, 210, 165, 25);
        userNameLabel.setBounds (385, 180, 100, 25);
        passwordLabel.setBounds (385, 240, 100, 25);
        registerButton.setBounds (410, 370, 120, 25);
        errorLabel.setBounds(430, 410, 120, 25);

        //Add listeners for the buttons.
        addListeners();
    }

    /**
     * Handles failed login
     */
    public void failedToLogin(){
        errorLabel.setText("Login failure!");
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(true);
    }

    /**
     * Adds a ActionListener and ActionCommands to the buttons.
     */
    public void addListeners(){
        signInButton.addActionListener(this);
        signInButton.setActionCommand("signIn");
        registerButton.addActionListener(this);
        registerButton.setActionCommand("register");
    }


    /**
     * Function to return the login panel.
     * @return panel
     */
    public JPanel getLoginPanel(){
        return this.panel;
    }

    /**
     * Handles button clicks.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        try{
            switch(action){
                case "signIn":
                    c.sendUserToServerLogin(usernameTextField.getText(), passwordTextField.getText());
                    break;
                case "register":
                    c.getMainForm().setRegisterPanel();
                    break;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

