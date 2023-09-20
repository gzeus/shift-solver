package com.gzeus.ShiftSolver.dao;

import com.gzeus.ShiftSolver.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Integer> {


    @Query(value = "SELECT * FROM holidays WHERE employee_id = ?1 ORDER BY start_date", nativeQuery = true)
    List<Holiday> findAllForEmployee(int employee_id);



}
