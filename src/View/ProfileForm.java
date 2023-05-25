package View;

import Controller.Client;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;

public class ProfileForm implements ActionListener {
    private JPanel profilePanel;
    private JButton returnButton;
    private Client c;
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private JButton myRequests;
    private JButton orderHistory;
    private JButton searchByDate;
    private JButton acceptRequestButton;
    private JButton declineRequestButton;

    private JLabel title;

    private JButton myInventoryButton;

    private JButton removeProductFromCart;
    private JButton buyProductsInCart;

    private JButton visitWishlistButton;

    private JButton createWishButton;
    private JButton inboxButton;


    public ProfileForm(Client c) {
        this.c = c;
        returnButton = new JButton("← Marketplace");
        myRequests =  new JButton("Requests");
        orderHistory = new JButton("Order History");
        acceptRequestButton = new JButton("Accept Request");
        declineRequestButton = new JButton("Decline Request");
        searchByDate = new JButton("Search by date");
        title = new JLabel("My Inventory");
        myInventoryButton = new JButton("My Inventory");
        removeProductFromCart = new JButton("Remove Product");
        buyProductsInCart = new JButton("Buy Products");
        visitWishlistButton = new JButton("Wishlist");
        createWishButton = new JButton("Create new Wish");
        inboxButton = new JButton("Inbox");

        acceptRequestButton.setVisible(false);
        declineRequestButton.setVisible(false);
        searchByDate.setVisible(false);
        removeProductFromCart.setVisible(false);
        buyProductsInCart.setVisible(false);
        createWishButton.setVisible(false);
        inboxButton.setVisible(false);

        myInventoryButton.setEnabled(false);
        orderHistory.setEnabled(true);
        myRequests.setEnabled(true);

        profilePanel = new JPanel();
        profilePanel.setPreferredSize (new Dimension(944, 569));
        profilePanel.setLayout (null);

        profilePanel.add (returnButton);
        profilePanel.add (myRequests);
        profilePanel.add (orderHistory);
        profilePanel.add (acceptRequestButton);
        profilePanel.add (declineRequestButton);
        profilePanel.add(searchByDate);
        profilePanel.add(title);
        profilePanel.add(myInventoryButton);
        profilePanel.add(removeProductFromCart);
        profilePanel.add(buyProductsInCart);
        profilePanel.add(visitWishlistButton);
        profilePanel.add(createWishButton);
        profilePanel.add(inboxButton);


        returnButton.setFocusPainted(false);
        returnButton.setContentAreaFilled(false);
        returnButton.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));


        visitWishlistButton.setFocusPainted(false);
        visitWishlistButton.setContentAreaFilled(false);
        visitWishlistButton.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));

        myInventoryButton.setFocusPainted(false);
        myInventoryButton.setContentAreaFilled(false);
        myInventoryButton.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));

        myRequests.setFocusPainted(false);
        myRequests.setContentAreaFilled(false);
        myRequests.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));

        orderHistory.setFocusPainted(false);
        orderHistory.setContentAreaFilled(false);
        orderHistory.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));

        returnButton.setBounds (100, 425, 120, 25);
        myInventoryButton.setBounds(100,450,120,25);
        myRequests.setBounds(100, 475, 120, 25);
        orderHistory.setBounds(100, 500,120,25);
        visitWishlistButton.setBounds(100, 525, 120, 25);

        createWishButton.setBounds(500,450,150,25);
        inboxButton.setBounds(700,450,150,25);

        acceptRequestButton.setBounds(500, 450, 150, 25);
        declineRequestButton.setBounds(700, 450, 150, 25);

        removeProductFromCart.setBounds(500, 450, 150, 25);
        buyProductsInCart.setBounds(700, 450, 150, 25);

        searchByDate.setBounds(700, 450, 150, 25);

        title.setBounds((944/2)-90, 20, 180,40);
        title.setFont(new Font("Serif", Font.PLAIN, 30));

        addListeners();
    }

    public void createTableModel(DefaultTableModel tableModel){
        //If it is not null, it means that the table is already created and just needs to be updated with new data.
        if(this.tableModel == null){
            //Set the tableModel passed in the param to the local one, so it can be used outside of this function.
            this.tableModel = tableModel;
            table = new JTable(this.tableModel);
            scrollPane = new JScrollPane(table);
            scrollPane.setBounds(80, 60, 785, 360);
            profilePanel.add(scrollPane);
            profilePanel.revalidate();
        }
        else {
            //Just update the table if it's not null.
            this.tableModel = tableModel;
            //Set the new updated model to the jtable.
            table.setModel(this.tableModel);
        }
    }

    private void addListeners() {
        returnButton.addActionListener(this);
        returnButton.setActionCommand("marketplace");
        myRequests.addActionListener(this);
        myRequests.setActionCommand("requests");
        orderHistory.addActionListener(this);
        orderHistory.setActionCommand("orderHistory");
        acceptRequestButton.addActionListener(this);
        acceptRequestButton.setActionCommand("acceptRequest");
        declineRequestButton.addActionListener(this);
        declineRequestButton.setActionCommand("declineRequest");
        searchByDate.addActionListener(this);
        searchByDate.setActionCommand("searchByDate");
        myInventoryButton.addActionListener(this);
        myInventoryButton.setActionCommand("myInventory");
        removeProductFromCart.addActionListener(this);
        removeProductFromCart.setActionCommand("removeProductFromCart");
        buyProductsInCart.addActionListener(this);
        buyProductsInCart.setActionCommand("buyProductsFromCart");
        visitWishlistButton.addActionListener(this);
        visitWishlistButton.setActionCommand("visitWishlist");

        createWishButton.addActionListener(this);
        createWishButton.setActionCommand("createWish");
    }

    /**
     * This function accepts a buy request.
     */
    public void acceptRequest(){
        String productName = "";
        String buyerId = "";
        String productId = "";
        try {
            if(!table.getSelectionModel().isSelectionEmpty()) {
                productName = (String) tableModel.getValueAt(table.getSelectedRow(), 2);
                buyerId = (String) tableModel.getValueAt(table.getSelectedRow(), 1);
                productId = (String) tableModel.getValueAt(table.getSelectedRow(), 0);
                int input = JOptionPane.showOptionDialog(null, "Do you want to sell product: " +
                                productName + " to buyer: " + buyerId, "Purchase confirmation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, null, null);
                if(input == 0){
                    c.sendAcceptRequestToServer(productName, Integer.valueOf(buyerId), Integer.valueOf(productId));
                }
            } else{
                JOptionPane.showMessageDialog(null, "Pick an item you want to accept the buy request for." +
                        "then proceed to press the Accept Request button.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function declines a buy request.
     */
    public void declineRequest(){
        String productId = "";
        try {
            if(!table.getSelectionModel().isSelectionEmpty()) {
                productId = (String) tableModel.getValueAt(table.getSelectedRow(), 0);
                int input = JOptionPane.showOptionDialog(null, "Do you want to decline the request for the product: " + productId
                        , "Purchase confirmation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, null, null);
                if(input == 0){
                    c.sendDeclineRequestToServer(Integer.valueOf(productId));
                }
            } else{
                JOptionPane.showMessageDialog(null, "Pick an item you want to decline the buy request for." +
                        "then proceed to press the Decline Request button.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAllProducts() throws IOException{
        c.sendShowAllProductsToServer();
    }


    private void returnToMarket() throws IOException {
        c.accessMarketplace();
    }

    public JPanel getProfilePanel() {
        return profilePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        switch (action){
            case "marketplace":
                try {
                    searchByDate.setVisible(false);
                    acceptRequestButton.setVisible(false);
                    declineRequestButton.setVisible(false);
                    removeProductFromCart.setVisible(false);
                    buyProductsInCart.setVisible(false);
                    createWishButton.setVisible(false);
                    inboxButton.setVisible(false);
                    showAllProducts();
                    returnToMarket();
                    c.getMainForm().setProductPanel();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case "requests":
                try {
                    acceptRequestButton.setVisible(true);
                    declineRequestButton.setVisible(true);
                    searchByDate.setVisible(false);
                    removeProductFromCart.setVisible(false);
                    buyProductsInCart.setVisible(false);
                    createWishButton.setVisible(false);
                    inboxButton.setVisible(false);

                    myInventoryButton.setEnabled(true);
                    orderHistory.setEnabled(true);
                    myRequests.setEnabled(false);
                    visitWishlistButton.setEnabled(true);

                    c.accessRequests();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                break;
            case "orderHistory":
                try {
                    searchByDate.setVisible(true);
                    acceptRequestButton.setVisible(false);
                    declineRequestButton.setVisible(false);
                    removeProductFromCart.setVisible(false);
                    buyProductsInCart.setVisible(false);
                    createWishButton.setVisible(false);
                    inboxButton.setVisible(false);

                    myInventoryButton.setEnabled(true);
                    orderHistory.setEnabled(false);
                    myRequests.setEnabled(true);
                    visitWishlistButton.setEnabled(true);


                    c.accessOrderHistory();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "acceptRequest":
                    acceptRequest();
                break;
            case "declineRequest":
                declineRequest();
                break;
            case "searchByDate":
                searchByDate();
                break;
            case "myInventory":
                try {
                    searchByDate.setVisible(false);
                    acceptRequestButton.setVisible(false);
                    declineRequestButton.setVisible(false);
                    removeProductFromCart.setVisible(false);
                    buyProductsInCart.setVisible(false);
                    createWishButton.setVisible(false);
                    inboxButton.setVisible(false);

                    myInventoryButton.setEnabled(false);
                    orderHistory.setEnabled(true);
                    myRequests.setEnabled(true);
                    visitWishlistButton.setEnabled(true);


                    c.sendUserIdToServerProfile(c.getUserId());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case "removeProductFromCart":
                removeFromCart();
                break;

            case "visitWishlist":
                try {
                    searchByDate.setVisible(false);
                    acceptRequestButton.setVisible(false);
                    declineRequestButton.setVisible(false);
                    removeProductFromCart.setVisible(false);
                    buyProductsInCart.setVisible(false);
                    createWishButton.setVisible(true);
                    inboxButton.setVisible(true);

                    myInventoryButton.setEnabled(true);
                    orderHistory.setEnabled(true);
                    myRequests.setEnabled(true);
                    visitWishlistButton.setEnabled(false);

                    c.accessWishlist();  // Call the new method in the Client class
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;

            case "createWish":
                try {
                    createWish();  // Call a new method in the Client class
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            case "buyProductsFromCart":
                try {
                    requestProductsInCart();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
        }
    }

    private void requestProductsInCart() throws IOException {
        ArrayList<Integer> firstColumnValues = new ArrayList<>();
        int rowCount = tableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            String value = (String) tableModel.getValueAt(i, 0); // 0 represents the first column index
            c.requestItemInCart(Integer.valueOf(value));
        }
        c.sendCartRequestToServer();
    }

    private boolean removeFromCart() {
        String productId = "";
        try {
            if(!table.getSelectionModel().isSelectionEmpty()) {
                productId = (String) tableModel.getValueAt(table.getSelectedRow(), 0);
                int input = JOptionPane.showOptionDialog(null, "Do you want to remove productId: " +
                                productId + " from your shoppingcart?", "Remove from cart", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, null, null);
            } else{
                JOptionPane.showMessageDialog(null, "Pick an item you want to remove" +
                        "then proceed to press the remove from cart button");
                return false;
            }
            c.sendRemoveFromCartToServer(Integer.parseInt(productId));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void searchByDate(){
        String start, end;
        start = JOptionPane.showInputDialog( "Enter a start date for search. (Format: YYYY-MM-DD)");
        end = JOptionPane.showInputDialog( "Enter an end date for search. (Format: YYYY-MM-DD)");

        try {
            c.searchByDate(start, end);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTitle(String name) {
        title.setText(name);
        if (name.equals("My Inventory")){
            myInventoryButton.setEnabled(false);
            orderHistory.setEnabled(true);
            myRequests.setEnabled(true);
        }
    }

    public void cartButtons() {
        removeProductFromCart.setVisible(true);
        buyProductsInCart.setVisible(true);

        myInventoryButton.setEnabled(true);
        orderHistory.setEnabled(true);
        myRequests.setEnabled(true);
        visitWishlistButton.setEnabled(true);
    }

    private void createWish() throws IOException {
        String type=JOptionPane.showInputDialog(null,"Enter the products type");
        c.sendWishToServer(c.getUserId(), type);
    }
}
