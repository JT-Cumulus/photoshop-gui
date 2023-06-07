package service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Create class for already saved orders
public class Order {
    private List<Item> currentOrder;
    private Integer orderID;
    private double totalPrice;
    private long totalTimeTaken;
    private LocalDate pickupDate;
    private LocalTime pickupTime;

    public Order(List<Item> currentOrder, Integer orderID, double totalPrice, long totalTimeTaken, LocalDate pickupDate, LocalTime pickupTime) {
        this.currentOrder = currentOrder;
        this.orderID = orderID;
        this.totalPrice = totalPrice;
        this.totalTimeTaken = totalTimeTaken;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
    }

    public Order(){
        currentOrder = new ArrayList<Item>();
    }
    
    public List<Item> getCurrentOrder() {
        return this.currentOrder;
    }

    public void setCurrentOrder(List<Item> currentOrder) {
        this.currentOrder = currentOrder;
    }

    public Integer getOrderID() {
        return this.orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getTotalTimeTaken() {
        return this.totalTimeTaken;
    }

    public void setTotalTimeTaken(long totalTimeTaken) {
        this.totalTimeTaken = totalTimeTaken;
    }

    public LocalDate getPickupDate() {
        return this.pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalTime getPickupTime(){
        return this.pickupTime;
    }

    public void setPickupTime(LocalTime pickupTime){
        this.pickupTime = pickupTime;
    }

    // Convert the pickup date to a string
    public String dateToString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formattedString = this.getPickupDate().format(formatter);
        return formattedString;
    }

    // Convert the pickup time to a string
    public String timeToString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedString = this.getPickupTime().format(formatter);
        return formattedString;
    }

    // Displays the order without changing any values
    public void displayOrder(){
        System.out.println(String.format("%-5s %-30s %15s %20s %10s" , "ID", "Item", "Price(EUR)", "Time to Make (min)", "Quantity"));
        for (Item object: this.currentOrder) {
            System.out.println(object + "\t   " + object.getQuantity());
        }
        System.out.println("");
        System.out.println(String.format("%-20s %10.2f %-10s", "Total Price: ", this.getTotalPrice(), "EUR"));
    }

    // Show the pickup time
    public void displayPickupTime(){
        System.out.println(String.format("%-20s %10s %-10s", "Pickup Date: ", this.getPickupDate(), this.getPickupTime()));
    }

    // Return the item within the cart such that the quantity can be updated in the shopping cart
    public Item getIteminCart(Item item){
        List<Item> temp = this.currentOrder;
        for (Item object: temp) {
            if (object.getId() == item.getId()){
                return object;
            } 
        }
        return null;
    }

    // Add item to the shopping cart
    public void addItem(Item item){
        if(getIteminCart(item) != null) {
            getIteminCart(item).addQuantity();
        } else {
            this.currentOrder.add(item);
        }        
    }

    // Remove item from shopping cart
    public void removeItem(Item item){
        if(getIteminCart(item) != null) {
            getIteminCart(item).addQuantity();
        } else {
            this.currentOrder.remove(item);
        }
    }
}
