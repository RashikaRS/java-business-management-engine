package com.bizengine.domain;

import com.bizengine.exception.InvalidBusinessDataException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class ContractEmployee extends Employee {
    private final BigDecimal hourlyRate;
    private int hoursWorked;

    public ContractEmployee(String id, String name, Department dept, LocalDate hireDate,
                            BigDecimal hourlyRate, int hoursWorked) {
        super(id, name, dept, hireDate);
        if (hourlyRate == null || hourlyRate.signum() <= 0)
            throw new InvalidBusinessDataException("Hourly rate must be positive");
        this.hourlyRate = hourlyRate;
        logHours(hoursWorked);
    }

    public BigDecimal getHourlyRate() { return hourlyRate; }
    public int getHoursWorked() { return hoursWorked; }

    /** Replaces the hours for the current pay period. */
    public void logHours(int hours) {
        if (hours < 0 || hours > 744) throw new InvalidBusinessDataException("Hours must be between 0 and 744");
        this.hoursWorked = hours;
    }

    @Override
    public BigDecimal monthlyPay() {
        return hourlyRate.multiply(BigDecimal.valueOf(hoursWorked)).setScale(2, RoundingMode.HALF_UP);
    }
}
