package com.example.employees.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.employees.dto.EmployeeRequest;
import com.example.employees.exception.DuplicateEmployeeException;
import com.example.employees.exception.EmployeeNotFoundException;
import com.example.employees.model.Employee;
import com.example.employees.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Employee> getAll() {
        return repository.findAll();
    }

    @Override
    public Employee getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Override
    public Employee create(EmployeeRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateEmployeeException(request.email());
        }
        Employee employee = new Employee(null, request.firstName().trim(), request.lastName().trim(),
                request.email().trim(), request.department().trim());
        return repository.save(employee);
    }

    @Override
    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new EmployeeNotFoundException(id);
        }
    }
}
