package com.gzeus.ShiftSolver.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


// Class that temporarily joins data from Employee, Holiday and Shift tables for calculations within the app
// Does not have its own Database table

public class EmployeeHolidayShift extends Employee {

    //
    private static final String DATE_PATTERN = "EEEE dd. MMMM";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);


    // counts total number of shifts in a given month (period) - used for calculations
    private int shiftsInCurrentPeriod;
    private List<LocalDate> holidays;
    private List<LocalDate> shifts;



    public EmployeeHolidayShift(String firstName, String lastName, int id) {
        super(firstName, lastName);
        this.setId(id);
        this.holidays = new ArrayList<>();
        this.shifts = new ArrayList<>();
    }


    public void addHolidays(LocalDate date){
        if (date != null) {
            holidays.add(date);
        }
    }

    public void addHolidays(LocalDate... dates){
        for(LocalDate date: dates){
            addHolidays(date);
        }
    }

    public void addHolidays(List<LocalDate> dates) {
        for(LocalDate date: dates){
            addHolidays(date);
        }
    }

    public void addShift(LocalDate date){
        if (date != null) {
            shifts.add(date);
        }
    }

    public void addShift(LocalDate... dates){
        for(LocalDate date: dates){
            addShift(date);
        }
    }

    public void addShift(List<LocalDate> dates) {
        for(LocalDate date: dates){
            addShift(date);
        }
    }

    public void increaseShiftInCurrentPeriod(){
        shiftsInCurrentPeriod++;
    }

    public boolean isOnHoliday(LocalDate date){
        return holidays.contains(date);

    }
    public int getShiftsInCurrentPeriod() {
        return shiftsInCurrentPeriod;
    }

    public String getFullName(){
        return getLastName() + " " + getFirstName();
    }

    public List<LocalDate> getShifts() {
        return shifts;
    }

    public List<LocalDate> getHolidays(){
        return holidays;
    }

    public void printHolidays(){

        StringBuilder sb = new StringBuilder(getFullName());
        sb.append("'s holiday list\n");
        sb.append("----------------\n");

        for (LocalDate date: holidays){
            sb.append(dateTimeFormatter.format(date));
            sb.append("\n");

        }

        System.out.println(sb);

    }

    @Override
    public String toString() {
        return "EmployeeHolidayShift{" +
                "shiftsInCurrentPeriod=" + shiftsInCurrentPeriod +
                ", holidays=" + holidays +
                ", shifts=" + shifts +
                "} " + super.toString();
    }
}
