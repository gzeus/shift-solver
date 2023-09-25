package com.gzeus.ShiftSolver.rest;

import com.gzeus.ShiftSolver.entity.Holiday;
import com.gzeus.ShiftSolver.service.HolidayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class HolidayRestController {

    private HolidayService holidayService;

    public HolidayRestController(HolidayService holidayService){
        this.holidayService = holidayService;
    }


    // display all holidays for an employee that are later than today
    @GetMapping("/api/employees/{employeeId}/holidays")
    public String findAllById(@PathVariable int employeeId){

        List<Holiday> holidayList = holidayService.findAllForEmployee(employeeId);

        StringBuilder sb = new StringBuilder("Employee with id " + employeeId);

        if (holidayList.size() > 0){
            sb.append(" will be on holidays: \n");
            sb.append("---------------------- \n");

            for (Holiday holiday:holidayList){
                if (holiday.getEndDate().isAfter(LocalDate.now())) {
                    sb.append(holiday.getStartDate()).append(" - ").append(holiday.getEndDate()).append("\n");
                }
            }
        } else {
            sb.append(" has no planned holiday");
        }

        return sb.toString();


    }
}
