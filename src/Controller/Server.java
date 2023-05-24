package Controller;

import DataAccessLayer.ProductProcedures;
import DataAccessLayer.UserProcedures;
import Model.Product;
import Model.Request;
import Model.ServerRequest;
import Model.User;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * This is the server class.
 */
public class Server {

    /**
     * Declare variables.
     */
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream oos;
    private UserProcedures userProcedures;
    private ProductProcedures productProcedures;

    /**
     * This function starts the server when it's called.
     * @throws IOException
     */
    public void startServer() throws IOException {
        serverSocket = new ServerSocket(8888);
        System.out.println("Server started on port 8080");
        userProcedures = new UserProcedures();
        productProcedures = new ProductProcedures();

        while (true) {
            //Accept a clients connection.
            clientSocket = serverSocket.accept();
            //Create output stream to send objects to the client.
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("Client connected: " + clientSocket.getInetAddress());
            new Thread(() -> {
                try {
                    //Create an input stream to read objects sent from the client.
                    ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                    while(true){
                        //Read the object.
                        Object object = ois.readObject();
                        //Check the type of the object.
                        if(object instanceof Product){
                            //Add product to database here.
                            Product product = (Product) object;
                            handleProductFromClient(product);
                        }
                        else if(object instanceof User){
                            //Log in or register user here.
                            User user = (User) object;
                            //Check if user is registered.
                            if(user.isRegistered()){
                                //Login user.
                                handleUserLoginFromClient(user);
                            }
                            else {
                                //Register user.
                                handleUserRegisterFromClient(user);
                            }
                        }
                        else if (object instanceof Request){
                            Request request = (Request) object;
                            if(request.getRequestType() == null){
                                handleAddCartFromClient(request);
                            }
                            else if(request.getRequestType().equals("declineRequest")){
                                handleDeclineBuyReqFromClient(request);
                            }
                            else if(request.getRequestType().equals("acceptRequest")){
                                handleAcceptBuyReqFromClient(request);
                            }
                            else if(request.getRequestType().equals("searchByType")){
                                sendSearchByTypeToClient(request);
                            }
                            else if(request.getRequestType().equals("searchByPrice")){
                                sendSearchByPriceToClient(request);
                            }
                            else if(request.getRequestType().equals("searchByCondition")){
                                sendSearchByConditionToClient(request);
                            }
                            else if(request.getRequestType().equals("searchByDate")){
                                sendSearchByDateToClient(request, request.getUserId());
                            }
                            else if(request.getRequestType().equals("showAllProducts")){
                                getAllProductsFromDatabase();
                            }
                            else if(request.getRequestType().equals("viewCart")){
                                sendCartItemsToClient(request.getUserId());
                            }
                            else if(request.getRequestType().equals("removeFromCart")){
                                if(productProcedures.removeFromCart(request.getUserId(), request.getProduct_id())) {
                                    sendCartItemsToClient(request.getUserId());
                                }
                            }
                            else if(request.getRequestType().equals("requestItemFromCart")){
                                productProcedures.requestItemFromCart(request.getUserId(), request.getProduct_id());
                            }
                        }
                        else if (object instanceof String){
                            if (object == "marketplace") {
                                getAllProductsFromDatabase();
                            }
                        } else if(object instanceof Integer){
                            sendClientUsersProducts((int)object);
                        }
                        else if(object instanceof ServerRequest){
                            ServerRequest serverRequest = (ServerRequest) object;
                            if(serverRequest.getRequestType().equals("getOrderHistory")){
                                sendClientOrderHistory(serverRequest.getUserID());
                            }

                            if(serverRequest.getRequestType().equals("getRequests")){
                                sendClientRequests(serverRequest.getUserID());
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    private void sendCartItemsToClient(int userId) throws SQLException, IOException {
        oos.writeObject(productProcedures.getUserShoppingcart(userId));
        oos.flush();
    }

    private void sendClientUsersProducts(int userId) throws IOException, SQLException {
        oos.writeObject(productProcedures.getUsersProducts(userId));
        oos.flush();
    }

    private void sendClientOrderHistory(int userId) throws IOException, SQLException {
        oos.writeObject(productProcedures.getOrderHistory(userId));
        oos.flush();
    }

    private void sendClientRequests(int userId) throws IOException, SQLException {
        oos.writeObject(productProcedures.getBuyReqs(userId));
        oos.flush();
    }

    /**
     * Handles buy requests from the client
     * @param request
     */
    private boolean handleAddCartFromClient(Request request) {
        return productProcedures.addToShoppingCart(request.getBuyer_id(), request.getProduct_id());
    }

    /**
     * Sends the products by type to the client.
     * @param request Includes data that was sent from the client.
     */
    public void sendSearchByTypeToClient(Request request) throws IOException {
        Hashtable results = productProcedures.getProductsByTitle(request.getParam());
        oos.writeObject(results);
        oos.flush();
    }

    /**
     * Sends the products by price to the client.
     * @param request Includes data that was sent from the client.
     */
    public void sendSearchByPriceToClient(Request request) throws IOException {
        Hashtable results = productProcedures.getProductsByPrice(request.getParam());
        oos.writeObject(results);
        oos.flush();
    }
    public void sendSearchByDateToClient(Request request, int userID) throws IOException {
        String startDate, endDate;
        String temp = request.getParam();
        startDate = temp.substring(0, temp.indexOf("|"));
        endDate = temp.substring(temp.indexOf("|") + 1, temp.length());
        Hashtable results = productProcedures.searchByDate(startDate, endDate, userID);
        sendHashtableToClient(results);
    }

    /**
     * Sends the products by condition to the client.
     * @param request Includes data that was sent from the client.
     */
    public void sendSearchByConditionToClient(Request request) throws IOException {
        Hashtable results = productProcedures.getProductsByCondition(request.getParam());
        oos.writeObject(results);
        oos.flush();
    }

    /**
     * Handles the buy request from the client.
     * @param request The request object holding data
     */
    public void handleAcceptBuyReqFromClient(Request request) throws SQLException, IOException {
        productProcedures.purchaseProd(request.getProduct_id(), request.getProductName(), request.getBuyer_id());
        //Update the client with the new request table from the DB.
        sendClientRequests(request.getUserId());
        //Update the client with the new product table from the DB after deleting the accepted product.
        getAllProductsFromDatabase();
    }

    /**
     * This function handles the decline request from the client.
     * @param request The request object holding data
     */
    public void handleDeclineBuyReqFromClient(Request request) throws SQLException, IOException {
        productProcedures.declinePurchaseRequest(request.getProduct_id());
        sendClientRequests(request.getUserId());
    }

    /**
     * Handles messages from the client.
     * The function decides what to do based on the message.
     */
    public void handleProductFromClient(Product product) throws IOException {
        productProcedures.registerProdForSale(product);
        //Get all the products from the database to update the GUI.
        getAllProductsFromDatabase();
    }

    /**
     * Handles the user registration.
     * @param user The user object that was sent from the client.
     */
    public void handleUserRegisterFromClient(User user) throws IOException {
        sendStringMessageToClient("The user has been registered.");
        userProcedures.createUser(user.getUsername(), user.getPassword(), user.getDateOfBirth(), user.getEmail());
    }

    /**
     * Handles the user login.
     * @param user The user object that was sent from the client.
     */
    public void handleUserLoginFromClient(User user) throws IOException {
        int userId = userProcedures.signInUser(user.getUsername(), user.getPassword());
        //If higher than 0, login was successfull.
        if(userId > 0){
            sendStringMessageToClient("loginSuccess");
            //Send the user id that was returned from the database to the client.
            sendUserIdToClient(userId);
            //Get all the products from the database and send to the client to show on the GUI.
            getAllProductsFromDatabase();
        }
        else {
            sendStringMessageToClient("loginFailed");
        }
    }

    /**
     * This function gets all the products from the database.
     * @throws IOException
     */
    public void getAllProductsFromDatabase() throws IOException {
        Hashtable hashtable = productProcedures.getAllProducts();
        //Send the DefaultTableModel holding the data to the client.
        sendHashtableToClient(hashtable);
    }

    /**
     * This function sends a hashtable to the client from the server.
     * @param hashtable The table model with the data that will be displayed in a JTable plus its type.
     * @throws IOException
     */
    public void sendHashtableToClient(Hashtable<String, DefaultTableModel> hashtable) throws IOException {
        oos.writeObject(hashtable);
        oos.flush();
    }

    /**
     * This function sends a String message to the client from the server.
     * @param message
     * @throws IOException
     */
    public void sendStringMessageToClient(String message) throws IOException {
        oos.writeObject(message);
        oos.flush();
    }

    /**
     * Sends the userid that was returned from the database after signing in to the client.
     * @param userId The users id.
     * @throws IOException
     */
    public void sendUserIdToClient(int userId) throws IOException {
        oos.writeObject(userId);
        oos.flush();
    }

    /**
     * Main function to start the server.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Server s = new Server();
        s.startServer();
    }
}
