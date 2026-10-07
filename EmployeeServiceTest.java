package com.bizengine;

import com.bizengine.domain.*;
import com.bizengine.exception.DuplicateEntityException;
import com.bizengine.exception.EntityNotFoundException;
import com.bizengine.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {
    private static final LocalDate PAST = LocalDate.of(2022, 6, 1);
    private EmployeeService service;

    @BeforeEach
    void setUp() throws Exception {
        service = new EmployeeService();
        service.hire(new FullTimeEmployee("E1", "Zoya", Department.ENGINEERING, PAST, new BigDecimal("1200000"))); // 100000/mo
        service.hire(new FullTimeEmployee("E2", "asha", Department.ENGINEERING, PAST, new BigDecimal("600000")));  // 50000/mo
        service.hire(new ContractEmployee("E3", "Ravi", Department.SALES, PAST, new BigDecimal("500"), 100));      // 50000/mo
    }

    @Test
    void duplicateIdIsRejectedWithCheckedException() {
        assertThrows(DuplicateEntityException.class, () ->
                service.hire(new FullTimeEmployee("E1", "Dup", Department.HR, PAST, BigDecimal.TEN)));
    }

    @Test
    void missingEmployeeThrowsNotFound() {
        assertThrows(EntityNotFoundException.class, () -> service.get("NOPE"));
        assertThrows(EntityNotFoundException.class, () -> service.terminate("NOPE"));
    }

    @Test
    void sortedByNameIsCaseInsensitive() {
        List<String> names = service.sortedByName().stream().map(Employee::getName).toList();
        assertEquals(List.of("asha", "Ravi", "Zoya"), names);
    }

    @Test
    void sortedByPayDescPutsHighestFirst() {
        assertEquals("E1", service.sortedByPayDesc().get(0).getId());
    }

    @Test
    void topEarnersLimitsResults() {
        assertEquals(1, service.topEarners(1).size());
        assertEquals("E1", service.topEarners(1).get(0).getId());
    }

    @Test
    void filterWithLambda() {
        List<Employee> contractors = service.findWhere(e -> e instanceof ContractEmployee);
        assertEquals(1, contractors.size());
        assertEquals("E3", contractors.get(0).getId());
    }

    @Test
    void activeDepartmentsIsADistinctSet() {
        Set<Department> depts = service.activeDepartments();
        assertEquals(Set.of(Department.ENGINEERING, Department.SALES), depts);
    }

    @Test
    void payrollAggregatesPerDepartment() {
        Map<Department, BigDecimal> payroll = service.payrollByDepartment();
        assertEquals(new BigDecimal("150000.00"), payroll.get(Department.ENGINEERING));
        assertEquals(new BigDecimal("50000.00"), payroll.get(Department.SALES));
    }

    @Test
    void terminateRemovesEmployee() throws Exception {
        service.terminate("E3");
        assertFalse(service.activeDepartments().contains(Department.SALES));
    }
}
