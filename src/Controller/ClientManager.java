package Controller;

import DataAccessLayer.WishProcedures;
import Model.ClientModel;

import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;


public class ClientManager {
    private Hashtable<Integer, ArrayList<String>> clientWishlists;
    private ConcurrentHashMap<Integer, ClientModel> clients;
    private Hashtable<Integer, Queue<String>> clientNotifications;

    private WishProcedures wishProcedures;

    public ClientManager(WishProcedures wishProcedures) {
        clientWishlists = new Hashtable<>();
        clients = new ConcurrentHashMap<>();
        clientNotifications = new Hashtable<>();
        this.wishProcedures=wishProcedures;
    }

    public void removeClient(int userId) {
        // Remove the client from the map
        this.clients.remove(userId);
    }

    public void addClient(ClientModel client) {
        this.clients.put(client.getUserId(),client);
        refreshWishlist(client.getUserId());
        System.out.println("Client added id: "+this.clients.get(client.getUserId()).getUserId());

    }

    public void refreshWishlist(int userId) {
        try {
            Hashtable wishlist = wishProcedures.getUserWishlist(userId);
            ClientModel client = clients.get(userId);
            if (client != null) {
                client.setWishlist(wishlist);
                System.out.println("Refreshwishlist called");
            }
        } catch (SQLException e) {
            System.err.println("Error refreshing wishlist for user " + userId);
            e.printStackTrace();  // Or log the error using your logging framework
            // Add your error handling logic here, for example retry the operation
        }
    }













}
