package repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Employee{

    private int employeeId;
    private String firstName;
    private String lastName; 

    public Employee(int employeeId, String firstName, String lastName) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Employee(Employee source){
        this.employeeId = source.employeeId;
        this.firstName = source.firstName;
        this.lastName = source.lastName;
    }

    private static Employee createEmployee(String[] metadata) {
        int id = Integer.parseInt(metadata[0]);
        String firstName = metadata[1];
        String lastName = metadata[2];

        return new Employee(id, firstName, lastName);
    }

    public static ArrayList<Employee> loadEmployees(){
        String fileName = "user/PhotoShop_Employees.csv";
        File file = new File(fileName);
        ArrayList<Employee> EmployeeContainer = new ArrayList<>();
        Scanner inputStream;

        try{
            inputStream = new Scanner(file);
            while(inputStream.hasNext()){
                String line = inputStream.nextLine();
                String[] values = line.split(";");
                Employee Employee = createEmployee(values);
                EmployeeContainer.add(Employee);
            }
            inputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return EmployeeContainer;
    }

    @Override
    public String toString(){
        return String.format("%-5s %30s %30s ", this.employeeId, this.firstName, this.lastName);
    }

    public int getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
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


}
