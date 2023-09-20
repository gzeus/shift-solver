package com.gzeus.ShiftSolver.service;

import com.gzeus.ShiftSolver.entity.Shift;

import java.time.LocalDate;
import java.util.List;

public interface ShiftService {


    List<Shift> findAll();


    List<Shift> findAllForEmployee(int employeeId);

    Shift findById(LocalDate theId);

    Shift save(Shift shift);

    void deleteById(LocalDate theId);

    List<Shift> findAllBetweenDates(LocalDate startDate, LocalDate endDate);


}
