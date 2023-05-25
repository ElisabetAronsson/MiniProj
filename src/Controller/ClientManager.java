package Controller;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {

    private Hashtable<Integer, ArrayList<String>> clientWishlists;
    private ConcurrentHashMap<Integer, Client> clients;

    private Hashtable<Integer, Queue<String>> clientNotifications;

    public ClientManager() {
        clientWishlists = new Hashtable<>();
        clients = new ConcurrentHashMap<>();
        clientNotifications = new Hashtable<>();
    }

    public void removeClient(int userId) {
        // Remove the client from the map
        this.clients.remove(userId);
    }

    public void addClient(Client client){

    }







}
