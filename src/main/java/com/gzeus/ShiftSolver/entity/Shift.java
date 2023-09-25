package com.gzeus.ShiftSolver.entity;

import jakarta.persistence.*;

import java.time.LocalDate;


// class for holding individual shifts assigned to employees. Only 1 shift per day, for simplicity
@Entity
@Table(name="shifts")
public class Shift {

    @Id
    @Column(name = "date")
    private LocalDate date;

    @Column(name="employee_id")
    private int employeeId;

    public Shift() {
    }

    public Shift(LocalDate date, int employeeId) {
        this.date = date;
        this.employeeId = employeeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
