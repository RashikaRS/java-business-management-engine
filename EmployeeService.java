package com.bizengine.service;

import com.bizengine.domain.Department;
import com.bizengine.domain.Employee;
import com.bizengine.exception.DuplicateEntityException;
import com.bizengine.exception.EntityNotFoundException;
import com.bizengine.repository.InMemoryRepository;
import com.bizengine.repository.Repository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EmployeeService {
    private final Repository<Employee, String> repo = new InMemoryRepository<>(Employee::getId);

    public void hire(Employee employee) throws DuplicateEntityException {
        repo.add(employee);
    }

    public Employee get(String id) throws EntityNotFoundException {
        return repo.getById(id);
    }

    public void terminate(String id) throws EntityNotFoundException {
        if (!repo.remove(id)) throw new EntityNotFoundException(id);
    }

    public List<Employee> findWhere(Predicate<Employee> predicate) {
        return CollectionUtils.filter(repo.findAll(), predicate);
    }

    public List<Employee> sortedByPayDesc() {
        return CollectionUtils.sorted(repo.findAll(), Comparator.comparing(Employee::monthlyPay).reversed());
    }

    public List<Employee> sortedByName() {
        return CollectionUtils.sorted(repo.findAll(), Comparator.comparing(Employee::getName, String.CASE_INSENSITIVE_ORDER));
    }

    public List<Employee> topEarners(int n) {
        return CollectionUtils.topN(repo.findAll(), Employee::monthlyPay, n);
    }

    /** Set: distinct departments currently staffed, naturally ordered. */
    public Set<Department> activeDepartments() {
        return repo.findAll().stream().map(Employee::getDepartment).collect(Collectors.toCollection(TreeSet::new));
    }

    /** Map: total monthly payroll per department. */
    public Map<Department, BigDecimal> payrollByDepartment() {
        return repo.findAll().stream().collect(Collectors.toMap(
                Employee::getDepartment,
                Employee::monthlyPay,
                BigDecimal::add,
                () -> new EnumMap<>(Department.class)));
    }

    public Map<Department, List<Employee>> byDepartment() {
        return CollectionUtils.groupBy(repo.findAll(), Employee::getDepartment);
    }
}
