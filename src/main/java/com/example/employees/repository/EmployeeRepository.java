package com.example.employees.repository;

import java.util.List;
import java.util.Optional;

import com.example.employees.model.Employee;

public interface EmployeeRepository {
    List<Employee> findAll();

    Optional<Employee> findById(Long id);

    boolean existsByEmail(String email);

    Employee save(Employee employee);

    /** @return true if an employee was removed, false if none existed with that id */
    boolean deleteById(Long id);

    void deleteAll();
}
