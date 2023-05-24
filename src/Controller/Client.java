package Controller;

import Model.*;
import View.MainForm;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;

/**
 * This is the class for the client.
 * It will connect to the server and start the GUI for the user.
 */
public class Client {
    /**
     * Declare variables
     */
    private Socket socket;
    private MainForm mainForm;
    private ObjectOutputStream oos;
    //user id is set on login
    private int userId;

    /**
     * Constructor
     */
    public Client(){
        try {
            //Connect to the server.
            connectToServer();
            //Start thread to read messages from the server.
            readMessagesFromServer();
            //Start main form and send a client object to it.
            mainForm = new MainForm(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Connects to the server.
     * @throws IOException
     */
    public void connectToServer() throws IOException {
        socket = new Socket("localhost", 8888);
        //Outputstream to send objects to the server.
        oos = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Sends a product object to the server.
     */
    public void sendProductToServer(String type, int price, String productionYear, String color, String condition) throws IOException {
        Product product = new Product(userId, type, price, productionYear, color, condition);
        oos.writeObject(product);
        oos.flush();
    }

    /**
     * Sends a user object to the server for registering.
     * @param username The username of the user.
     * @param password The password of the user.
     * @param dateOfBirth The date of birth of the user.
     * @param email The email of the user.
     * @throws IOException
     */
    public void sendUserToServerRegister(String username, String password, String dateOfBirth, String email, boolean registered) throws IOException {
        User user = new User(username, password, dateOfBirth, email, registered);
        oos.writeObject(user);
        oos.flush();
    }

    /**
     * Sends a user object to the server for logging in/ for marketplace access
     * @param username The username of the user.
     * @param password The password of the user.
     * @throws IOException
     */
    public void sendUserToServerLogin(String username, String password) throws IOException {
        User user = new User(username, password, true);
        oos.writeObject(user);
        oos.flush();
    }
    public void searchByDate(String start, String end) throws IOException {
        Request request = new Request(start, end, "searchByDate", userId);
        oos.writeObject(request);
        oos.flush();
    }

    public void accessMarketplace() throws IOException {
        String message = "marketplace";
        oos.writeObject(message);
        oos.flush();
    }

    public void accessOrderHistory() throws IOException {
        ServerRequest serverRequest = new ServerRequest("getOrderHistory",userId);
        oos.writeObject(serverRequest);
        oos.flush();
    }

    public void accessRequests() throws IOException{
        ServerRequest serverRequest = new ServerRequest("getRequests",userId);
        oos.writeObject(serverRequest);
        oos.flush();
    }

    public void sendRemoveFromCartToServer(int productId) throws IOException {
        Request request = new Request(productId, "removeFromCart", userId);
        oos.writeObject(request);
        oos.flush();

    }

    public void requestItemInCart(int productId) throws IOException {
        Request request = new Request(productId, "requestItemFromCart", userId);
        oos.writeObject(request);
        oos.flush();
    }

    public void sendRequestToServer(int productId) throws IOException {
        Request request = new Request(this.userId, productId);
        oos.writeObject(request);
        oos.flush();
    }

    /**
     * Sends the data from the accepted request to the server.
     * Acquires the data fromt the JTable shown in the GUI.
     *
     * @param productName The name of the product that was sold.
     * @param buyerId     The id of the user that bought the product.
     * @param productId
     */
    public void sendAcceptRequestToServer(String productName, int buyerId, int productId) throws IOException {
        Request request = new Request(productName, buyerId, productId, "acceptRequest", this.userId);
        oos.writeObject(request);
        oos.flush();
    }

    /**
     * Sends a decline request to the server.
     * @param productId The id of the product that was decliend.
     * @throws IOException
     */
    public void sendDeclineRequestToServer(int productId) throws IOException {
        Request request = new Request(productId, "declineRequest", this.userId);
        oos.writeObject(request);
        oos.flush();
    }

    /**
     * Sends the user_id of your user to receive information about your products and order history
     */
    public void sendUserIdToServerProfile(int userId) throws IOException {
        oos.writeObject(userId);
        oos.flush();
    }

    public void sendSearchByTypeToServer(String type) throws IOException {
        Request request = new Request(type, "searchByType", userId);
        oos.writeObject(request);
        oos.flush();
    }

    public void sendSearchByPriceToServer(String price) throws IOException {
        Request request = new Request(price, "searchByPrice", userId);
        oos.writeObject(request);
        oos.flush();
    }

    public void sendSearchByConditionToServer(String condition) throws IOException {
        Request request = new Request(condition, "searchByCondition", userId);
        oos.writeObject(request);
        oos.flush();
    }

    public void sendShowAllProductsToServer() throws IOException{
        Request request = new Request("","showAllProducts", userId);
        oos.writeObject(request);
        oos.flush();
    }

    public void sendCartRequestToServer() throws IOException {
        Request request = new Request(this.userId, "viewCart");
        oos.writeObject(request);
        oos.flush();
    }

    /**
     * Reads a message that was received from the server.
     */
    public void readMessagesFromServer() {
        new Thread(() -> {
            try {
                //Input stream to read objects sent from the server.
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                while (true) {
                    //Read the object.
                    Object object = ois.readObject();
                    //Check the type of the object.
                    if(object instanceof String){
                        String s = (String) object;
                        //Pass the object to a function to handle it.
                        handleStringMessagesFromServer(s);
                    }
                    //The userID sent from the server is Integer.
                    if(object instanceof Integer){
                        int i = (int) object;
                        //Pass the userId to the function to handle it.
                        handleUserIdFromServer(i);
                    }
                    //DefaultTableModel holds data from the database that will be added to a JTable.
                    if(object instanceof Hashtable<?,?>){
                        Hashtable<?,?> hashtable = (Hashtable<?, ?>) object;
                        handleHashtableFromServer((Hashtable<String, DefaultTableModel>) hashtable);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Handles the DefaultTableModel sent from the server.
     * @param hashtable Table model that holds data from the database.
     */
    public void handleHashtableFromServer(Hashtable<String, DefaultTableModel> hashtable) {
        if(hashtable.containsKey("Marketplace Products")){
            mainForm.getProductForm().createTableModel(hashtable.get("Marketplace Products"));
        }

        if(hashtable.containsKey("My products")){
            mainForm.getProfileForm().createTableModel(hashtable.get("My products"));
            mainForm.setProfilePanel();
            mainForm.getProfileForm().setTitle("My Inventory");

        }

        if(hashtable.containsKey("Order History")){
            mainForm.getProfileForm().createTableModel(hashtable.get("Order History"));
            mainForm.setProfilePanel();
            mainForm.getProfileForm().setTitle("Order History");
        }

        if(hashtable.containsKey("Buy Requests")){
            mainForm.getProfileForm().createTableModel(hashtable.get("Buy Requests"));
            mainForm.setProfilePanel();
            mainForm.getProfileForm().setTitle("Requests");
        }

        if(hashtable.containsKey("My cart")){
            mainForm.getProfileForm().createTableModel(hashtable.get("My cart"));
            mainForm.setProfilePanel();
            mainForm.getProfileForm().setTitle("Shopping Cart");
            mainForm.getProfileForm().cartButtons();

        }
        if(hashtable.containsKey("Orders")){
            mainForm.getProfileForm().createTableModel(hashtable.get("Orders"));
            mainForm.setProfilePanel();
            mainForm.getProfileForm().setTitle("Orders by date");
        }
    }


    /**
     * Handles messages from the server.
     * The function decides what to do based on the message.
     * @param message
     */
    public void handleStringMessagesFromServer(String message){
        switch(message){
            case "loginSuccess":
                mainForm.setProductPanel();
                break;
            case "loginFailed":
                mainForm.getLoginForm().failedToLogin();
                break;
        }
    }

    /**
     * Handles the userId that was sent from the server after the user logged in.
     * @param userId The users id.
     */
    public void handleUserIdFromServer(int userId){
        this.userId = userId;
    }

    /**
     * Returns the MainForm of the application.
     * @return mainForm
     */
    public MainForm getMainForm(){
        return this.mainForm;
    }

    public static void main(String[] args) {
        //Startar main fönstret.
        new Client();
    }
    public User getCurrentUser(){
        return new User("todo", "later", true); //I guess we need an instance variable for this?
    }

    public int getUserId() {
        return userId;
    }


}
