package View;

import Controller.Client;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;

/**
 * This class is the main JFrame of the application.
 */
public class MainForm {
    private JFrame mainFrame;
    private Client c;
    private LoginForm loginForm;
    private RegisterForm registerForm;
    private MarketplaceForm productForm;
    private ProfileForm profileForm;

    /**
     * The mainform is contains a mainframe where
     * then depending on what view youre on adds the different
     * panels LoginForm/RegisterForm/ProductForm/ProfileForm
     *
     * This is the constructor and it sets up the mainframe
     */
    public MainForm(Client c){
        //Assign client variable.
        this.c = c;

        setLookAndFeel();
        mainFrame = new JFrame("Marketplace Application");
        mainFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(950, 600);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);

        //Create the applications forms and pass the client object to them.
        loginForm = new LoginForm(c);
        registerForm = new RegisterForm(c);
        productForm = new MarketplaceForm(c);
        profileForm = new ProfileForm(c);

        //Set the login panel.
        setLoginPanel();

        //Make the main frame visible.
        mainFrame.setVisible(true);
    }

    /**
     * Sets the login panel when the application starts for the first time or when it's called.
     */
    public void setLoginPanel(){
        mainFrame.setContentPane(loginForm.getLoginPanel());
        mainFrame.getContentPane().revalidate();
    }

    /**
     * Sets the register panel when called.
     */
    public void setRegisterPanel(){
        mainFrame.setContentPane(registerForm.getRegisterPanel());
        mainFrame.getContentPane().revalidate();
    }

    public void setProductPanel(){
        mainFrame.setContentPane(productForm.getProductPanel());
        mainFrame.getContentPane().revalidate();
    }

    public void setProfilePanel(){
        mainFrame.setContentPane(profileForm.getProfilePanel());
        mainFrame.getContentPane().revalidate();
    }

    /**
     * Returns the login panel/form.
     * @return login panel/form
     */
    public LoginForm getLoginForm(){
        return this.loginForm;
    }

    /**
     * Returns the product panel/form.
     * @return product panel/forum
     */
    public MarketplaceForm getProductForm(){
        return this.productForm;
    }

    public ProfileForm getProfileForm() {
        return profileForm;
    }

    /**
     * Sets the look and feel for the application.
     */
    public void setLookAndFeel(){
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }
}
