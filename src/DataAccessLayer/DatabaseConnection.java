package DataAccessLayer;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This class managse the database connection.
 */
public class DatabaseConnection {
    private Connection connection;

    /**
     * Construct the class and connect to the database.
     */
    public DatabaseConnection(){
        String url = "jdbc:postgresql://db.tdkecbgmmdghrwajxfmv.supabase.co/Marketplace";

        Properties props = new Properties();
        props.setProperty("user", getUsernameFromTxt());
        props.setProperty("password", getPasswordFromTxt());

        try {
            connection = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the username that is in the text file.
     * @return username
     * @throws IOException
     */
    public String getUsernameFromTxt() {
        String[] text = new String[2];
        try {
            String path = "resources/PasswordsAndKeys.txt";
            File file = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String s;
            int counter = 0;
            while((s = reader.readLine()) != null){
                text[counter] = s.substring(s.indexOf("=") + 1);
                counter++;
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } {

        }
        return text[0];
    }

    /**
     * Returns the password that is in the textfile.
     * @return password
     * @throws IOException
     */
    public String getPasswordFromTxt() {
        String[] text = new String[2];
        try {
            String path = "resources/PasswordsAndKeys.txt";
            File file = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String s;
            int counter = 0;
            while((s = reader.readLine()) != null){
                text[counter] = s.substring(s.indexOf("=") + 1);
                counter++;
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } {

        }
        return text[1];
    }

    /**
     * Returns the database connection.
     * @return connection.
     */
    public Connection getConnection(){
        return this.connection;
    }
}
