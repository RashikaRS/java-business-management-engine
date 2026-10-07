package com.bizengine;

import com.bizengine.domain.*;
import com.bizengine.exception.BusinessException;
import com.bizengine.service.EmployeeService;
import com.bizengine.service.InventoryService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        EmployeeService hr = new EmployeeService();
        InventoryService inv = new InventoryService();
        try {
            hr.hire(new FullTimeEmployee("E1", "Asha", Department.ENGINEERING, LocalDate.of(2022, 1, 10), new BigDecimal("960000")));
            hr.hire(new ContractEmployee("E2", "Ravi", Department.SALES, LocalDate.of(2024, 3, 1), new BigDecimal("900"), 120));
            System.out.println("Payroll: " + hr.payrollByDepartment());
            System.out.println("Top earner: " + hr.topEarners(1));

            inv.addProduct(new Product("P1", "Keyboard", new BigDecimal("1499.00"), 10));
            System.out.println(inv.placeOrder("Acme", Map.of("P1", 3)));
            inv.placeOrder("Acme", Map.of("P1", 50)); // triggers checked exception
        } catch (BusinessException e) {
            System.out.println("Business rule failed: " + e.getMessage());
        }
    }
}
