package com.madadipouya.workforce.service;

import com.madadipouya.workforce.model.Employee;
import com.madadipouya.workforce.repository.EmployeeRepository;
import com.madadipouya.workforce.service.exception.DuplicateEmailAddressException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    @InjectMocks
    DefaultEmployeeService employeeService;

    @Mock
    EmployeeRepository employeeRepository;

    @Test
    public void testCreateEmployee() {
        Employee employee = new Employee();
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        employeeService.createEmployee(employee);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    public void testUpdateEmployeeWithSameEmail() throws DuplicateEmailAddressException {
        String employeeUuid = UUID.randomUUID().toString();
        Employee newEmployee = createEmployee("new");
        newEmployee.setEmail("example@example.com");
        Employee persistedOldEmployee = createEmployee("persistedOld");
        persistedOldEmployee.setEmail("example@example.com");
        Employee newEmployeeToPersist = employeeService.copyProperties(newEmployee, persistedOldEmployee);
        when(employeeRepository.findByUuid(anyString())).thenReturn(Optional.of(persistedOldEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployeeToPersist);
        Employee result = employeeService.updateEmployee(employeeUuid, newEmployee);
        verify(employeeRepository, times(1)).findByUuid(employeeUuid);
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeRepository, times(0)).existsByEmail("example@example.com");
        assertEquals(newEmployeeToPersist, result);
    }

    @Test
    public void testUpdateEmployeeWithDifferentEmail() throws DuplicateEmailAddressException {
        String employeeUuid = UUID.randomUUID().toString();
        Employee newEmployee = createEmployee("new");
        Employee persistedOldEmployee = createEmployee("persistedOld");
        when(employeeRepository.findByUuid(anyString())).thenReturn(Optional.of(persistedOldEmployee));
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        employeeService.updateEmployee(employeeUuid, newEmployee);
        verify(employeeRepository, times(1)).findByUuid(employeeUuid);
        verify(employeeRepository, times(1)).existsByEmail(newEmployee.getEmail());
        Employee newEmployeeToPersist = employeeService.copyProperties(newEmployee, persistedOldEmployee);
        verify(employeeRepository, times(1)).save(newEmployeeToPersist);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateEmployeeInvalidUuid() throws DuplicateEmailAddressException {
        String employeeUuid = UUID.randomUUID().toString();
        Employee newEmployee = createEmployee("new");
        when(employeeRepository.findByUuid(anyString())).thenReturn(Optional.empty());
        employeeService.updateEmployee(employeeUuid, newEmployee);
        verify(employeeRepository, times(1)).findByUuid(employeeUuid);
        verify(employeeRepository, times(0)).existsByEmail(newEmployee.getEmail());
    }

    @Test(expected = DuplicateEmailAddressException.class)
    public void testUpdateEmployeeDuplicateEmailAddress() throws DuplicateEmailAddressException {
        String employeeUuid = UUID.randomUUID().toString();
        Employee newEmployee = createEmployee("new");
        Employee persistedOldEmployee = createEmployee("persistedOld");
        when(employeeRepository.findByUuid(anyString())).thenReturn(Optional.of(persistedOldEmployee));
        when(employeeRepository.existsByEmail(anyString())).thenReturn(true);
        employeeService.updateEmployee(employeeUuid, newEmployee);
        verify(employeeRepository, times(1)).findByUuid(employeeUuid);
        verify(employeeRepository, times(1)).existsByEmail(newEmployee.getEmail());
    }

    @Test
    public void testGetEmployee() {
        Optional<Employee> employee = Optional.of(mock(Employee.class));
        String uuid = UUID.randomUUID().toString();
        when(employeeRepository.findByUuid(anyString())).thenReturn(employee);
        Optional<Employee> result = employeeService.getEmployee(uuid);
        verify(employeeRepository, times(1)).findByUuid(uuid);
        assertEquals(employee, result);
    }

    @Test
    public void testGetEmployeeByEmail() {
        Optional<Employee> employee = Optional.of(mock(Employee.class));
        String email = "example@example.com";
        when(employeeRepository.findByEmail(anyString())).thenReturn(employee);
        Optional<Employee> result = employeeService.getEmployeeByEmail(email);
        verify(employeeRepository, times(1)).findByEmail(email);
        assertEquals(employee, result);
    }

    @Test
    public void testGetEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(mock(Employee.class));
        employees.add(mock(Employee.class));
        when(employeeRepository.findAll()).thenReturn(employees);
        List<Employee> result = employeeService.getEmployees();
        verify(employeeRepository, times(1)).findAll();
        assertEquals(employees, result);
        assertEquals(2, employees.size());
    }

    @Test
    public void testDeleteByEmployee() {
        Employee employee = mock(Employee.class);
        employeeService.delete(employee);
        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    public void testDeleteByEmployeeUuidNotThrownException() {
        String employeeUuid = UUID.randomUUID().toString();
        when(employeeRepository.existsByUuid(anyString())).thenReturn(true);
        employeeService.delete(employeeUuid);
        verify(employeeRepository, times(1)).existsByUuid(employeeUuid);
        verify(employeeRepository, times(1)).deleteById(employeeUuid);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByEmployeeUuidThrowsException() {
        String employeeUuid = UUID.randomUUID().toString();
        when(employeeRepository.existsByUuid(anyString())).thenReturn(false);
        employeeService.delete(employeeUuid);
        verify(employeeRepository, times(1)).existsByUuid(employeeUuid);
        verify(employeeRepository, times(0)).deleteById(employeeUuid);
    }

    @Test
    public void testIsEmployeeExist() {
        String employeeUuid = UUID.randomUUID().toString();
        when(employeeRepository.existsByUuid(anyString()))
                .thenReturn(false)
                .thenReturn(true);
        boolean result = employeeService.isEmployeeExist(employeeUuid);
        assertEquals(false, result);
        result = employeeService.isEmployeeExist(employeeUuid);
        assertEquals(true, result);
        verify(employeeRepository, times(2)).existsByUuid(employeeUuid);
    }

    private Employee createEmployee(String prefix) {
        List<String> hobbies = new ArrayList<>();
        hobbies.add(prefix + "Walking");
        hobbies.add(prefix + "Swiming");
        Employee employee = new Employee();
        employee.setFirstName(prefix + "FirstNamme");
        employee.setLastName(prefix + "LastName");
        employee.setEmail(prefix + "example@example.com");
        employee.setHobbies(hobbies);
        return employee;
    }
}
