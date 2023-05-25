package Model;

import javax.swing.table.DefaultTableModel;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

public class ClientModel {
    private int userId;
    private ObjectOutputStream oos;
    private Hashtable wishlist;


    public ClientModel(int userId, ObjectOutputStream oos){
        this.userId=userId;
        this.oos=oos;

    }

    public int getUserId() {
        return userId;
    }

    public void setWishlist(Hashtable wishlist) {
        this.wishlist = wishlist;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }
}
