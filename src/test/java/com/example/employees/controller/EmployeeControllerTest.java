package com.example.employees.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.employees.exception.DuplicateEmployeeException;
import com.example.employees.exception.EmployeeNotFoundException;
import com.example.employees.model.Employee;
import com.example.employees.service.EmployeeService;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    private static final String VALID_BODY =
            "{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane@acme.com\",\"department\":\"Eng\"}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService service;

    @Test
    void getAllReturnsEmployees() throws Exception {
        when(service.getAll()).thenReturn(List.of(new Employee(1L, "Jane", "Doe", "jane@acme.com", "Eng")));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("jane@acme.com"));
    }

    @Test
    void getByIdReturns404WhenMissing() throws Exception {
        when(service.getById(5L)).thenThrow(new EmployeeNotFoundException(5L));

        mockMvc.perform(get("/api/employees/5")).andExpect(status().isNotFound());
    }

    @Test
    void getByIdReturns400ForNonNumericId() throws Exception {
        mockMvc.perform(get("/api/employees/abc")).andExpect(status().isBadRequest());
    }

    @Test
    void postCreatesEmployeeAndReturnsLocation() throws Exception {
        when(service.create(any())).thenReturn(new Employee(1L, "Jane", "Doe", "jane@acme.com", "Eng"));

        mockMvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/employees/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void postReturns400ForInvalidPayload() throws Exception {
        String invalid = "{\"firstName\":\"\",\"lastName\":\"Doe\",\"email\":\"nope\",\"department\":\"Eng\"}";

        mockMvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(invalid))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.firstName").exists())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void postReturns409ForDuplicateEmail() throws Exception {
        when(service.create(any())).thenThrow(new DuplicateEmployeeException("jane@acme.com"));

        mockMvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteReturns204() throws Exception {
        mockMvc.perform(delete("/api/employees/1")).andExpect(status().isNoContent());
    }

    @Test
    void deleteReturns404WhenMissing() throws Exception {
        doThrow(new EmployeeNotFoundException(9L)).when(service).delete(9L);

        mockMvc.perform(delete("/api/employees/9")).andExpect(status().isNotFound());
    }
}
