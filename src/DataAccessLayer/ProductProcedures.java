package DataAccessLayer;

import Model.Product;
import Model.User;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class ProductProcedures {

    public static void main(String[] args) throws SQLException {
        ProductProcedures pc = new ProductProcedures();
        pc.getOrderHistory(11);
    }

    /**
     * Get all the products that are available for sale from the database.
     *
     * @return Products for sale
     */
    public Hashtable getAllProducts() {
        DatabaseConnection dc = new DatabaseConnection();
        try (CallableStatement statement = dc.getConnection().prepareCall("{ call get_all_products() }")) {
            //Create the table model
            DefaultTableModel tableModel = new DefaultTableModel();
            String[] columnNames = {"ProductID", "SellerID", "Type", "Price", "Production Year", "Color", "Condition"};
            //Add the column names to the table model
            for (int i = 0; i < columnNames.length; i++) {
                tableModel.addColumn(columnNames[i]);
            }
            int counter = 0;

            statement.executeQuery();
            ResultSet res = statement.getResultSet();
            while (res.next()) {
                //Add the data to the table model
                tableModel.insertRow(counter, new Object[]{res.getString(1), res.getString(2), res.getString(3),
                        res.getString(4), res.getString(5), res.getString(6), res.getString(7)});
                counter++;
            }
            //Return the table model with the data
            Hashtable<String, DefaultTableModel> hashtable = new Hashtable<>();
            hashtable.put("Marketplace Products", tableModel);
            return hashtable;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns all the products from the database based on their title.
     * @param title The title of the product.
     * @return Hashtable to update the GUI.
     */
    public Hashtable getProductsByTitle(String title){
        DatabaseConnection dc = new DatabaseConnection();
        try (CallableStatement statement = dc.getConnection().prepareCall("{ call get_products_by_type(?) }")) {
            //Create the table model
            DefaultTableModel tableModel = new DefaultTableModel();
            String[] columnNames = {"ProductID", "SellerID", "Type", "Price", "Production Year", "Color", "Condition"};
            //Add the column names to the table model
            for (int i = 0; i < columnNames.length; i++) {
                tableModel.addColumn(columnNames[i]);
            }
            int counter = 0;

            statement.setString(1, title);
            statement.executeQuery();
            ResultSet res = statement.getResultSet();
            while (res.next()) {
                //Add the data to the table model
                tableModel.insertRow(counter, new Object[]{res.getString(1), res.getString(2), res.getString(3),
                        res.getString(4), res.getString(5), res.getString(6), res.getString(7)});
                counter++;
            }
            //Return the table model with the data
            Hashtable<String, DefaultTableModel> hashtable = new Hashtable<>();
            hashtable.put("Marketplace Products", tableModel);
            return hashtable;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns all the products from the database based on their title.
     * @param price The price of the product.
     * @return Hashtable to update the GUI.
     */
    public Hashtable getProductsByPrice(String price){
        DatabaseConnection dc = new DatabaseConnection();
        try (CallableStatement statement = dc.getConnection().prepareCall("{ call get_products_by_price(?) }")) {
            //Create the table model
            DefaultTableModel tableModel = new DefaultTableModel();
            String[] columnNames = {"ProductID", "SellerID", "Type", "Price", "Production Year", "Color", "Condition"};
            //Add the column names to the table model
            for (int i = 0; i < columnNames.length; i++) {
                tableModel.addColumn(columnNames[i]);
            }
            int counter = 0;

            statement.setInt(1, Integer.parseInt(price));
            statement.executeQuery();
            ResultSet res = statement.getResultSet();
            while (res.next()) {
                //Add the data to the table model
                tableModel.insertRow(counter, new Object[]{res.getString(1), res.getString(2), res.getString(3),
                        res.getString(4), res.getString(5), res.getString(6), res.getString(7)});
                counter++;
            }
            //Return the table model with the data
            Hashtable<String, DefaultTableModel> hashtable = new Hashtable<>();
            hashtable.put("Marketplace Products", tableModel);
            return hashtable;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns all the products from the database based on their title.
     * @param condition The condition of the product.
     * @return Hashtable to update the GUI.
     */
    public Hashtable getProductsByCondition(String condition){
        DatabaseConnection dc = new DatabaseConnection();
        try (CallableStatement statement = dc.getConnection().prepareCall("{ call get_products_by_condition(?) }")) {
            //Create the table model
            DefaultTableModel tableModel = new DefaultTableModel();
            String[] columnNames = {"ProductID", "SellerID", "Type", "Price", "Production Year", "Color", "Condition"};
            //Add the column names to the table model
            for (int i = 0; i < columnNames.length; i++) {
                tableModel.addColumn(columnNames[i]);
            }
            int counter = 0;

            statement.setString(1, condition);
            statement.executeQuery();
            ResultSet res = statement.getResultSet();
            while (res.next()) {
                //Add the data to the table model
                tableModel.insertRow(counter, new Object[]{res.getString(1), res.getString(2), res.getString(3),
                        res.getString(4), res.getString(5), res.getString(6), res.getString(7)});
                counter++;
            }
            //Return the table model with the data
            Hashtable<String, DefaultTableModel> hashtable = new Hashtable<>();
            hashtable.put("Marketplace Products", tableModel);
            return hashtable;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a product for sale.
     *
     * @param product The product to add for sale.
     */
    public int registerProdForSale(Product product) {
        DatabaseConnection dc = new DatabaseConnection();
        try (CallableStatement statement = dc.getConnection().prepareCall("{ ? = call add_product(?, ?, ?, ?, ?, ?) }")) {
            statement.registerOutParameter(1, Types.INTEGER);
            statement.setInt(2, product.getUser_id());
            statement.setString(3, product.getTitle());
            statement.setInt(4, (int) product.getPrice());
            statement.setDate(5, Date.valueOf(product.getYear_of_production()));
            statement.setString(6, product.getColor());
            statement.setString(7, product.getCondition());
            statement.execute();
            int result = statement.getInt(1);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tries to purchase a product when the sale is accepted from the seller
     *
     * @param product_id   A unique ID of the product
     * @param product_name A name of the product
     * @param buyer_id     The buyers unique ID
     * @return True if no exceptions occurs, else false
     */
    public boolean purchaseProd(int product_id, String product_name, int buyer_id) {
        DatabaseConnection dc = new DatabaseConnection();
        try {
            CallableStatement statement = dc.getConnection().prepareCall("{CALL purchase_prod (?, ?, ?)}");

            //Remove the product from the product table
            statement.setInt(1, product_id);
            statement.setString(2, product_name);
            statement.setInt(3, buyer_id);

            statement.execute();

            return true;    //If all goes well, return true
        } catch (Exception e) {
            e.printStackTrace();
            return false;   // Return false if any exception occurs
        }
    }

    /**
     * This function deletes a request from the request table.
     * @param product_id The id of the product being deleted from the request table.
     * @return true or false depending on success
     */
    public boolean declinePurchaseRequest(int product_id){
        DatabaseConnection dc = new DatabaseConnection();
        try {
            CallableStatement statement = dc.getConnection().prepareCall("{CALL decline_buy_request (?)}");

            //Remove the product from the request table
            statement.setInt(1, product_id);

            statement.execute();

            return true;    //If all goes well, return true
        } catch (Exception e) {
            e.printStackTrace();
            return false;   // Return false if any exception occurs
        }
    }


    public boolean buyReq(int user_id, int product_id) { // Måste uppdatera ägaren av produkten att en req finns
        DatabaseConnection dc = new DatabaseConnection();
        try (CallableStatement statement = dc.getConnection().prepareCall("{ ? = call buyreq(?,?) }")) {
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.setInt(2, user_id);
            statement.setInt(3, product_id);
            statement.execute();
            boolean result = statement.getBoolean(1);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(User user) {
        return false;
    }

    public boolean login() {
        return false;
    }

   public Hashtable getOrderHistory(int user_id) throws SQLException{
       List<Object> list = new ArrayList<>();
       DatabaseConnection databaseConnection = new DatabaseConnection();
       CallableStatement statement = databaseConnection.getConnection().prepareCall("SELECT * FROM get_order_history(?)");
       statement.setInt(1, user_id);

       DefaultTableModel tableModel = new DefaultTableModel();
       String[] columnNames = {"OrderID", "Title", "BuyerID", "Date of transaction"};
       //Add the column names to the table model
       for (int i = 0; i < columnNames.length; i++) {
           tableModel.addColumn(columnNames[i]);
       }
       int counter = 0;

       statement.executeQuery();
       ResultSet res = statement.getResultSet();
       while (res.next()) {
           //Add the data to the table model
           tableModel.insertRow(counter, new Object[]{res.getString(1), res.getString(2), res.getString(3),
                   res.getString(4), });
           counter++;
       }

       Hashtable<String, DefaultTableModel> hashtable = new Hashtable();
       hashtable.put("Order History", tableModel);

       //Printing out test
       for (int i = 0; i < tableModel.getRowCount(); i++) {
           for (int j = 0; j < tableModel.getColumnCount(); j++) {
               System.out.print(tableModel.getValueAt(i, j) + " ");
           }
           System.out.println();
       }
        return hashtable;
    }


    /**
     * Fetches any product that a buyer wishes to buy.
     *
     * @param user_id The sellers unique ID
     * @return A list of the products currently pending, seller will then need to accept or decline the sale.
     */
    public Hashtable getBuyReqs(int user_id) throws SQLException {
        List<Object> list = new ArrayList<>();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        CallableStatement statement = databaseConnection.getConnection().prepareCall("SELECT * FROM getbuyreqs(?)");
        statement.setInt(1, user_id);

        DefaultTableModel tableModel = new DefaultTableModel();
        /**
         * Här är det fel värden!
         */
        String[] columnNames = {"ProductID", "Buyer ID","Title", "Price","Year of production" ,"Color","Condition"};
        //Add the column names to the table model
        for (int i = 0; i < columnNames.length; i++) {
            tableModel.addColumn(columnNames[i]);
        }
        int counter = 0;

        statement.executeQuery();
        ResultSet res = statement.getResultSet();
        while (res.next()) {
            tableModel.insertRow(counter, new Object[]{res.getString(1), res.getString(2), res.getString(3),
                    res.getString(4), res.getString(5), res.getString(6), res.getString(7)});
            counter++;
            //Add the data to the table model

        }

        Hashtable<String, DefaultTableModel> hashtable = new Hashtable();
        hashtable.put("Buy Requests", tableModel);

        return hashtable;
    }

    public Hashtable getUsersProducts(int user_id) throws SQLException {
        List<Object> list = new ArrayList<>();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        CallableStatement statement = databaseConnection.getConnection().prepareCall("SELECT * FROM get_my_products(?)");
        statement.setInt(1, user_id);

        DefaultTableModel tableModel = new DefaultTableModel();
        String[] columnNames = {"ProductID", "SellerID", "Type", "Price", "Production Year", "Color", "Condition"};
        //Add the column names to the table model
        for (int i = 0; i < columnNames.length; i++) {
            tableModel.addColumn(columnNames[i]);
        }
        int counter = 0;

        statement.executeQuery();
        ResultSet res = statement.getResultSet();
        while (res.next()) {
            //Add the data to the table model
            tableModel.insertRow(counter, new Object[]{res.getString(1), res.getString(2), res.getString(3),
                    res.getString(4), res.getString(5), res.getString(6), res.getString(7)});
            counter++;
        }
        Hashtable<String, DefaultTableModel> hashtable = new Hashtable();
        hashtable.put("My products", tableModel);
        return hashtable;
    }

    public boolean registerNewProd(Product product) { //Skicka ut till alla online users att products är uppdaterad
        //Ny rad i products
        return false;
    }

    public List<Object> getPurchasedProducts(Date earliestDate) { //Från detta datumet och framåt
        //Hämtar listan med alla produkter som man köpt från och med earliestDate
        // om earliestDate är null så hämtas alla
        return null;
    }

    public boolean addToShoppingCart(int user_id, int product_id) {
        DatabaseConnection dc = new DatabaseConnection();
        try (CallableStatement statement = dc.getConnection().prepareCall("{ ? = call addToCart(?,?) }")) {
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.setInt(2, user_id);
            statement.setInt(3, product_id);
            statement.execute();
            boolean result = statement.getBoolean(1);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Object getUserShoppingcart(int user_id) throws SQLException {
        List<Object> list = new ArrayList<>();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        CallableStatement statement = databaseConnection.getConnection().prepareCall("SELECT * FROM get_my_cart(?)");
        statement.setInt(1, user_id);

        DefaultTableModel tableModel = new DefaultTableModel();
        String[] columnNames = {"Product_id", "Seller_id", "Type", "Price", "Year_Of_Production", "Color", "Condition"};
        //Add the column names to the table model
        for (int i = 0; i < columnNames.length; i++) {
            tableModel.addColumn(columnNames[i]);
        }
        int counter = 0;

        statement.executeQuery();
        ResultSet res = statement.getResultSet();
        while (res.next()) {
            //Add the data to the table model
            tableModel.insertRow(counter, new Object[]{res.getString(1), res.getString(2), res.getString(3),
                    res.getString(4), res.getString(5), res.getString(6), res.getString(7)});
            counter++;
        }
        Hashtable<String, DefaultTableModel> hashtable = new Hashtable();
        hashtable.put("My cart", tableModel);
        return hashtable;
    }
}
