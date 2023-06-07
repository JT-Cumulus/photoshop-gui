import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import repository.Customer;
import repository.Employee;
import repository.UserHandler;
import service.Catalogue;
import service.Days;
import service.Invoice;
import service.Item;
import service.Order;
import service.ShoppingCart;

public class Main {
    // First instance catalogue so daily catalogue can be loaded in
    static Catalogue catalogue = new Catalogue();
    static ShoppingCart cart = new ShoppingCart();
    static UserHandler users = new UserHandler();
    static Scanner scan = new Scanner(System.in);

    // Tracker for user location in navigation
    static int userLocation = 0;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // Load in Opening Times of the shop
        List<Days> openingTimes = Days.loadDays();
        Days days = new Days();

        // Declare id of employer
        users.displayEmployees();
        System.out.println("Please select your employee ID: ");
        Employee currentEmployee = users.getEmployee(userNavigation());

        // Load daily prices
        catalogue = Item.loadItems(catalogue);

        // Set quit condition for terminating application
        while (userLocation > -1){
            // Main store menu
            shopMenu();

            // Check for user input
            int option = userNavigation();

            if (option == 1) {
                // Add item to shopping cart here
                purchaseMenu(currentEmployee, days, openingTimes);

            } else if (option == 2) {
                // Print order details - provide date for order pickup
                System.out.println("Please enter your order ID: ");
                int orderID = userNavigation();
                checkOrder(orderID, days, openingTimes);

            } else if (option == 3) {
                // Print invoice details / allow user to access order details from invoice no.
                checkInvoice(users, catalogue, days, openingTimes);

            } else if (option == 0) {
                // Exit program condition
                userLocation = -1;
                break;
            }
        }
        
        // Close scanner at end
        scan.close();
    }

    // Function for user navigation
    public static int userNavigation(){
        int userChoice = 0;
        userChoice = scan.nextInt();
        return userChoice;
    }

    // Function displaying the shop menu for user navigation
    public static void shopMenu(){
        String menuWelcomeMessage = "Hello, welcome to PhotoShop! \n" + "Please type a number to navigate the menu \n";
        String menuDivider = "---------------------------";

        System.out.println(menuDivider);
        System.out.print(menuWelcomeMessage);
        System.out.println(menuDivider);
        System.out.println("Press 1 - Make a purchase");
        System.out.println("Press 2 - See order status");
        System.out.println("Press 3 - See invoice details");
        System.out.println("Press 0 - Exit program");
        System.out.println(menuDivider);
    }

    //Function for making purchase
    public static void purchaseMenu(Employee employee, Days days, List<Days> openingTimes) throws FileNotFoundException, IOException{

        String status = "c";
        while (status.equals("c")) {
            catalogue.printCatalogue();
            System.out.print("Please choose a number between 1 - " + catalogue.getLength() + ": ");
            int choice = scan.nextInt();
            Item item = catalogue.getItem(choice);
            cart.addItem(item);
            System.out.print("\nYou have added: " + item.getName());
            System.out.print("\nTo add another item type 'c' ");
            System.out.print("\nTo remove an item type 'v' ");
            System.out.print("\nTo finalise your purchase type 'b': \n ");
            status = scan.next();

            // Check if user wants to remove item
            if (status.equals("v")){
                cart.displayCart();
                System.out.print("Please choose the item to remove: ");
                int choiceRemove = scan.nextInt();
                Item itemRemoved = cart.getIteminCart(catalogue.getItem(choiceRemove));
                cart.removeItem(itemRemoved);
                System.out.println("Your cart now:");
                cart.displayCart();
                status = "c";
            } 

            // Check if user wants to make purchase
            if (status.equals("b")){
                // Confirm the purchase to be made
                cart.displayCart();
                System.out.println("\nAre you sure you with to make this purchase?");
                System.out.println("Type 'y' to confirm and 'n' to go back");
                String confirm = scan.next();

                if (confirm.equals("y")){

                    Customer currentCustomer = customerEntry();
                    cart.confirmPurchase(cart, days, employee, currentCustomer, openingTimes);
                    checkOrder(cart.getOrderID(), days, openingTimes);
                } else {
                    status = "c";
                }
            } 
        }
    }

    // Function for entering customer information
    public static Customer customerEntry(){
        String menuDivider = "---------------------------";

        System.out.println(menuDivider);
        System.out.println("Enter details to complete purchase");
        System.out.println("Press 1 - for return customer");
        System.out.println("Press 2 - for new customer");
        System.out.println("Press 3 - for guest");
        System.out.println(menuDivider);

        int userChoice = userNavigation();
        switch(userChoice){
            case 1:
            users.displayCustomers();
            System.out.println("Please select the customer id: ");
            int option = userNavigation();
            Customer customer = users.getCustomer(option);
            return customer;
            
            case 2:
            Customer newCustomer = users.makeNewCustomer();
            return newCustomer;

            case 3:
            Customer guest = users.getCustomer(1);
            guest.setID(1);
            return guest;
        }

        return null;
    }

    // Function to check an order from its ID
    public static void checkOrder(int orderID, Days days, List<Days> openingTimes) throws FileNotFoundException, IOException{
        Invoice newInvoice = new Invoice();
        Order order = newInvoice.findInvoice(orderID, catalogue);

        order.displayOrder();
        order.displayPickupTime();
        userLocation = 0;
    }

    // Check invoice navigation - prints out the invoice of the order id
    public static void checkInvoice(UserHandler user, Catalogue catalogue, Days days, List<Days> openingTimes) throws FileNotFoundException, IOException{
        Invoice newInvoice = new Invoice();
        System.out.println("Please enter your order ID: ");
        int orderID = userNavigation();
        String[] userInfo = users.getInfo(orderID);
        Order order = newInvoice.findInvoice(orderID, catalogue);

        // Grabs the saved customer and employee using orders.csv
        Customer chosenCustomer = user.getCustomer(Integer.parseInt(userInfo[1]));
        Employee chosenEmployee = user.getEmployee(Integer.parseInt(userInfo[4]));
        String orderDate = userInfo[2];
        
        // New invoice instanced from the retrieved data
        newInvoice = new Invoice(chosenEmployee, chosenCustomer, order);
        newInvoice.displayInvoice(orderDate);
        order.displayOrder();
        order.displayPickupTime();

        userLocation = 0;
    }

}
