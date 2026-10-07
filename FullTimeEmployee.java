package com.bizengine.domain;

import com.bizengine.exception.InvalidBusinessDataException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class FullTimeEmployee extends Employee {
    private BigDecimal annualSalary;

    public FullTimeEmployee(String id, String name, Department dept, LocalDate hireDate, BigDecimal annualSalary) {
        super(id, name, dept, hireDate);
        setAnnualSalary(annualSalary);
    }

    public BigDecimal getAnnualSalary() { return annualSalary; }

    public void setAnnualSalary(BigDecimal annualSalary) {
        if (annualSalary == null || annualSalary.signum() <= 0)
            throw new InvalidBusinessDataException("Annual salary must be positive");
        this.annualSalary = annualSalary;
    }

    @Override
    public BigDecimal monthlyPay() {
        return annualSalary.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }
}
