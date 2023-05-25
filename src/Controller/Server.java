package Controller;

import DataAccessLayer.ProductProcedures;
import DataAccessLayer.UserProcedures;
import DataAccessLayer.WishProcedures;
import Model.*;
import Model.ClientModel;

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
    private UserProcedures userProcedures;
    private ProductProcedures productProcedures;
    private WishProcedures wishProcedures;

    private ClientManager clientManager;


    /**
     * This function starts the server when it's called.
     * @throws IOException
     */
    public void startServer() throws IOException {
        serverSocket = new ServerSocket(8888);
        System.out.println("Server started on port 8080");
        userProcedures = new UserProcedures();
        productProcedures = new ProductProcedures();
        wishProcedures = new WishProcedures();
        clientManager = new ClientManager(wishProcedures);


        while (true) {
            //Accept a clients connection.
            clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());
            new Thread(() -> {
                try {
                    //Create output stream to send objects to the client.
                    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());

                    //Create an input stream to read objects sent from the client.
                    ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                    while(true){
                        //Read the object.
                        Object object = ois.readObject();
                        //Check the type of the object.
                        if(object instanceof Product){
                            //Add product to database here.
                            Product product = (Product) object;
                            handleProductFromClient(product, oos);
                        }
                        else if(object instanceof User){
                            //Log in or register user here.
                            User user = (User) object;
                            //Check if user is registered.
                            if(user.isRegistered()){
                                //Login user.
                                handleUserLoginFromClient(user, oos);
                            }
                            else {
                                //Register user.
                                handleUserRegisterFromClient(user, oos);
                            }
                        }
                        else if (object instanceof Request){
                            Request request = (Request) object;
                            if(request.getRequestType() == null){
                                handleAddCartFromClient(request);
                            }
                            else if(request.getRequestType().equals("declineRequest")){
                                handleDeclineBuyReqFromClient(request, oos);
                            }
                            else if(request.getRequestType().equals("acceptRequest")){
                                handleAcceptBuyReqFromClient(request, oos);
                            }
                            else if(request.getRequestType().equals("searchByType")){
                                sendSearchByTypeToClient(request, oos);
                            }
                            else if(request.getRequestType().equals("searchByPrice")){
                                sendSearchByPriceToClient(request, oos);
                            }
                            else if(request.getRequestType().equals("searchByCondition")){
                                sendSearchByConditionToClient(request, oos);
                            }
                            else if(request.getRequestType().equals("searchByDate")){
                                sendSearchByDateToClient(request, request.getUserId(), oos);
                            }
                            else if(request.getRequestType().equals("showAllProducts")){
                                getAllProductsFromDatabase(oos);
                            }
                            else if(request.getRequestType().equals("viewCart")){
                                sendCartItemsToClient(request.getUserId(), oos);
                            }
                            else if(request.getRequestType().equals("removeFromCart")){
                                if(productProcedures.removeFromCart(request.getUserId(), request.getProduct_id())) {
                                    sendCartItemsToClient(request.getUserId(), oos);
                                }
                            }
                            else if(request.getRequestType().equals("requestItemFromCart")){
                                productProcedures.requestItemFromCart(request.getUserId(), request.getProduct_id());
                            }

                        }
                        else if (object instanceof String){
                            if (object == "marketplace") {
                                getAllProductsFromDatabase(oos);
                            }
                        } else if(object instanceof Integer){
                            sendClientUsersProducts((int)object, oos);
                        }

                        else if (object instanceof Wish) {
                            System.out.println("Lägger till wish för user id: " +((Wish) object).getUserID());
                            addWishToDataBase((Wish) object,oos);

                        }
                        else if(object instanceof ServerRequest){
                            ServerRequest serverRequest = (ServerRequest) object;
                            if(serverRequest.getRequestType().equals("getOrderHistory")){
                                sendClientOrderHistory(serverRequest.getUserID(), oos);
                            }

                            if(serverRequest.getRequestType().equals("getRequests")){
                                sendClientRequests(serverRequest.getUserID(), oos);
                            }
                            if(serverRequest.getRequestType().equals("accessWishList")){
                                System.out.println("Server sending wishlist to client");
                                sendWishListToClient(serverRequest.getUserID(),oos);
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


    private void sendCartItemsToClient(int userId, ObjectOutputStream oos) throws SQLException, IOException {
        oos.writeObject(productProcedures.getUserShoppingcart(userId));
        oos.flush();
    }

    private void sendClientUsersProducts(int userId, ObjectOutputStream oos) throws IOException, SQLException {
        oos.writeObject(productProcedures.getUsersProducts(userId));
        oos.flush();
    }

    private void addWishToDataBase(Wish wish,ObjectOutputStream oos) throws IOException, SQLException {
        oos.writeObject(wishProcedures.addWishToDataBase(wish));
        clientManager.refreshWishlist(wish.getUserID());
        oos.flush();
    }
    private void sendWishListToClient(int userID,ObjectOutputStream oos) throws SQLException, IOException {
        oos.writeObject(wishProcedures.getUserWishlist(userID));
        System.out.println("Contains message server: " + productProcedures.
                getUsersProducts(userID).containsKey("My Wishlist"));
        oos.flush();

    }

    private void sendClientOrderHistory(int userId, ObjectOutputStream oos) throws IOException, SQLException {
        oos.writeObject(productProcedures.getOrderHistory(userId));
        oos.flush();
    }

    private void sendClientRequests(int userId, ObjectOutputStream oos) throws IOException, SQLException {
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
    public void sendSearchByTypeToClient(Request request, ObjectOutputStream oos) throws IOException {
        Hashtable results = productProcedures.getProductsByTitle(request.getParam());
        oos.writeObject(results);
        oos.flush();
    }

    /**
     * Sends the products by price to the client.
     * @param request Includes data that was sent from the client.
     */
    public void sendSearchByPriceToClient(Request request, ObjectOutputStream oos) throws IOException {
        Hashtable results = productProcedures.getProductsByPrice(request.getParam());
        oos.writeObject(results);
        oos.flush();
    }
    public void sendSearchByDateToClient(Request request, int userID, ObjectOutputStream oos) throws IOException {
        String startDate, endDate;
        String temp = request.getParam();
        startDate = temp.substring(0, temp.indexOf("|"));
        endDate = temp.substring(temp.indexOf("|") + 1, temp.length());
        Hashtable results = productProcedures.searchByDate(startDate, endDate, userID);
        sendHashtableToClient(results, oos);
    }

    /**
     * Sends the products by condition to the client.
     * @param request Includes data that was sent from the client.
     */
    public void sendSearchByConditionToClient(Request request, ObjectOutputStream oos) throws IOException {
        Hashtable results = productProcedures.getProductsByCondition(request.getParam());
        oos.writeObject(results);
        oos.flush();
    }

    /**
     * Handles the buy request from the client.
     * @param request The request object holding data
     */
    public void handleAcceptBuyReqFromClient(Request request, ObjectOutputStream oos) throws SQLException, IOException {
        productProcedures.purchaseProd(request.getProduct_id(), request.getProductName(), request.getBuyer_id());
        //Update the client with the new request table from the DB.
        sendClientRequests(request.getUserId(), oos);
        //Update the client with the new product table from the DB after deleting the accepted product.
        getAllProductsFromDatabase(oos);
    }

    /**
     * This function handles the decline request from the client.
     * @param request The request object holding data
     */
    public void handleDeclineBuyReqFromClient(Request request, ObjectOutputStream oos) throws SQLException, IOException {
        productProcedures.declinePurchaseRequest(request.getProduct_id());
        sendClientRequests(request.getUserId(), oos);
    }

    /**
     * Handles messages from the client.
     * The function decides what to do based on the message.
     */
    public void handleProductFromClient(Product product, ObjectOutputStream oos) throws IOException {
        productProcedures.registerProdForSale(product);
        //Get all the products from the database to update the GUI.
        getAllProductsFromDatabase(oos);
    }

    /**
     * Handles the user registration.
     * @param user The user object that was sent from the client.
     */
    public void handleUserRegisterFromClient(User user, ObjectOutputStream oos) throws IOException {
        sendStringMessageToClient("The user has been registered.", oos);
        userProcedures.createUser(user.getUsername(), user.getPassword(), user.getDateOfBirth(), user.getEmail());
    }

    /**
     * Handles the user login.
     * @param user The user object that was sent from the client.
     */
    public void handleUserLoginFromClient(User user, ObjectOutputStream oos) throws IOException {
        int userId = userProcedures.signInUser(user.getUsername(), user.getPassword());
        //If higher than 0, login was successfull.
        if(userId > 0){
            //add client to hashmap
            clientManager.addClient(new ClientModel(userId,oos));
            sendStringMessageToClient("loginSuccess", oos);
            //Send the user id that was returned from the database to the client.
            sendUserIdToClient(userId, oos);
            //Get all the products from the database and send to the client to show on the GUI.
            getAllProductsFromDatabase(oos);
        }
        else {
            sendStringMessageToClient("loginFailed", oos);
        }
    }

    /**
     * This function gets all the products from the database.
     * @throws IOException
     */
    public void getAllProductsFromDatabase(ObjectOutputStream oos) throws IOException {
        Hashtable hashtable = productProcedures.getAllProducts();
        //Send the DefaultTableModel holding the data to the client.
        sendHashtableToClient(hashtable, oos);
    }

    /**
     * This function sends a hashtable to the client from the server.
     * @param hashtable The table model with the data that will be displayed in a JTable plus its type.
     * @throws IOException
     */
    public void sendHashtableToClient(Hashtable<String, DefaultTableModel> hashtable, ObjectOutputStream oos) throws IOException {
        oos.writeObject(hashtable);
        oos.flush();
    }

    /**
     * This function sends a String message to the client from the server.
     * @param message
     * @throws IOException
     */
    public void sendStringMessageToClient(String message, ObjectOutputStream oos) throws IOException {
        oos.writeObject(message);
        oos.flush();
    }

    /**
     * Sends the userid that was returned from the database after signing in to the client.
     * @param userId The users id.
     * @throws IOException
     */
    public void sendUserIdToClient(int userId, ObjectOutputStream oos) throws IOException {
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
