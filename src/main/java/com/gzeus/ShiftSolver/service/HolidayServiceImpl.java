package com.gzeus.ShiftSolver.service;

import com.gzeus.ShiftSolver.dao.HolidayRepository;
import com.gzeus.ShiftSolver.entity.Holiday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HolidayServiceImpl implements HolidayService{

    private HolidayRepository holidayRepository;

    @Autowired
    public HolidayServiceImpl(HolidayRepository holidayRepository){
        this.holidayRepository = holidayRepository;
    }
    @Override
    public List<Holiday> findAll() {
        return holidayRepository.findAll();
    }

    @Override
    public List<Holiday> findAllForEmployee(int employeeId) {
        return holidayRepository.findAllForEmployee(employeeId);
    }

    @Override
    public Holiday findById(int theId) {
        Optional<Holiday> result = holidayRepository.findById(theId);

        Holiday holiday = null;

        if (result.isPresent()){
            holiday =result.get();
        } else {
            throw new ResourceNotFoundException("Did not find holiday id: " + theId);
        }

        return holiday;
    }

    @Override
    public Holiday save(Holiday holiday) {
        return holidayRepository.save(holiday);
    }

    @Override
    public void deleteById(int theId) {
        holidayRepository.deleteById(theId);
    }
}
