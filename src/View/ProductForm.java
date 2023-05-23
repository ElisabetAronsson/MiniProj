package View;


import Controller.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ProductForm implements ActionListener {
    private JPanel productPanel;
    private JButton addProductButton;
    private JButton profileButton;
    private JButton purchaseButton;
    private JTable table;
    private Client c;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private JButton searchByTypeButton;
    private JButton searchByPriceButton;
    private JButton searchByConditionButton;

    public ProductForm(Client c) {
        //Apply passed client variable to the local one.
        this.c = c;

        //construct components
        addProductButton = new JButton("Add Product");
        purchaseButton = new JButton("Buy Product");
        profileButton = new JButton ("My Inventory");
        searchByTypeButton = new JButton("Search By Type");
        searchByPriceButton = new JButton("Search By Price");
        searchByConditionButton = new JButton("Search By Condition");


        //adjust size and set layout
        productPanel = new JPanel();
        productPanel.setPreferredSize (new Dimension(944, 569));
        productPanel.setLayout (null);

        //add components
        productPanel.add (addProductButton);
        productPanel.add (purchaseButton);
        productPanel.add (profileButton);
        productPanel.add (searchByTypeButton);
        productPanel.add (searchByPriceButton);
        productPanel.add (searchByConditionButton);

        //set component bounds (only needed by Absolute Positioning)
        addProductButton.setBounds (100, 450, 120, 25);
        purchaseButton.setBounds (250, 450, 120,25);
        profileButton.setBounds (700, 450, 120, 25);
        searchByTypeButton.setBounds (100, 500, 120, 25);
        searchByPriceButton.setBounds (420, 500, 120, 25);
        searchByConditionButton.setBounds (250, 500, 150,25);


        //Add listeners to buttons
        addListeners();
    }

    /**
     * Creates the JTable that is displayed.
     * @param tableModel A table model with data from the database.
     */
    public void createTableModel(DefaultTableModel tableModel){
        //If the tablemodel in the class is null, create the JTable for the first time.
        //If it is not null, it means that the table is already created and just needs to be updated with new data.
        if(this.tableModel == null){
            //Set the tableModel passed in the param to the local one, so it can be used outside of this function.
            this.tableModel = tableModel;
            table = new JTable(this.tableModel);
            scrollPane = new JScrollPane(table);
            scrollPane.setBounds(80, 60, 785, 360);
            productPanel.add(scrollPane);
            productPanel.revalidate();
        }
        else {
            //Just update the table if it's not null.
            this.tableModel = tableModel;
            //Set the new updated model to the jtable.
            table.setModel(this.tableModel);
        }
    }

    /**
     * Adds ActionListeners for the buttons.
     */
    public void addListeners(){
        addProductButton.addActionListener(this);
        addProductButton.setActionCommand("addProduct");

        purchaseButton.addActionListener(this);
        purchaseButton.setActionCommand("buyProduct");

        profileButton.addActionListener(this);
        profileButton.setActionCommand("visitProfile");

        searchByTypeButton.addActionListener(this);
        searchByTypeButton.setActionCommand("searchType");

        searchByConditionButton.addActionListener(this);
        searchByConditionButton.setActionCommand("searchCondition");

        searchByPriceButton.addActionListener(this);
        searchByPriceButton.setActionCommand("searchPrice");


    }

    /**
     * Adds a product for sale to the database and the GUI.
     */
    public void addProduct(){
        try {
            String type=JOptionPane.showInputDialog(null,"Enter the products type");
            int price=Integer.parseInt(JOptionPane.showInputDialog(null,"Enter the products price"));
            String productionYear=JOptionPane.showInputDialog(null,"Enter the products production year");
            String color=JOptionPane.showInputDialog(null,"Enter the products color");
            String condition=JOptionPane.showInputDialog(null,"Enter the products condition");
            c.sendProductToServer(type, price, productionYear, color, condition);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean buyProduct(){
        String productId = "";
        try {
            if(!table.getSelectionModel().isSelectionEmpty()) {
                productId = (String) tableModel.getValueAt(table.getSelectedRow(), 0);
                int input = JOptionPane.showOptionDialog(null, "Do you want to buy productId: " +
                                productId, "Purchase confirmation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, null, null);
            } else{
                JOptionPane.showMessageDialog(null, "Pick an item you want to purchase" +
                        "then proceed to press the purchase button");
                return false;
            }

            c.sendRequesttoServer(Integer.parseInt(productId));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Searches after a product by its type.
     * @throws IOException
     */
    public void searchByType() throws IOException {
        String type=JOptionPane.showInputDialog(null,"Enter the products type");
        c.sendSearchByTypeToServer(type);
    };

    /**
     * Searches after a product by its price.
     * @throws IOException
     */
    public void searchByPrice() throws IOException {
        String price=JOptionPane.showInputDialog(null,"Enter the products price");
        c.sendSearchByPriceToServer(price);
    };

    /**
     * Searches after a product by its condition.
     * @throws IOException
     */
    public void searchByCondition() throws IOException {
        String condition=JOptionPane.showInputDialog(null,"Enter the products condition");
        c.sendSearchByConditionToServer(condition);
    };

    /**
     * Returns the product panel.
     * @return productPanel
     */
    public JPanel getProductPanel(){
        return this.productPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        switch(action){
            case "addProduct":
                addProduct();
                break;
            case "buyProduct":
                buyProduct();
                break;
            case "visitProfile":
                try {
                    System.out.println("what");
                    c.sendUserIdToServerProfile(c.getUserId());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case "searchType":
                try {
                    searchByType();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "searchPrice":
                try {
                    searchByPrice();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "searchCondition":
                try {
                    searchByCondition();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;

        }

    }
}
