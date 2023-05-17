package Model;

import java.io.Serializable;

public class ServerRequest implements Serializable {
    private String requestType;
    private int userID;
    private static final long serialVersionUID = 1L;


    public ServerRequest(String requestType,int userID){
        this.requestType=requestType;
        this.userID=userID;
    }


    public String getRequestType() {
        return requestType;
    }

    public int getUserID() {
        return userID;
    }
}
