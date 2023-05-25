package Model;

import java.io.Serializable;

public class Wish implements Serializable {
    private int userID;
    private String productName;

    public Wish(int userID,String productName){
        this.userID=userID;
        this.productName=productName;

    }

    public int getUserID() {
        return userID;
    }

    public String getProductName() {
        return productName;
    }
}