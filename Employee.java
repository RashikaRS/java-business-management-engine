package com.bizengine.domain;

import com.bizengine.exception.InvalidBusinessDataException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/** Abstract base: shared state is encapsulated, pay calculation is left to subclasses. */
public abstract class Employee implements Comparable<Employee> {
    private final String id;
    private String name;
    private Department department;
    private final LocalDate hireDate;

    protected Employee(String id, String name, Department department, LocalDate hireDate) {
        if (id == null || id.isBlank()) throw new InvalidBusinessDataException("Employee id is required");
        if (hireDate == null || hireDate.isAfter(LocalDate.now()))
            throw new InvalidBusinessDataException("Hire date cannot be in the future");
        this.id = id;
        this.hireDate = hireDate;
        setName(name);
        setDepartment(department);
    }

    /** Polymorphic: each employee type computes pay differently. */
    public abstract BigDecimal monthlyPay();

    public String getId() { return id; }
    public String getName() { return name; }
    public Department getDepartment() { return department; }
    public LocalDate getHireDate() { return hireDate; }

    public void setName(String name) {
        if (name == null || name.isBlank()) throw new InvalidBusinessDataException("Employee name is required");
        this.name = name.trim();
    }

    public void setDepartment(Department department) {
        this.department = Objects.requireNonNull(department, "department");
    }

    @Override
    public int compareTo(Employee other) {
        return this.id.compareTo(other.id);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Employee e && id.equals(e.id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + id + ", " + name + ", " + department + ", pay=" + monthlyPay() + "]";
    }
}
