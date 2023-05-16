package View;

import Controller.Client;
import Model.TableType;

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



    public ProfileForm(Client c) {
        this.c = c;
        returnButton = new JButton("Marketplace");
        myRequests =  new JButton("Requests");
        orderHistory = new JButton("Order History");

        profilePanel = new JPanel();
        profilePanel.setPreferredSize (new Dimension(944, 569));
        profilePanel.setLayout (null);

        profilePanel.add (returnButton);
        profilePanel.add (myRequests);
        profilePanel.add (orderHistory);

        returnButton.setBounds (100, 450, 120, 25);
        myRequests.setBounds(550, 450, 120, 25);
        orderHistory.setBounds(750, 450,120,25);

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

                break;
            case "orderHistory":
                break;
        }
    }
}
