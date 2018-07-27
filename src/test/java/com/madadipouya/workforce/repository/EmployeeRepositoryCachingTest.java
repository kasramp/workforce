package com.madadipouya.workforce.repository;


import com.madadipouya.workforce.model.Employee;
import com.madadipouya.workforce.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Test class for {@link EmployeeRepository} to ensure caching is working as expected
 */

@TestPropertySource(locations = {"classpath:test.properties"})
@RunWith(SpringRunner.class)
// TODO Fix the issue related to Springfox to be able to just start the app (WebEnvironment#None}, not entire server
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class EmployeeRepositoryCachingTest {

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    public void testGetEmployeesCachesResult() {
        when(employeeRepository.findAll())
                .thenReturn(Arrays.asList(createEmployee("test"), createEmployee("test")));
        employeeService.getEmployees();
        employeeService.getEmployees();
        verify(employeeRepository, times(1)).findAll();

    }

    @Test
    public void testGetEmployeeByUuidCachesResult() {
        when(employeeRepository.findByUuid(anyString()))
                .thenReturn(Optional.of(createEmployee("test")));
        String uuid = UUID.randomUUID().toString();
        employeeService.getEmployee(uuid);
        employeeService.getEmployee(uuid);
        verify(employeeRepository, times(1)).findByUuid(uuid);

    }

    @Test
    public void testGetEmployeeByEmailCachesResult() {
        Employee employee = createEmployee("test");
        String email = employee.getEmail();
        when(employeeRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(employee));

        employeeService.getEmployeeByEmail(email);
        employeeService.getEmployeeByEmail(email);
        verify(employeeRepository, times(1)).findByEmail(email);
    }

    // TODO replace with EmployeeBuilder
    private Employee createEmployee(String name) {
        Employee employee = new Employee();
        employee.setFirstName(String.format("first %s", name));
        employee.setLastName(String.format("last %s", name));
        employee.setEmail(String.format("%s.%s@example.com", employee.getFirstName(), employee.getLastName()));
        employee.setBirthDate(LocalDate.parse("1980-01-01"));
        employee.setHobbies(Arrays.asList(String.format("Hobby-0 %s", name), String.format("Hobby-1 %s", name)));
        return employee;
    }
}
