package com.gzeus.ShiftSolver.dao;

import com.gzeus.ShiftSolver.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query(value = "SELECT * FROM employees WHERE first_name = ?1", nativeQuery = true)
    List<Employee> findByFirstName(String first_name);


}
