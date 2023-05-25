package Model;

import Controller.ClientManager;

import java.io.ObjectOutputStream;

public class Client {
    private int userId;
    private ObjectOutputStream oos;

    public Client(int userId, ObjectOutputStream oos){
        this.userId=userId;
        this.oos=oos;

    }

    public int getUserId() {
        return userId;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }
}
