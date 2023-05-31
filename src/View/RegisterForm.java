package View;

import Controller.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * This class creates the register frame of the application.
 */
public class RegisterForm implements ActionListener {

    private JPanel registerPanel;
    private Client c;
    private JTextField passwordTextField;
    private JTextField usernameTextField;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JButton registerButton;
    private JLabel emailLabel;
    private JTextField emailTextField;
    private JLabel dateOfBirthLabel;
    private JTextField dateOfBirthTextField;
    private JButton goBackButton;

    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JTextField firstNameTextField;
    private JTextField lastNameTextField;


    public RegisterForm(Client c) {
        //Assign the client variable to the local one.
        this.c = c;

        //construct components
        firstNameLabel = new JLabel("First name:");
        firstNameTextField = new JTextField(5);
        lastNameLabel = new JLabel("Last name:");
        lastNameTextField = new JTextField(5);

        passwordTextField = new JTextField (5);
        usernameTextField = new JTextField (5);
        userNameLabel = new JLabel ("Username:");
        passwordLabel = new JLabel ("Password:");
        registerButton = new JButton ("Register");
        emailLabel = new JLabel ("Email:");
        emailTextField = new JTextField (5);
        dateOfBirthLabel = new JLabel ("Date of Birth:");
        dateOfBirthTextField = new JTextField (5);
        goBackButton = new JButton("Go Back");

        //adjust size and set layout
        registerPanel = new JPanel();
        registerPanel.setPreferredSize (new Dimension(944, 569));
        registerPanel.setLayout (null);

        //add components
        registerPanel.add(firstNameLabel);
        registerPanel.add(firstNameTextField);
        registerPanel.add(lastNameLabel);
        registerPanel.add(lastNameTextField);
        registerPanel.add (passwordTextField);
        registerPanel. add (usernameTextField);
        registerPanel.add (userNameLabel);
        registerPanel.add (passwordLabel);
        registerPanel.add (registerButton);
        registerPanel.add (emailLabel);
        registerPanel.add (emailTextField);
        registerPanel.add (dateOfBirthLabel);
        registerPanel.add (dateOfBirthTextField);
        registerPanel.add (goBackButton);

        //set component bounds (only needed by Absolute Positioning)
        firstNameLabel.setBounds(389,75,100,25);
        firstNameTextField.setBounds(389,25+80,165,25);
        lastNameLabel.setBounds(389,55+80,100,25);
        lastNameTextField.setBounds(389,85+80,165,25);

        userNameLabel.setBounds (389, 115+80, 100, 25);
        usernameTextField.setBounds (389, 145+80, 165, 25);
        passwordLabel.setBounds (389, 175+80, 100, 25);
        passwordTextField.setBounds (389, 205+80, 165, 25);
        emailLabel.setBounds (389, 235+80, 100, 25);
        emailTextField.setBounds (389, 265+80, 165, 25);
        dateOfBirthLabel.setBounds (389, 295+80, 100, 25);
        dateOfBirthTextField.setBounds (389, 325+80, 165, 25);
        registerButton.setBounds (412, 380+80, 120, 25);
        goBackButton.setBounds (412, 420+80, 120, 25);

        //Add listeners for the buttons.
        addListeners();
    }

    /**
     * Adds a ActionListener and ActionCommands to the buttons.
     */
    public void addListeners(){
        registerButton.addActionListener(this);
        registerButton.setActionCommand("register");
        goBackButton.addActionListener(this);
        goBackButton.setActionCommand("goBack");
    }

    /**
     * Returns the register panel.
     * @return registerPanel
     */
    public JPanel getRegisterPanel(){
        return this.registerPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        try {
            switch(action){
                case "register":
                    //Register user to database and switch view to login again.
                    c.sendUserToServerRegister(usernameTextField.getText(), passwordTextField.getText(), dateOfBirthTextField.getText(), emailTextField.getText(), false);
                    c.getMainForm().setLoginPanel();
                    break;
                case "goBack":
                    //Go back to the login panel.
                    c.getMainForm().setLoginPanel();
                    break;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
