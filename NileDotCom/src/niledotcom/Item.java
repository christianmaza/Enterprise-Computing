/* Name: Christian Mazariegos
Course: CNT 4714 – Spring 2021
Assignment title: Project 1 – Event-driven Enterprise Simulation 
Date: Sunday January 31, 2021
*/
package niledotcom;

public class Item {
    private String itemId; 
    private String itemDescription; 
    private double itemPrice;
    private double itemFinalPrice;
    private int itemDiscount;

    public double getItemFinalPrice() {
        return itemFinalPrice;
    }

    public void setItemFinalPrice(double itemFinalPrice) {
        this.itemFinalPrice = itemFinalPrice;
    }
    private int itemQuatnity;

    public int getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(int itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public int getItemQuatnity() {
        return itemQuatnity;
    }

    public void setItemQuatnity(int itemQuatnity) {
        this.itemQuatnity = itemQuatnity;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }
    
    
}
