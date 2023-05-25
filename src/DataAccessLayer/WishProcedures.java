package DataAccessLayer;
import Model.Product;
import Model.User;
import Model.Wish;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
public class WishProcedures
{

    public static void main(String[] args) throws SQLException {
        WishProcedures wishProcedures = new WishProcedures();
        // wishProcedures.getUserWishlist(11);

        Wish newWish = new Wish(11, "New Product");
        wishProcedures.addWishToDataBase(newWish);

    }

    public Hashtable getUserWishlist(int user_id) throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        CallableStatement statement = databaseConnection.getConnection().prepareCall("SELECT * FROM get_user_wishlist(?)");
        statement.setInt(1, user_id);

        DefaultTableModel tableModel = new DefaultTableModel();
        String[] columnNames = {"UserID", "Product Name"};
        // Add the column names to the table model
        for (String columnName : columnNames) {
            tableModel.addColumn(columnName);
        }
        int counter = 0;

        statement.executeQuery();
        ResultSet res = statement.getResultSet();
        while (res.next()) {
            // Add the data to the table model
            tableModel.insertRow(counter, new Object[]{res.getString(1), res.getString(2)});
            counter++;
        }

        Hashtable<String, DefaultTableModel> hashtable = new Hashtable<>();
        hashtable.put("My Wishlist", tableModel);

        int rowCount = tableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                System.out.print(tableModel.getValueAt(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println("The function works");
        System.out.println("DB method contains My Wishlist: " + hashtable.containsKey("My Wishlist"));
        return hashtable;
    }


    public Hashtable<String, DefaultTableModel> addWishToDataBase(Wish wish) throws SQLException {
        Hashtable<String, DefaultTableModel> hashtable = new Hashtable<>();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        CallableStatement statement = databaseConnection.getConnection().prepareCall("SELECT * FROM add_wish_and_get_wishlist(?,?)");

        // Setting input parameters
        statement.setInt(1, wish.getUserID());
        statement.setString(2, wish.getProductName());

        // Executing SQL function
        statement.execute();

        // Retrieving data into ResultSet
        ResultSet res = statement.getResultSet();

        DefaultTableModel tableModel = new DefaultTableModel();
        String[] columnNames = {"UserID", "ProductName"};
        for (int i = 0; i < columnNames.length; i++) {
            tableModel.addColumn(columnNames[i]);
        }

        int counter = 0;
        while (res.next()) {
            //Add the data to the table model
            tableModel.insertRow(counter, new Object[]{res.getInt(1), res.getString(2)});
            counter++;
        }

        hashtable.put("My Wishlist", tableModel);

        int rowCount = tableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                System.out.print(tableModel.getValueAt(i, j) + " ");
            }
            System.out.println();
        }

        System.out.println("The method works");
        System.out.println("DB method contains My Wishlist: " + hashtable.containsKey("My Wishlist"));

        return hashtable;
    }

}

