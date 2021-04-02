/* Name: Christian Mazariegos
Course: CNT 4714 – Spring 2021
Assignment title: Project 1 – Event-driven Enterprise Simulation 
Date: Sunday January 31, 2021
*/
package niledotcom;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import userinterface.StoreSessionUI;


public class StoreSession {
    
    //StoreSession fields
    private static StoreSessionUI ui;
    private static Scanner scan;
    private static Scanner scanLine;
    private static int numItems;
    private static double subtotal;
    private static int itemCounter;
    private static int itemsInCounter;
    private static int subtotalCounter;
    private static Item temp;
    private static ArrayList<Item> order;
    public static final double TAX_RATE = 0.06;
    
    public static void main(String[] args) {
       ui = new StoreSessionUI(new StoreSession());
    }

    public static Item getTemp() {
        return StoreSession.temp;
    }

    public static void setTemp(Item temp) {
        StoreSession.temp = temp;
    }
    
    
    
    public static int getItemsInCounter() {
        return itemsInCounter;
    }

    public static void setItemsInCounter(int itemsInCounter) {
        StoreSession.itemsInCounter = itemsInCounter;
    }
    
    
    
    public static double getSubtotal() {
        return subtotal;
    }

    public static void setSubtotal(double subtotal) {
        StoreSession.subtotal = subtotal;
    }

    public static int getSubtotalCounter() {
        return subtotalCounter;
    }

    public static void setSubtotalCounter(int subtotalCounter) {
        StoreSession.subtotalCounter = subtotalCounter;
    }
    
    public static int getNumItems() {
        return numItems;
    }

    public static void setNumItems(int numItems) {
        StoreSession.numItems = numItems;
    }

    public static int getItemCounter() {
        return itemCounter;
    }

    public static void setItemCounter(int itemCounter) {
        StoreSession.itemCounter = itemCounter;
    }

    public static ArrayList<Item> getOrder() {
        return order;
    }

    public static void setOrder(ArrayList<Item> order) {
        StoreSession.order = order;
    }

    public static Scanner getScan() {
        return scan;
    }

    public static void setScan(Scanner scan) {
        StoreSession.scan = scan;
    }

    public static Scanner getScanLine() {
        return scanLine;
    }

    public static void setScanLine(Scanner scanLine) {
        StoreSession.scanLine = scanLine;
    }
    
    
    
    public static boolean processItem(StoreSession sessionP, String itemIdP, int quantityItemsP, JFrame f, StoreSessionUI uiP){
        double discount; 
        String target = "";
        
        if(sessionP.getOrder() == null){
            sessionP.setTemp(new Item());
            sessionP.setOrder(new ArrayList<Item>());
        }
        
        if(quantityItemsP <= 4){
            discount = 0.00;
        }
        else if(quantityItemsP > 4 && quantityItemsP <= 9){
            discount = 0.10;
        }
        else if(quantityItemsP > 10 && quantityItemsP <= 14){
            discount = 0.15;
        }
        else{
            discount = 0.20;
        }
        
        try{
            File fp = new File("./src/inventory/inventory.txt");
            scan = new Scanner(fp);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(f, "Inventory file not found");
        } 
        
        scan.useDelimiter(", ");
        String pattern = "\"[^\"]*\"";
        
        while(scan.hasNext()){
            String a = scan.nextLine();
            scanLine = new Scanner(a);
            scanLine.useDelimiter(", ");
            
            String b = scanLine.next(); 
            String c = scanLine.next(pattern); 
            boolean d = scanLine.nextBoolean();
            double e = scanLine.nextDouble();
            
            if(b.contentEquals(itemIdP)){
                target = a;
       
                if(d){
                    sessionP.getTemp().setItemId(b);
                    sessionP.getTemp().setItemDescription(c);
                    sessionP.getTemp().setItemPrice(e);
                    sessionP.getTemp().setItemDiscount((int)(discount * 100));
                    sessionP.getTemp().setItemFinalPrice((e * quantityItemsP) - ((e * quantityItemsP) * discount));
                    sessionP.getTemp().setItemQuatnity(quantityItemsP);
                    return true;
                }
                else{
                    JOptionPane.showMessageDialog(f, "Sorry...that item is out of stock, please try another item");
                    uiP.getProcessButton().setEnabled(true);
                    uiP.getConfirmButton().setEnabled(false);
                    return false;
                }
            }
        }
        if(target.isEmpty()){
            JOptionPane.showMessageDialog(f, "Item ID " + itemIdP + " not in file");
            return false;
        }
        else{
            return false;
        }
    }
    
    public static void viewOrder(StoreSession sessionP, JFrame f){
        String viewOrderString = ""; 
            int counter = 1;
            for(Item i : sessionP.getOrder()){
                viewOrderString+= String.format("%d.%s %s $%.2f %d %%%d $%.2f\n", counter, i.getItemId(), i.getItemDescription(),
                            i.getItemPrice(), i.getItemQuatnity(), i.getItemDiscount(), 
                            i.getItemFinalPrice());
                counter++;
            }
            JOptionPane.showMessageDialog(f, viewOrderString);
    }
    
    public static void finishOrder(StoreSession sessionP, JFrame f){
        String ordersString = "";
        String writeString = "";
        int counter = 1;
        Charset charset = Charset.forName("UTF-8");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yy, hh:mm:ss a z"); 
        DateTimeFormatter formatWrite = DateTimeFormatter.ofPattern("dMMyyyyHHm");
        String dateTime = ZonedDateTime.now().format(format);
        String transactionId = ZonedDateTime.now().format(formatWrite);
        String subtotalString = String.format("%.2f", sessionP.getSubtotal());
        String taxAmount = String.format("%.2f", (sessionP.TAX_RATE * sessionP.getSubtotal()));
        String orderTotal = String.format("%.2f", (Double.parseDouble(taxAmount) + sessionP.getSubtotal()));
        
        for(Item i : sessionP.getOrder()){
                writeString+= String.format("%s, %s, %s, %.2f, %d, %.1f, $%.2f, %s\n", transactionId, i.getItemId(), i.getItemDescription(),
                            i.getItemPrice(), i.getItemQuatnity(), (i.getItemDiscount() / 100.00), 
                            i.getItemFinalPrice(), dateTime);
        }
        
        Path p = Paths.get("./transactions.txt");
        
        
        try(BufferedWriter writer = Files.newBufferedWriter(p, charset, StandardOpenOption.CREATE, StandardOpenOption.APPEND)){
            writer.write(writeString, 0, writeString.length());
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(f, "Could not write to " + p.toString());
        }
        
        for(Item i : sessionP.getOrder()){
                ordersString+= String.format("%d.%s %s $%.2f %d %%%d $%.2f\n", counter, i.getItemId(), i.getItemDescription(),
                            i.getItemPrice(), i.getItemQuatnity(), i.getItemDiscount(), 
                            i.getItemFinalPrice());
                counter++;
        }
        
        String viewOrderString = "Date: " + dateTime + "\n\n" + "Number of line items: " + sessionP.getSubtotalCounter()+ "\n\n"
                + "Item#/ID/Title/Price/Qty/Disc %/Subtotal:\n\n" + ordersString + "\n"
                + "Order subtotal: $" + subtotalString + "\n\n" + "Tax Rate: " + (int)(sessionP.TAX_RATE * 100.00) + "%\n\n"
                + "Tax Amount: $" + taxAmount + "\n\n" + "Order Total: $" + orderTotal + "\n\n"
                + "Thanks for shopping at NileDotCom!";
        
        JOptionPane.showMessageDialog(f, viewOrderString);
    } 
    
    public static void newOrder(StoreSession sessionP, JFrame f, StoreSessionUI ui){
        sessionP.setNumItems(0);
        sessionP.setItemsInCounter(0);
        sessionP.setSubtotalCounter(0);
        sessionP.setItemCounter(1);
        sessionP.setSubtotal(0);
        sessionP.getOrder().clear();
        sessionP.setTemp(new Item());
        
        ui.getNumItemsInput().setText("");
        ui.getNumItemsInput().setEditable(true);
        ui.getNumItemsInput().setEnabled(true);
        ui.getItemIdInput().setText("");
        ui.getItemIdInput().setEnabled(true);
        ui.getItemIdInput().setEditable(true);
        ui.getItemIdLabel().setText(("Enter Item ID for Item #" + sessionP.getItemCounter() + ":"));
        ui.getItemQuantityInput().setText(""); 
        ui.getItemQuantityInput().setEditable(true);
        ui.getItemQuantityInput().setEnabled(true);
        ui.getItemQuantityLabel().setText("Enter quantity for Item #" + sessionP.getItemCounter() + ":");
        ui.getItemInfoOutput().setText("");
        ui.getItemInfoLabel().setText("Item #" + sessionP.getItemCounter() + " info:");
        ui.getSubtotalOutput().setText("");
        ui.getSubtotalLabel().setText("subtotal for " + sessionP.getSubtotalCounter() + " item(s)");
        
        ui.getProcessButton().setEnabled(true);
        ui.getProcessButton().setText("Process Item #" + sessionP.getItemCounter());
        ui.getConfirmButton().setEnabled(false);
        ui.getConfirmButton().setText("Confirm Item#" + sessionP.getItemCounter());
        ui.getViewOrderButton().setEnabled(false);
        ui.getFinishOrderButton().setEnabled(false);
        f.revalidate();
        f.repaint();
    }
}
