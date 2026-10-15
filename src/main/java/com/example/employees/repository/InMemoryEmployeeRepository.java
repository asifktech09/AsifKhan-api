package com.example.employees.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.employees.model.Employee;

/** Thread-safe repository backed by a static in-memory collection (no database). */
@Repository
public class InMemoryEmployeeRepository implements EmployeeRepository {

    private static final Map<Long, Employee> STORE = new ConcurrentHashMap<>();
    private static final AtomicLong ID_SEQUENCE = new AtomicLong();

    @Override
    public List<Employee> findAll() {
        return STORE.values().stream().sorted(Comparator.comparing(Employee::id)).toList();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(STORE.get(id));
    }

    @Override
    public boolean existsByEmail(String email) {
        return STORE.values().stream().anyMatch(e -> e.email().equalsIgnoreCase(email));
    }

    @Override
    public Employee save(Employee employee) {
        Employee toSave = employee.id() == null ? employee.withId(ID_SEQUENCE.incrementAndGet()) : employee;
        STORE.put(toSave.id(), toSave);
        return toSave;
    }

    @Override
    public boolean deleteById(Long id) {
        return STORE.remove(id) != null;
    }

    @Override
    public void deleteAll() {
        STORE.clear();
    }
}
