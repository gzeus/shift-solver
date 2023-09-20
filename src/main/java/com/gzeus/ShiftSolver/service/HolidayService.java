package com.gzeus.ShiftSolver.service;

import com.gzeus.ShiftSolver.entity.Holiday;

import java.util.List;

public interface HolidayService {


    List<Holiday> findAll();


    List<Holiday> findAllForEmployee(int employeeId);

    Holiday findById(int theId);

    Holiday save(Holiday holiday);

    void deleteById(int theId);

}
