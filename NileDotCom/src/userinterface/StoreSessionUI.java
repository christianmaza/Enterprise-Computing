/* Name: Christian Mazariegos
Course: CNT 4714 – Spring 2021
Assignment title: Project 1 – Event-driven Enterprise Simulation 
Date: Sunday January 31, 2021
*/
package userinterface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import niledotcom.Item;
import niledotcom.StoreSession;

public class StoreSessionUI {
    private static StoreSessionUI ui;
    private JFrame frame;
    private StoreSession storeSession;
    private JPanel itemLookup;
    private JPanel optionsPanel;
    private JButton processButton;
    private JButton confirmButton; 
    private JButton viewOrderButton; 
    private JButton finishOrderButton; 
    private JButton newOrderButton; 
    private JButton exitButton;
    private GridLayout gridLayout;
    private FlowLayout flowLayout; 
    private JLabel numItemsLabel;
    private JLabel itemIdLabel; 
    private JLabel itemQuantityLabel; 
    private JLabel itemInfoLabel; 
    private JLabel subtotalLabel;
    private JTextField numItemsInput;
    private JTextField itemIdInput; 
    private JTextField itemQuantityInput;
    private JTextField itemInfoOutput; 
    private JTextField subtotalOutput; 
    private ProcessListener processListen; 
    private ConfirmListener confirmListen; 
    private ViewOrderListener viewOrderListen; 
    private FinishOrderListener finishOrderListen; 
    private NewOrderListener newOrderListen; 
    private ExitListener exitListen; 
    
    public StoreSessionUI(StoreSession storeSessionParam){
        storeSession = storeSessionParam;
        storeSession.setItemsInCounter(0);
        storeSession.setSubtotalCounter(0);
        storeSession.setItemCounter(1);
        initObjects();
        initComponents();
        ui = this;
    }
    
    public void initObjects(){
        gridLayout = new GridLayout(0,2); 
        flowLayout = new FlowLayout();
    }
    
    public void initComponents(){
        frame = new JFrame("Nile Dot Com");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        numItemsLabel = new JLabel("Enter number of items in this order:"); 
        itemIdLabel = new JLabel("Enter Item ID for Item #" + storeSession.getItemCounter() + ":");
        itemQuantityLabel = new JLabel("Enter quantity for Item #" + storeSession.getItemCounter() + ":");
        itemInfoLabel = new JLabel("Item #" + storeSession.getItemCounter() + " info:");
        subtotalLabel = new JLabel("subtotal for " + storeSession.getSubtotalCounter() + " item(s)");
        
        numItemsInput = new JTextField(); 
        numItemsInput.setColumns(29);
        itemIdInput = new JTextField();
        itemIdInput.setColumns(29);
        itemQuantityInput = new JTextField();
        itemQuantityInput.setColumns(29);
        itemInfoOutput = new JTextField();
        itemInfoOutput.setColumns(29);
        itemInfoOutput.setEditable(false);
        itemInfoOutput.setEnabled(false);
        subtotalOutput = new JTextField();
        subtotalOutput.setColumns(29);
        subtotalOutput.setEditable(false);
        subtotalOutput.setEnabled(false);
        
        itemLookup = new JPanel();
        itemLookup.setLayout(gridLayout);
        itemLookup.add(numItemsLabel);
        itemLookup.add(numItemsInput);
        itemLookup.add(itemIdLabel);  
        itemLookup.add(itemIdInput);
        itemLookup.add(itemQuantityLabel);
        itemLookup.add(itemQuantityInput);
        itemLookup.add(itemInfoLabel); 
        itemLookup.add(itemInfoOutput);
        itemLookup.add(subtotalLabel); 
        itemLookup.add(subtotalOutput);
        
        processButton = new JButton("Process Item #" + storeSession.getItemCounter());
        confirmButton = new JButton("Confirm Item#" + storeSession.getItemCounter());
        viewOrderButton = new JButton("View Order"); 
        finishOrderButton = new JButton("Finish Order"); 
        newOrderButton = new JButton("New Order"); 
        exitButton = new JButton("Exit"); 
        
        processListen = new ProcessListener();
        processButton.addActionListener(processListen); 
        confirmListen = new ConfirmListener(); 
        confirmButton.addActionListener(confirmListen);
        confirmButton.setEnabled(false);
        viewOrderListen = new ViewOrderListener();
        viewOrderButton.addActionListener(viewOrderListen);
        viewOrderButton.setEnabled(false);
        finishOrderListen = new FinishOrderListener();
        finishOrderButton.addActionListener(finishOrderListen);
        finishOrderButton.setEnabled(false);
        newOrderListen = new NewOrderListener(); 
        newOrderButton.addActionListener(newOrderListen); 
        exitListen = new ExitListener(); 
        exitButton.addActionListener(exitListen); 
        
        optionsPanel = new JPanel(); 
        optionsPanel.setLayout(flowLayout); 
        optionsPanel.add(processButton); 
        optionsPanel.add(confirmButton);
        optionsPanel.add(viewOrderButton); 
        optionsPanel.add(finishOrderButton); 
        optionsPanel.add(newOrderButton); 
        optionsPanel.add(exitButton); 
        
        frame.add(itemLookup, BorderLayout.WEST); 
        frame.add(optionsPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
    
    private class ProcessListener implements ActionListener{
        
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean result = false;
            String rawItemId = "";
            int rawItemQuantity = 0;
            
            try{
                if(storeSession.getNumItems() == 0){
                    storeSession.setNumItems(Integer.parseInt(numItemsInput.getText()));
                }
                rawItemId = itemIdInput.getText();
                rawItemQuantity = Integer.parseInt(itemQuantityInput.getText());
                numItemsInput.setEnabled(false);
                numItemsInput.setEditable(false);
                result = storeSession.processItem(storeSession, rawItemId, rawItemQuantity, frame, ui);
                
                
                if(result){
                    Item currentItem = storeSession.getTemp();
                    
                    itemInfoOutput.setText(String.format("%s %s $%.2f %d %%%d $%.2f", currentItem.getItemId(), currentItem.getItemDescription(),
                            currentItem.getItemPrice(), currentItem.getItemQuatnity(), currentItem.getItemDiscount(), 
                            currentItem.getItemFinalPrice()));
                    
                    processButton.setEnabled(false);
                    confirmButton.setEnabled(true);
                    frame.revalidate();
                    frame.repaint();
                } 
            }
            catch(NumberFormatException n){
                JOptionPane.showMessageDialog(frame, "Enter a valid number for either:\n\"Enter number "
                        + "of items in this order\"\nOR\n\"Enter quantity for Item #" + storeSession.getItemCounter() + "\"");
            }
        }
        
    } 
    
    private class ConfirmListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            storeSession.getOrder().add(storeSession.getTemp());
            storeSession.setSubtotal(storeSession.getSubtotal() + storeSession.getOrder().get(storeSession.getItemsInCounter()).getItemFinalPrice());
            JOptionPane.showMessageDialog(frame, "Item #" + (storeSession.getItemCounter()) + " accepted");
            storeSession.setItemsInCounter(storeSession.getItemsInCounter() + 1);
            storeSession.setItemCounter(storeSession.getItemCounter() + 1);
            storeSession.setSubtotalCounter(storeSession.getSubtotalCounter() + 1);
            itemIdInput.setText("");
            itemQuantityInput.setText("");
            
            if(storeSession.getOrder().size() < storeSession.getNumItems()){
                if(storeSession.getOrder().size() == 1){
                    viewOrderButton.setEnabled(true);
                    finishOrderButton.setEnabled(true);
                }
                itemIdLabel.setText("Enter Item ID for Item #" + storeSession.getItemCounter() + ":");
                itemQuantityLabel.setText("Enter quantity for Item #" + storeSession.getItemCounter() + ":");
                itemInfoLabel.setText("Item #" + storeSession.getItemCounter() + " info:");
                processButton.setText("Process Item #" + storeSession.getItemCounter());
                confirmButton.setText("Confirm Item#" + storeSession.getItemCounter());
                processButton.setEnabled(true);
                confirmButton.setEnabled(false);
            }
            else{
                if(storeSession.getOrder().size() == 1){
                    viewOrderButton.setEnabled(true);
                    finishOrderButton.setEnabled(true);
                }
                itemIdLabel.setText("");
                itemQuantityLabel.setText("");
                itemIdInput.setEditable(false);
                itemIdInput.setEnabled(false);
                itemQuantityInput.setEditable(false);
                itemQuantityInput.setEnabled(false);
                processButton.setText("Process Item");
                processButton.setEnabled(false);
                confirmButton.setText("Confirm Item"); 
                confirmButton.setEnabled(false);
            }
            
            subtotalLabel.setText("subtotal for " + storeSession.getSubtotalCounter() + " item(s)");
            subtotalOutput.setText(String.format("$%.2f", storeSession.getSubtotal()));
            storeSession.setTemp(new Item());
            frame.revalidate();
            frame.repaint();

        }
        
    } 
    
    private class ViewOrderListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            storeSession.viewOrder(storeSession, frame);
        }
        
    }
    
    private class FinishOrderListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            viewOrderButton.setEnabled(false);
            finishOrderButton.setEnabled(false);
            storeSession.finishOrder(storeSession, frame);
        }
        
    }
    
    private class NewOrderListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            storeSession.newOrder(storeSession, frame, ui);
        }
        
    }
    
    private class ExitListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            storeSession.getScan().close();
            storeSession.getScanLine().close();
            System.exit(0);
        }
        
    }

    public JButton getProcessButton() {
        return processButton;
    }

    public void setProcessButton(JButton processButton) {
        this.processButton = processButton;
    }

    public JButton getConfirmButton() {
        return confirmButton;
    }

    public void setConfirmButton(JButton confirmButton) {
        this.confirmButton = confirmButton;
    }

    public JButton getViewOrderButton() {
        return viewOrderButton;
    }

    public void setViewOrderButton(JButton viewOrderButton) {
        this.viewOrderButton = viewOrderButton;
    }

    public JButton getNewOrderButton() {
        return newOrderButton;
    }

    public void setNewOrderButton(JButton newOrderButton) {
        this.newOrderButton = newOrderButton;
    }

    public JLabel getItemIdLabel() {
        return itemIdLabel;
    }

    public void setItemIdLabel(JLabel itemIdLabel) {
        this.itemIdLabel = itemIdLabel;
    }

    public JLabel getItemQuantityLabel() {
        return itemQuantityLabel;
    }

    public void setItemQuantityLabel(JLabel itemQuantityLabel) {
        this.itemQuantityLabel = itemQuantityLabel;
    }

    public JTextField getItemIdInput() {
        return itemIdInput;
    }

    public void setItemIdInput(JTextField itemIdInput) {
        this.itemIdInput = itemIdInput;
    }

    public JTextField getItemQuantityInput() {
        return itemQuantityInput;
    }

    public void setItemQuantityInput(JTextField itemQuantityInput) {
        this.itemQuantityInput = itemQuantityInput;
    }

    public JTextField getNumItemsInput() {
        return numItemsInput;
    }

    public void setNumItemsInput(JTextField numItemsInput) {
        this.numItemsInput = numItemsInput;
    }

    public JLabel getItemInfoLabel() {
        return itemInfoLabel;
    }

    public void setItemInfoLabel(JLabel itemInfoLabel) {
        this.itemInfoLabel = itemInfoLabel;
    }

    public JLabel getSubtotalLabel() {
        return subtotalLabel;
    }

    public void setSubtotalLabel(JLabel subtotalLabel) {
        this.subtotalLabel = subtotalLabel;
    }

    public JTextField getItemInfoOutput() {
        return itemInfoOutput;
    }

    public void setItemInfoOutput(JTextField itemInfoOutput) {
        this.itemInfoOutput = itemInfoOutput;
    }

    public JTextField getSubtotalOutput() {
        return subtotalOutput;
    }

    public void setSubtotalOutput(JTextField subtotalOutput) {
        this.subtotalOutput = subtotalOutput;
    }

    public JButton getFinishOrderButton() {
        return finishOrderButton;
    }

    public void setFinishOrderButton(JButton finishOrderButton) {
        this.finishOrderButton = finishOrderButton;
    }
    
    
}
