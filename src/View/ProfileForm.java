package View;

import Controller.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ProfileForm implements ActionListener {
    private JPanel profilePanel;
    private JButton returnButton;
    private Client c;
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private JButton myRequests;
    private JButton orderHistory;
    private JButton acceptRequestButton;



    public ProfileForm(Client c) {
        this.c = c;
        returnButton = new JButton("Marketplace");
        myRequests =  new JButton("Requests");
        orderHistory = new JButton("Order History");
        acceptRequestButton = new JButton("Accept Request");

        profilePanel = new JPanel();
        profilePanel.setPreferredSize (new Dimension(944, 569));
        profilePanel.setLayout (null);

        profilePanel.add (returnButton);
        profilePanel.add (myRequests);
        profilePanel.add (orderHistory);
        profilePanel.add (acceptRequestButton);

        returnButton.setBounds (100, 450, 120, 25);
        myRequests.setBounds(550, 450, 120, 25);
        orderHistory.setBounds(750, 450,120,25);
        acceptRequestButton.setBounds(750, 500, 120, 25);

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
                    returnToMarket();
                    c.getMainForm().setProductPanel();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case "requests":
                try {
                    System.out.println("requests button pressed");
                    c.accessRequests();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                break;
            case "orderHistory":
                try {
                    c.accessOrderHistory();
                    System.out.println("orderHistory pressed");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "acceptRequest":
                    acceptRequest();
                break;
        }
    }
}
