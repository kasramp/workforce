package com.madadipouya.workforce.repository;


import com.madadipouya.workforce.model.Employee;
import com.madadipouya.workforce.service.EmployeeService;
import com.madadipouya.workforce.service.exception.DuplicateEmailAddressException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


/**
 * Test class for {@link EmployeeRepository} to ensure caching is working as expected
 */

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
        when(employeeRepository.findByUuid(anyString())).thenReturn(Optional.of(createEmployee("test")));
        String uuid = UUID.randomUUID().toString();
        employeeService.getEmployee(uuid);
        employeeService.getEmployee(uuid);
        verify(employeeRepository, times(1)).findByUuid(uuid);
    }

    @Test
    public void testGetEmployeeByEmailCachesResult() {
        Employee employee = createEmployee("test");
        String email = employee.getEmail();
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.of(employee));

        employeeService.getEmployeeByEmail(email);
        employeeService.getEmployeeByEmail(email);
        verify(employeeRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testGetEmployeeByEmailCacheEvictedOnCreate() {
        Employee employee = createEmployee("test");
        String email = employee.getEmail();
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeService.getEmployeeByEmail(email);
        employeeService.createEmployee(employee);
        employeeService.getEmployeeByEmail(email);
        verify(employeeRepository, times(2)).findByEmail(email);
    }

    @Test
    public void testGetEmployeeByEmailCacheEvictedOnUpdate() throws DuplicateEmailAddressException {
        Employee employee = createEmployee("test");
        String email = employee.getEmail();
        String uuid = UUID.randomUUID().toString();
        when(employeeRepository.findByUuid(anyString())).thenReturn(Optional.of(employee));
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeService.getEmployeeByEmail(email);
        employeeService.updateEmployee(uuid, employee);
        employeeService.getEmployeeByEmail(email);
        verify(employeeRepository, times(2)).findByEmail(email);
    }

    @Test
    public void testGetEmployeeByEmailCacheEvictedOnDelete() {
        Employee employee = createEmployee("test");
        String email = employee.getEmail();
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(any(Employee.class));

        employeeService.getEmployeeByEmail(email);
        employeeService.delete(employee);
        employeeService.getEmployeeByEmail(email);
        verify(employeeRepository, times(2)).findByEmail(email);
    }

    @Test
    public void testGetEmployeeByEmailCacheEvictedOnDeleteByUuid() {
        Employee employee = createEmployee("test");
        String email = employee.getEmail();
        String uuid = UUID.randomUUID().toString();
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByUuid(anyString())).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(anyString());

        employeeService.getEmployeeByEmail(email);
        employeeService.delete(uuid);
        employeeService.getEmployeeByEmail(email);
        verify(employeeRepository, times(2)).findByEmail(email);
    }

    @Test
    public void testGetEmployeeCacheEvictedOnCreate() {
        Employee employee = createEmployee("test");
        String uuid = UUID.randomUUID().toString();
        when(employeeRepository.findByUuid(anyString())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeService.getEmployee(uuid);
        employeeService.createEmployee(employee);
        employeeService.getEmployee(uuid);
        verify(employeeRepository, times(2)).findByUuid(uuid);
    }

    @Test
    public void testGetEmployeeCacheEvictedOnUpdate() throws DuplicateEmailAddressException {
        Employee employee = createEmployee("test");
        String uuid = UUID.randomUUID().toString();
        when(employeeRepository.findByUuid(anyString())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeService.getEmployee(uuid);
        employeeService.updateEmployee(uuid, employee);
        employeeService.getEmployee(uuid);
        verify(employeeRepository, times(3)).findByUuid(uuid);
    }

    @Test
    public void testGetEmployeeCacheEvictedOnDelete() {
        Employee employee = createEmployee("test");
        String uuid = UUID.randomUUID().toString();
        when(employeeRepository.findByUuid(anyString())).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(any(Employee.class));

        employeeService.getEmployee(uuid);
        employeeService.delete(employee);
        employeeService.getEmployee(uuid);
        verify(employeeRepository, times(2)).findByUuid(uuid);
    }

    @Test
    public void testGetEmployeeCacheEvictedOnDeleteByUuid() {
        Employee employee = createEmployee("test");
        String uuid = UUID.randomUUID().toString();
        when(employeeRepository.findByUuid(anyString())).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByUuid(anyString())).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(anyString());

        employeeService.getEmployee(uuid);
        employeeService.delete(uuid);
        employeeService.getEmployee(uuid);
        verify(employeeRepository, times(2)).findByUuid(uuid);
    }

    @Test
    public void testGetAllEmployeesCacheEvictedOnCreate() {
        Employee employee = createEmployee("test");
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeService.getEmployees();
        employeeService.createEmployee(employee);
        employeeService.getEmployees();
        verify(employeeRepository, times(2)).findAll();
    }

    @Test
    public void testGetAllEmployeesCacheEvictedOnUpdate() throws DuplicateEmailAddressException {
        Employee employee = createEmployee("test");
        String uuid = UUID.randomUUID().toString();
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());
        when(employeeRepository.findByUuid(anyString())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeService.getEmployees();
        employeeService.updateEmployee(uuid, employee);
        employeeService.getEmployees();
        verify(employeeRepository, times(2)).findAll();
    }

    @Test
    public void testGetAllEmployeesCacheEvictedOnDelete() {
        Employee employee = createEmployee("test");
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());
        doNothing().when(employeeRepository).delete(any(Employee.class));

        employeeService.getEmployees();
        employeeService.delete(employee);
        employeeService.getEmployees();
        verify(employeeRepository, times(2)).findAll();
    }

    @Test
    public void testGetAllEmployeesCacheEvictedOnDeleteByUuid() {
        String uuid = UUID.randomUUID().toString();
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());
        when(employeeRepository.existsByUuid(anyString())).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(anyString());

        employeeService.getEmployees();
        employeeService.delete(uuid);
        employeeService.getEmployees();
        verify(employeeRepository, times(2)).findAll();
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
