package com.example.employees.service;

import java.util.List;

import com.example.employees.dto.EmployeeRequest;
import com.example.employees.model.Employee;

public interface EmployeeService {
    List<Employee> getAll();

    Employee getById(Long id);

    Employee create(EmployeeRequest request);

    void delete(Long id);
}
