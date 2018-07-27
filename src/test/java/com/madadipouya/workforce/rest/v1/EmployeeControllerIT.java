package com.madadipouya.workforce.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madadipouya.workforce.model.Employee;
import com.madadipouya.workforce.service.EmployeeService;
import com.madadipouya.workforce.service.exception.DuplicateEmailAddressException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Mock MVC tests. Simpliefied/lighweighted to test all corner cases slice test
 */
//@TestPropertySource(locations = {"classpath:test.properties"})
//@WithMockUser(username = "test", password = "test", roles = "USER")
@RunWith(SpringRunner.class)
//@ContextConfiguration
@AutoConfigureMockMvc(secure = false)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void testCreateEmployee() throws Exception {
        Employee employee =
                createEmployee("test", "1990-12-20", Arrays.asList("Swimming", "Running"));
        given(employeeService.createEmployee(any(Employee.class))).willReturn(employee);
        mockMvc.perform(post("/v1/employees/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("firstName").value(employee.getFirstName()))
                .andExpect(jsonPath("lastName").value(employee.getLastName()))
                .andExpect(jsonPath("email").value(employee.getEmail()))
                .andExpect(jsonPath("birthDate").value(employee.getBirthDate().toString()))
                .andExpect(jsonPath("hobbies[0]").value(employee.getHobbies().get(0)))
                .andExpect(jsonPath("hobbies[1]").value(employee.getHobbies().get(1)));
    }

    @Test
    public void testCreateEmployeeBadRequest() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("J");
        employee.setLastName("W");
        employee.setEmail("example");
        mockMvc.perform(post("/v1/employees/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors.*", hasSize(3)));
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        Employee employee1 =
                createEmployee("test1", "1990-12-20", Arrays.asList("Swimming", "Running"));
        Employee employee2 =
                createEmployee("test2", "1980-01-01", Arrays.asList("Reading", "Writing"));
        given(employeeService.getEmployees()).willReturn(Arrays.asList(employee1, employee2));
        mockMvc.perform(get("/v1/employees/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.[0].firstName").value(employee1.getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(employee1.getLastName()))
                .andExpect(jsonPath("$.[0].email").value(employee1.getEmail()))
                .andExpect(jsonPath("$.[0].birthDate").value(employee1.getBirthDate().toString()))
                .andExpect(jsonPath("$.[0].hobbies[0]").value(employee1.getHobbies().get(0)))
                .andExpect(jsonPath("$.[0].hobbies[1]").value(employee1.getHobbies().get(1)))
                .andExpect(jsonPath("$.[1].firstName").value(employee2.getFirstName()))
                .andExpect(jsonPath("$.[1].lastName").value(employee2.getLastName()))
                .andExpect(jsonPath("$.[1].email").value(employee2.getEmail()))
                .andExpect(jsonPath("$.[1].birthDate").value(employee2.getBirthDate().toString()))
                .andExpect(jsonPath("$.[1].hobbies[0]").value(employee2.getHobbies().get(0)))
                .andExpect(jsonPath("$.[1].hobbies[1]").value(employee2.getHobbies().get(1)));
    }

    @Test
    public void testGetEmployee() throws Exception {
        Employee employee =
                createEmployee("test1", "1990-12-20", Arrays.asList("Swimming", "Running"));
        given(employeeService.getEmployee(anyString())).willReturn(Optional.of(employee));
        mockMvc.perform(get(String.format("/v1/employees/%s", UUID.randomUUID().toString())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value(employee.getFirstName()))
                .andExpect(jsonPath("lastName").value(employee.getLastName()))
                .andExpect(jsonPath("email").value(employee.getEmail()))
                .andExpect(jsonPath("birthDate").value(employee.getBirthDate().toString()))
                .andExpect(jsonPath("hobbies[0]").value(employee.getHobbies().get(0)))
                .andExpect(jsonPath("hobbies[1]").value(employee.getHobbies().get(1)));
    }

    @Test
    public void testGetEmployeeNotFound() throws Exception {
        given(employeeService.getEmployee(anyString())).willReturn(Optional.empty());
        mockMvc.perform(get(String.format("/v1/employees/%s", UUID.randomUUID().toString())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Employee employee = createEmployee("test", "1989-07-25", Arrays.asList("Reading", "Blogging"));
        given(employeeService.updateEmployee(anyString(), any(Employee.class))).willReturn(employee);
        mockMvc.perform(put(String.format("/v1/employees/%s", UUID.randomUUID().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value(employee.getFirstName()))
                .andExpect(jsonPath("lastName").value(employee.getLastName()))
                .andExpect(jsonPath("email").value(employee.getEmail()))
                .andExpect(jsonPath("birthDate").value(employee.getBirthDate().toString()))
                .andExpect(jsonPath("hobbies[0]").value(employee.getHobbies().get(0)))
                .andExpect(jsonPath("hobbies[1]").value(employee.getHobbies().get(1)));
    }

    @Test
    public void testUpdateEmployeeDuplicateEmail() throws Exception {
        Employee employee = createEmployee("test", "1989-07-25", Arrays.asList("Reading", "Blogging"));
        doThrow(new DuplicateEmailAddressException("email", "Email is duplicated"))
                .when(employeeService).updateEmployee(anyString(), any(Employee.class));
        mockMvc.perform(put(String.format("/v1/employees/%s", UUID.randomUUID().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors", hasSize(1)))
                .andExpect(jsonPath("fieldErrors[0].field").value("email"))
                .andExpect(jsonPath("fieldErrors[0].error").value("Email is duplicated"));
    }

    @Test
    public void testUpdateEmployeeInvalidUuid() throws Exception {
        Employee employee = createEmployee("test", "1989-07-25", Arrays.asList("Reading", "Blogging"));
        doThrow(new EntityNotFoundException("Failed to retrieve entity"))
                .when(employeeService).updateEmployee(anyString(), any(Employee.class));
        mockMvc.perform(put(String.format("/v1/employees/%s", UUID.randomUUID().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Failed to retrieve entity"));
    }

    @Test
    public void testUpdateEmployeeInvalidPayLoad() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("x");
        employee.setLastName("y");
        doThrow(new EntityNotFoundException("Failed to retrieve entity"))
                .when(employeeService).updateEmployee(anyString(), any(Employee.class));
        mockMvc.perform(put(String.format("/v1/employees/%s", UUID.randomUUID().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors.*", hasSize(3)));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        mockMvc.perform(delete(String.format("/v1/employees/%s", UUID.randomUUID().toString())))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteEmployeeNotFound() throws Exception {
        doThrow(new EntityNotFoundException()).when(employeeService).delete(anyString());
        mockMvc.perform(delete(String.format("/v1/employees/%s", UUID.randomUUID().toString())))
                .andExpect(status().isNotFound());
    }

    private Employee createEmployee(String name, String birthday, List<String> hobbies) {
        Employee employee = new Employee();
        employee.setFirstName("first" + name);
        employee.setLastName("last" + name);
        employee.setEmail(String.format("%s.%s@example.com", employee.getFirstName(), employee.getLastName()));
        employee.setBirthDate(LocalDate.parse(birthday));
        employee.setHobbies(hobbies);
        return employee;
    }
}
