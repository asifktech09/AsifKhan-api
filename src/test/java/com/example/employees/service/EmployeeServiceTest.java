package com.example.employees.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.employees.dto.EmployeeRequest;
import com.example.employees.exception.DuplicateEmployeeException;
import com.example.employees.exception.EmployeeNotFoundException;
import com.example.employees.model.Employee;
import com.example.employees.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeServiceImpl service;

    private final EmployeeRequest request = new EmployeeRequest("Jane", "Doe", "jane@acme.com", "Engineering");

    @Test
    void createSavesEmployee() {
        when(repository.existsByEmail("jane@acme.com")).thenReturn(false);
        when(repository.save(any(Employee.class)))
                .thenReturn(new Employee(1L, "Jane", "Doe", "jane@acme.com", "Engineering"));

        Employee result = service.create(request);

        assertEquals(1L, result.id());
        assertEquals("Jane", result.firstName());
    }

    @Test
    void createRejectsDuplicateEmail() {
        when(repository.existsByEmail("jane@acme.com")).thenReturn(true);

        assertThrows(DuplicateEmployeeException.class, () -> service.create(request));
        verify(repository, never()).save(any());
    }

    @Test
    void getByIdThrowsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void deleteThrowsWhenMissing() {
        when(repository.deleteById(99L)).thenReturn(false);

        assertThrows(EmployeeNotFoundException.class, () -> service.delete(99L));
    }
}
