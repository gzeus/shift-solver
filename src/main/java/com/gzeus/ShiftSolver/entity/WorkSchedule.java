package com.gzeus.ShiftSolver.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


// class for calculating the work schedule. Method accessed through the ShiftRepository
public class WorkSchedule {


    private LocalDate startDate;
    private LocalDate endDate;
    private Set<Integer> weekdaysToExclude;
    private List<LocalDate> holidays = new ArrayList<>();
    private final String DATE_PATTERN = "EEEE dd. MMMM";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

    private int highestNumberOfShifts = 0;

    private int totalNumberOfShifts;


    private List<EmployeeHolidayShift> employees;

    private TreeMap<LocalDate, EmployeeHolidayShift> schedule;

    public WorkSchedule(LocalDate startDate, LocalDate endDate, List<LocalDate> holidays, List<Integer> weekdaysToExclude) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.weekdaysToExclude = Set.copyOf(weekdaysToExclude);
        this.holidays = holidays;
        this.employees = new ArrayList<>();
        this.schedule = new TreeMap<>();

    }

    public WorkSchedule(LocalDate startDate, LocalDate endDate, List<LocalDate> holidays) {
        this(startDate, endDate, holidays, new ArrayList<>());
    }

    public WorkSchedule(LocalDate startDate, LocalDate endDate) {
        this(startDate, endDate, new ArrayList<>(), new ArrayList<>());
    }

    public List<EmployeeHolidayShift> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeHolidayShift> employees) {
        this.employees = employees;
    }


    private List<LocalDate> workDates = new ArrayList<>();
  /*  startDate.datesUntil(endDate)
            .filter(s -> weekdaysToExclude.contains(s.getDayOfWeek().getValue()))
            .collect(Collectors.toSet());*/


    public TreeMap<LocalDate, EmployeeHolidayShift> calculateSchedule() {

        workDates = startDate.datesUntil(endDate)
                .filter(s -> !weekdaysToExclude.contains(s.getDayOfWeek().getValue()))
                .collect(Collectors.toList());


        List<EmployeeHolidayShift> shuffledEmployees = new ArrayList<>(List.copyOf(employees));

        Collections.shuffle(shuffledEmployees);

        holidays.forEach(s -> schedule.put(s, null));
        System.out.println("Schedule after inserting holidays: " + schedule);

        for (int i = 0, j = 0; i < workDates.size(); i++, j++) {
            while (!checkAndPutEmployeeOnShift(workDates.get(i), shuffledEmployees.get(j % shuffledEmployees.size()))) {
                j++;
            }
        }


        return schedule;

    }

    private boolean checkAndPutEmployeeOnShift(LocalDate date, EmployeeHolidayShift employee) {
        if (!employee.isOnHoliday(date)
                && employee.getShiftsInCurrentPeriod() < getAvgNumberOfShifts() + 2)

//                && employee.getShiftsInCurrentPeriod() < (getHighestNumberOfShifts() + 1))
        {

            if (!schedule.containsKey(date)) {
                if (schedule.putIfAbsent(date, employee) == null) {

                    System.out.println("Putting " + employee.getFullName() + " on shift on: " + date);
                    employee.increaseShiftInCurrentPeriod();
                    employee.addShift(date);
                    highestNumberOfShifts = Math.max(employee.getShiftsInCurrentPeriod(), highestNumberOfShifts);
                    totalNumberOfShifts++;
                }
            }

            return true;
        }


        return false;
    }

    private int getHighestNumberOfShifts() {

        return highestNumberOfShifts;
    }

    private int getAvgNumberOfShifts() {

        if (employees.isEmpty()) {
            return 0;
        }
        return totalNumberOfShifts / employees.size();
    }

    // print formatted shift schedule into console
    public void printSchedule() {

        calculateSchedule();

        schedule.forEach((date, employee) ->
        {
            if (employee == null) {

                System.out.printf("%-20s: Public Holiday%n", dateTimeFormatter.format(date));
            } else {

                System.out.printf("%-20s: %s%n", dateTimeFormatter.format(date), employee.getFullName());
            }

        });
        System.out.println("-------------------");
        System.out.println("Number of shifts in a given month for each employee: ");
        System.out.println("-------------------");

        employees.forEach(s -> {
            System.out.printf("%-20s : %d%n", s.getFullName(), s.getShiftsInCurrentPeriod());
        });

        var totalShifts = schedule.values().stream()
                .filter(Objects::nonNull)
                .count();

        System.out.println("-------------------");
        System.out.printf("Total number of assigned shifts in the given month: %d%n", totalShifts);
        System.out.println("-------------------");
    }

    public void manuallyAddShifts(Map<LocalDate, EmployeeHolidayShift> shifts) {

        shifts.forEach((date, employee) -> {

                    schedule.put(date, employee);
                    employee.increaseShiftInCurrentPeriod();

                }
        );


    }


    public TreeMap<LocalDate, EmployeeHolidayShift> getSchedule() {
        return schedule;
    }
}
