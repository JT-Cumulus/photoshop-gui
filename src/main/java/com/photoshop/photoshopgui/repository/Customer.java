package repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Customer {

    private int ID;
    private String firstName;
    private String lastName;
    private String address;
    private String postcode;
    private String city;
    private String email;
    private String mobile;

    public Customer(int ID, String firstName, String lastName, String address, String postcode, String city, String email, String mobile) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.email = email;
        this.mobile = mobile;
    }

    public Customer(Customer source) {
        this.ID = source.ID;
        this.firstName = source.firstName;
        this.lastName = source.lastName;
        this.address = source.address;
        this.postcode = source.postcode;
        this.city = source.city;
        this.email = source.email;
        this.mobile = source.mobile;
    }

    public static Customer createCustomer(String[] metadata) {
        int id = Integer.parseInt(metadata[0]);
        String firstName = metadata[1];
        String lastName = metadata[2];
        String address = metadata[3];
        String postcode = metadata[4];
        String city = metadata[5];
        String email = metadata[6];
        String mobile = metadata[7];

        return new Customer(id, firstName, lastName, address, postcode, city, email, mobile);
    }

    public static Customer createNewCustomer(String[] metadata) {
        String firstName = metadata[1];
        String lastName = metadata[2];
        String address = metadata[3];
        String postcode = metadata[4];
        String city = metadata[5];
        String email = metadata[6];
        String mobile = metadata[7];

        return new Customer(Customer.loadIdIncrement(), firstName, lastName, address, postcode, city, email, mobile);
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    
    // Load current id for customers
    public static int loadIdIncrement(){
        String fileName = "user/PhotoShop_Customers.csv";
        File file = new File(fileName);
        int id = 2;

        // this gives you a 2-dimensional array of strings
        Scanner inputStream;

        try{
            inputStream = new Scanner(file);
            while(inputStream.hasNext()){
                String line = inputStream.nextLine();
                String[] values = line.split(";");
                // this adds the currently parsed line to the 2-dimensional string array
                id = Integer.parseInt(values[0]);
            }
            inputStream.close();
            
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return id + 1;
    }

    public static ArrayList<Customer> loadCustomers(){
        String fileName = "user/PhotoShop_Customers.csv";
        File file = new File(fileName);
        ArrayList<Customer> customerContainer = new ArrayList<>();
        Scanner inputStream;

        try{
            inputStream = new Scanner(file);
            while(inputStream.hasNext()){
                String line = inputStream.nextLine();
                String[] values = line.split(";");
                Customer customer = createCustomer(values);
                customerContainer.add(customer);
            }
            inputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return customerContainer;
    }

    @Override
    public String toString(){
        return String.format("%-5s %30s %30s %30s %10s %10s %20s %10s", this.ID, this.firstName, this.lastName, this.address, this.postcode, this.city, this.email, this.mobile);
    }


}
