package Controller;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Queue;

public class ClientManager {

    private Hashtable<Integer, ArrayList<String>> clientWishlists;
    private Hashtable<Integer, ObjectOutputStream> clientStreams;
    private Hashtable<Integer, Queue<String>> clientNotifications;

    public ClientManager() {
        clientWishlists = new Hashtable<>();
        clientStreams = new Hashtable<>();
        clientNotifications = new Hashtable<>();
    }



}
