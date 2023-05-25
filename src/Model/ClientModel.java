package Model;

import javax.swing.table.DefaultTableModel;
import java.io.ObjectOutputStream;

public class ClientModel {
    private int userId;
    private ObjectOutputStream oos;
    private Hashtable<String, DefaultTableModel> wishlist;


    public ClientModel(int userId, ObjectOutputStream oos){
        this.userId=userId;
        this.oos=oos;

    }

    public int getUserId() {
        return userId;
    }

    public void setWishlist(Hashtable<String, DefaultTableModel> wishlist) {
        this.wishlist = wishlist;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }
}
