package com.gzeus.ShiftSolver.service;

import com.gzeus.ShiftSolver.entity.Employee;

import java.util.List;


public interface EmployeeService {


    List<Employee> findAll();


    List<Employee> findByFirstName(String firstName);

    Employee findById(int theId);

    Employee save(Employee theEmployee);

    void deleteById(int theId);



}
