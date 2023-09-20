package com.gzeus.ShiftSolver.service;

import com.gzeus.ShiftSolver.dao.ShiftRepository;
import com.gzeus.ShiftSolver.entity.Shift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ShiftServiceImpl implements ShiftService{


    private ShiftRepository shiftRepository;

    @Autowired
    public ShiftServiceImpl(ShiftRepository shiftRepository){
        this.shiftRepository = shiftRepository;
    }

    @Override
    public List<Shift> findAll() {
        return shiftRepository.findAll();
    }

    @Override
    public List<Shift> findAllForEmployee(int employeeId) {
        return shiftRepository.findAllForEmployee(employeeId);
    }

    @Override
    public Shift findById(LocalDate theId) {
        Optional<Shift> result = shiftRepository.findById(theId);

        Shift shift = null;

        if (result.isPresent()){
            shift =result.get();
        } else {
            throw new RuntimeException("Did not find shift date: " + theId);
        }

        return shift;
    }

    @Override
    public Shift save(Shift shift) {
        return shiftRepository.save(shift);
    }

    @Override
    public void deleteById(LocalDate theId) {
        shiftRepository.deleteById(theId);

    }

    @Override
    public List<Shift> findAllBetweenDates(LocalDate startDate, LocalDate endDate) {
        return shiftRepository.findAllBetweenDates(startDate, endDate);
    }
}
