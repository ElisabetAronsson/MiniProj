package Model;

import java.io.Serializable;
import java.sql.Date;

/**
 * This class represents a product.
 */
public class Product implements Serializable {
    private int user_id;
    private String title;
    private double price;
    private String year_of_production;
    private String color;
    private String condition;

    public Product(int user_id, String title, double price, String year_of_production, String color, String condition) {
        this.user_id = user_id;
        this.title = title;
        this.price = price;
        this.year_of_production = year_of_production;
        this.color = color;
        this.condition = condition;
    }

    public int getUser_id() {
        return user_id;
    }
    public String getTitle() {
        return title;
    }
    public String getYear_of_production() {
        return year_of_production;
    }
    public String getColor() {
        return color;
    }
    public String getCondition() {
        return condition;
    }
    public double getPrice() {
        return price;
    }
}
