package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import static java.time.temporal.ChronoUnit.MINUTES;

// Class responsible for displaying time remaining until order is complete
public class Days {
    private int dayNumber;
    private String day;
    private LocalTime openFrom;
    private LocalTime openTill;
    private Long workingMinutes;

    public Days(int dayNumber, String day, LocalTime openFrom, LocalTime openTill) {
        this.dayNumber = dayNumber;
        this.day = day;
        this.openFrom = openFrom;
        this.openTill = openTill;
        this.workingMinutes = MINUTES.between(openFrom, openTill);
    }

    public Days(){}

    public int getDayNumber() {
        return this.dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocalTime getOpenFrom() {
        return this.openFrom;
    }

    public void setOpenFrom(LocalTime openFrom) {
        this.openFrom = openFrom;
    }

    public LocalTime getOpenTill() {
        return this.openTill;
    }

    public void setOpenTill(LocalTime openTill) {
        this.openTill = openTill;
    }

    public Long getWorkingMinutes() {
        return this.workingMinutes;
    }

    public void setWorkingMinutes(Long workingMinutes) {
        this.workingMinutes = workingMinutes;
    }
    
    // Create a day object, used for loading in the csv
    private static Days createDay(String[] metadata) {
        int dayNumber = Integer.parseInt(metadata[0]);
        String name = metadata[1];
        LocalTime openFrom = LocalTime.parse(metadata[2]);
        LocalTime openTill = LocalTime.parse(metadata[3]);

        return new Days(dayNumber, name, openFrom, openTill);
    }

    // Function for loading the opening hours csv
    public static List<Days> loadDays(){
        List<Days> openingsTimes = new LinkedList<Days>();
        String fileName= "database/PhotoShop_OpeningHours.csv";
        File file= new File(fileName);

        // this gives you a 2-dimensional array of strings
        Scanner inputStream;

        try{
            inputStream = new Scanner(file);
            // Skip the header 
            inputStream.nextLine();

            while(inputStream.hasNext()){
                String line = inputStream.nextLine();
                String[] values = line.split(";");
                // this adds the currently parsed line to the 2-dimensional string array
                Days day = Days.createDay(values);
                openingsTimes.add(day);
            }

            inputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return openingsTimes;
    }

    // Prints out the opening time stored in the days
    public void displayOpeningTimes(List<Days> days){
        System.out.println(String.format("%-30s %15s %15s" , "Day", "Open From", "Open Till"));
        for(Days current: days){
            System.out.println(current.toString());
        }
    }

    // Return the day of the week as an int
    public int calculateDayOfWeek(){
        Calendar c = Calendar.getInstance();
        c.setTime(c.getTime());
        return c.get(Calendar.DAY_OF_WEEK) - 1;
    }

    // Function to iterate over days and calculate time taken till pickup
    public LocalDateTime calculatePickup(Long totalWorkDuration, List<Days> openingTimes){
        LocalDateTime timeNow = LocalDateTime.now();
        long workDuration = totalWorkDuration;
        Days currentDay = openingTimes.get(this.calculateDayOfWeek());

        // get time left in the day until closing hour and return if order can be completed today
        long timeLeft = timeNow.toLocalTime().until(currentDay.getOpenTill(), MINUTES);
        if (timeLeft > workDuration) {
            return timeNow.plus(workDuration, MINUTES);
        }

        // check if its a saturday
        timeNow = timeNow.plus(timeLeft, MINUTES);
        workDuration -= timeLeft;
        if (currentDay.getDayNumber() == 6) {
            currentDay = openingTimes.get(1);
            timeNow = timeNow.plusDays(1);
        }

        // change day to new day
        currentDay = openingTimes.get(currentDay.getDayNumber());
        timeNow = timeNow.plusDays(1);

        // loop until workduration ticks down to nothing
        while(workDuration > 0) {
            long dailyWorkDuration = currentDay.getWorkingMinutes();
            timeNow = timeNow.withHour(9);
            timeNow = timeNow.withMinute(0);

            if (dailyWorkDuration > workDuration) {
                timeNow = timeNow.plus(workDuration, MINUTES);
                break;
            }

            workDuration -= dailyWorkDuration;

            if (currentDay.getDayNumber() == 7) {
                currentDay = openingTimes.get(1);
                timeNow = timeNow.plusDays(1);
            } else {
                currentDay = openingTimes.get(currentDay.getDayNumber());
                timeNow = timeNow.plusDays(1);
            }
        }
        
        return timeNow;
    }

    
    public String toString(){
        return String.format("%-30s %15s %15s" , this.day, this.openFrom, this.openTill );
    }

}
