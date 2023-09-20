package com.gzeus.ShiftSolver.dao;

import com.gzeus.ShiftSolver.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, LocalDate> {

    @Query(value = "SELECT * FROM shifts WHERE employee_id = ?1 ORDER BY date", nativeQuery = true)
    List<Shift> findAllForEmployee(int employee_id);

    @Query(value = "SELECT * FROM shifts WHERE date BETWEEN ?1 AND ?2 ORDER BY date", nativeQuery = true)
    List<Shift> findAllBetweenDates(LocalDate startDate, LocalDate endDate);





}
