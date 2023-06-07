package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// For items in the catalogue
// With ID, name, price, and quantity for the item
public class Item {
    private int id;
    private String name;
    private double price;
    private int minutes;
    private int quantity;

    public Item(int id, String name, double price, int minutes){
        this.id = id;
        this.name = name;
        this.price = price;
        this.minutes = minutes;
        this.quantity = 1;
    }

    public Item(Item source){
        this.id = source.id;
        this.name = source.name;
        this.price = source.price;
        this.minutes = source.minutes;
        this.quantity = 1;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public int getMinutes(){
        return this.minutes;
    }

    public void setMinutes(int minutes){
        this.minutes = minutes;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void addQuantity(){
        this.quantity += 1;
    }

    // Function to break up items 
    private static Item createItem(String[] metadata) {
        int id = Integer.parseInt(metadata[0]);
        String name = metadata[1];
        double price = Double.parseDouble(metadata[2]);
        int hours = toMins(metadata[3]);

        return new Item(id, name, price, hours);
    }

    private static int toMins(String s) {
        String[] hourMin = s.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int mins = Integer.parseInt(hourMin[1]);
        int hoursInMins = hour * 60;
        return hoursInMins + mins;
    }

    // Load items from pricelist csv
    public static Catalogue loadItems(Catalogue catalogue){
        String fileName = "database/PhotoShop_PriceList.csv";
        File file = new File(fileName);

        // this gives you a 2-dimensional array of strings
        Scanner inputStream;

        try{
            inputStream = new Scanner(file);
            while(inputStream.hasNext()){
                String line = inputStream.nextLine();
                String[] values = line.split(";");
                // this adds the currently parsed line to the 2-dimensional string array
                Item items = createItem(values);
                catalogue.addItem(items);
            }
            inputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return catalogue;
    }

    public String toString() {
       // Format print to be in readable columns with correct decimal points
        return String.format("%-5s %-30s %15.2f %20s" , this.getId(), this.getName(), this.getPrice(), this.getMinutes() );
    }    


}
