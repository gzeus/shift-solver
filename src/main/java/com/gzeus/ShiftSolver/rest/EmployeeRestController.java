package com.gzeus.ShiftSolver.rest;

import com.gzeus.ShiftSolver.service.EmployeeService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeRestController {

    private EmployeeService employeeService;

    public EmployeeRestController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

}
