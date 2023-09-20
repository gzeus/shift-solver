package com.gzeus.ShiftSolver.rest;

import com.gzeus.ShiftSolver.entity.Employee;
import com.gzeus.ShiftSolver.service.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class EmployeeRestController {

    private EmployeeService employeeService;

    public EmployeeRestController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String getHome(){
        return "Hello World!";
    }



    @GetMapping("/api/employees/names/{name}")
    public Collection<Employee> findByFirstName(@PathVariable String name){

            return  employeeService.findByFirstName(name);
    }



}
