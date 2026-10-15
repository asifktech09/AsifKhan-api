package com.example.employees.model;

/** Immutable domain object representing an employee. */
public record Employee(Long id, String firstName, String lastName, String email, String department) {

    public Employee withId(Long newId) {
        return new Employee(newId, firstName, lastName, email, department);
    }
}
