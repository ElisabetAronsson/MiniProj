package Model;

import Controller.Server;

import java.io.Serializable;

public class Request implements Serializable {
    private int buyer_id;
    private int product_id;
    private String requestType;
    private String productName;

    private int userId;

    public Request(int buyer_id, int product_id) {
        this.buyer_id = buyer_id;
        this.product_id = product_id;
    }

    /**
     * This constructor is used create an object for accepting a buy request.
     * @param buyer_id The id of the buyer.
     * @param productName The name of the product being sold.
     * @param requestType What the type of the request is.
     */
    public Request(String productName, int buyer_id, int product_id, String requestType, int userId) {
        this.buyer_id = buyer_id;
        this.productName = productName;
        this.product_id = product_id;
        this.requestType = requestType;
        this.userId = userId;
    }

    public int getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getRequestType(){
        return this.requestType;
    }

    public String getProductName(){
        return this.productName;
    }

    public int getUserId() {
        return this.userId;
    }
}
