package com.gzeus.ShiftSolver.rest;

import com.gzeus.ShiftSolver.entity.*;
import com.gzeus.ShiftSolver.service.EmployeeService;
import com.gzeus.ShiftSolver.service.HolidayService;
import com.gzeus.ShiftSolver.service.ShiftService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ShiftRestController {

    private ShiftService shiftService;
    private HolidayService holidayService;
    private EmployeeService employeeService;


    public ShiftRestController(ShiftService shiftService, HolidayService holidayService, EmployeeService employeeService) {
        this.shiftService = shiftService;
        this.holidayService = holidayService;
        this.employeeService = employeeService;
    }

    // Get all existing shifts for the given month
    @GetMapping("/api/shifts/{year}/{month}")
    public List<Shift> getShifts(@PathVariable int year, @PathVariable int month) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month + 1, 1).minusDays(1);

        return shiftService.findAllBetweenDates(startDate, endDate);

    }

    /*
    Post request with following JSON body to generate shifts for the given month.
    ---- following values are all optional
    "publicHolidays" - Date[] (List of public holidays with no shifts)
    "weekdaysToExclude" - int[] (What weekdays have no shifts every week. 1 - Monday, 7 - Sunday)
    "recalculate" - boolean (if true, deletes all previous shifts in the month from the database and calculates again)
*/
    @PostMapping("/api/shifts/{year}/{month}")
    public List<EmployeeHolidayShift> generateAllShifts(@PathVariable int year, @PathVariable int month, @RequestBody Map<String, Object> payload) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1);

        List<EmployeeHolidayShift> employeeHolidayShifts = new ArrayList<>();

        // if "recalculate" is posted, first delete all existing shifts in the month

        if (payload.containsKey("recalculate") && (Boolean) payload.get("recalculate") == true) {

            System.out.println("Deleting existing shifts for month starting on: " + startDate);
            startDate.datesUntil(endDate)
                    .forEach(s -> shiftService.deleteById(s));
        }

        // Find all employees from database, and for each their holidays and existing shifts
        // and generate EmployeeHolidayShifts objects that hold all their shifts and holidays

        List<Employee> employees = employeeService.findAll();
        List<Holiday> employeeHoliday = new ArrayList<>();
        List<Shift> employeeShift = new ArrayList<>();

        for (Employee employee : employees) {

            employeeHoliday = holidayService.findAllForEmployee(employee.getId());
            employeeShift = shiftService.findAllForEmployee(employee.getId());
            EmployeeHolidayShift ehs = new EmployeeHolidayShift(employee.getFirstName(), employee.getLastName(), employee.getId());

            // put all holidays from the database to the EmployeeHolidayShift object
            for (Holiday holiday : employeeHoliday) {

                ehs.addHolidays(
                        holiday.getStartDate().datesUntil(holiday.getEndDate().plusDays(1))
                                .collect(Collectors.toList())
                );
            }
            //  put all shifts from the database to the EmployeeHolidayShift object
            for (Shift shift : employeeShift) {
                ehs.addShift(shift.getDate());
            }
            employeeHolidayShifts.add(ehs);
        }

        // parse all holidays and weekdays to Exclude from the HTTP request JSON body
        List<LocalDate> holidays = new ArrayList<>();
        List<Integer> weekdaysToExclude = new ArrayList<>();

        if (payload.get("publicHolidays") != null) {

            for (var el : (ArrayList<String>) payload.get("publicHolidays")) {
                holidays.add(LocalDate.parse(el));
            }
        }

        if (payload.get("weekdaysToExclude") != null) {
            for (var el : (ArrayList<Integer>) payload.get("weekdaysToExclude")) {
                weekdaysToExclude.add((Integer) el);
            }
        }

        // create the WorkSchedule object that provides tha calculations of shifts based on EmployeeHolidayShift objects

        WorkSchedule schedule = new WorkSchedule(startDate, endDate, holidays, weekdaysToExclude);
        schedule.setEmployees(employeeHolidayShifts);

        // calculate the shifts and print the schedule into console
        schedule.printSchedule();

        // save individual shifts into database
        for (var entry : schedule.getSchedule().entrySet()) {
            // if the mapped date employee_id was undefined (null), save it in the database as -1 to mark it was holidays
            if (entry.getValue() == null) {
                shiftService.save(new Shift(entry.getKey(), -1));
            } else {
                shiftService.save(new Shift(entry.getKey(), entry.getValue().getId()));
            }
        }

        // returns a list of all Employees and their shifts and holidays in the given month
        return schedule.getEmployees();

    }


    // get all shifts for a given employee

    @GetMapping("/api/employees/{employeeId}/shifts")
    public String findAllById(@PathVariable int employeeId) {

        List<Shift> shiftList = shiftService.findAllForEmployee(employeeId);
        Employee employee = employeeService.findById(employeeId);

        StringBuilder sb = new StringBuilder("Employee with the id of " + employeeId);
        sb.append(" (");
        sb.append(employee.getFirstName()).append(" ").append(employee.getLastName());
        sb.append(")");

        if (!shiftList.isEmpty()) {
            sb.append(" has the following shifts: \n");
            sb.append("---------------------- \n");

            for (Shift shift : shiftList) {
                if (shift.getDate().isAfter(LocalDate.now())) {
                    sb.append(shift.getDate()).append("\n");
                }
            }

        } else {
            sb.append(" has no planned shifts");
        }

        return sb.toString();
    }
}
