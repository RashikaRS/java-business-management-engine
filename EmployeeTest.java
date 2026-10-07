package com.bizengine;

import com.bizengine.domain.*;
import com.bizengine.exception.InvalidBusinessDataException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
    private static final LocalDate PAST = LocalDate.of(2023, 1, 1);

    @Test
    void fullTimeMonthlyPayIsSalaryDividedByTwelve() {
        Employee e = new FullTimeEmployee("E1", "Asha", Department.HR, PAST, new BigDecimal("120000"));
        assertEquals(new BigDecimal("10000.00"), e.monthlyPay());
    }

    @Test
    void contractPayIsRateTimesHours() {
        Employee e = new ContractEmployee("E2", "Ravi", Department.SALES, PAST, new BigDecimal("500"), 40);
        assertEquals(new BigDecimal("20000.00"), e.monthlyPay());
    }

    @Test
    void polymorphismWorksThroughBaseType() {
        Employee[] staff = {
                new FullTimeEmployee("E1", "A", Department.HR, PAST, new BigDecimal("12000")),
                new ContractEmployee("E2", "B", Department.HR, PAST, new BigDecimal("10"), 10)
        };
        assertEquals(new BigDecimal("1000.00"), staff[0].monthlyPay());
        assertEquals(new BigDecimal("100.00"), staff[1].monthlyPay());
    }

    @Test
    void blankNameRejected() {
        assertThrows(InvalidBusinessDataException.class,
                () -> new FullTimeEmployee("E1", " ", Department.HR, PAST, BigDecimal.TEN));
    }

    @Test
    void futureHireDateRejected() {
        assertThrows(InvalidBusinessDataException.class,
                () -> new FullTimeEmployee("E1", "A", Department.HR, LocalDate.now().plusDays(1), BigDecimal.TEN));
    }

    @Test
    void nonPositiveSalaryRejected() {
        assertThrows(InvalidBusinessDataException.class,
                () -> new FullTimeEmployee("E1", "A", Department.HR, PAST, BigDecimal.ZERO));
    }

    @Test
    void impossibleContractHoursRejected() {
        Employee e = new ContractEmployee("E2", "B", Department.HR, PAST, BigDecimal.TEN, 10);
        assertThrows(InvalidBusinessDataException.class, () -> ((ContractEmployee) e).logHours(1000));
    }

    @Test
    void equalityIsBasedOnId() {
        Employee a = new FullTimeEmployee("E1", "A", Department.HR, PAST, BigDecimal.TEN);
        Employee b = new ContractEmployee("E1", "B", Department.SALES, PAST, BigDecimal.ONE, 1);
        assertEquals(a, b);
    }
}
