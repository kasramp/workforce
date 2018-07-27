package com.madadipouya.workforce.repository;

import com.madadipouya.workforce.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Test class for {@link EmployeeRepository}
 */

@TestPropertySource(locations = {"classpath:test.properties"})
@RunWith(SpringRunner.class)
@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Use entity manager to persisting object to database and not only read from cache
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testFindByUuid() {
        Employee savedEmployee = testEntityManager.persistAndFlush(createEmployee("test"));
        Optional<Employee> optionalEmployee = employeeRepository.findByUuid(savedEmployee.getUuid());
        assertTrue(optionalEmployee.isPresent());
        Employee employee = optionalEmployee.get();
        assertEquals(savedEmployee.getUuid(), employee.getUuid());
        assertEquals(savedEmployee.getFirstName(), employee.getFirstName());
        assertEquals(savedEmployee.getLastName(), employee.getLastName());
        assertEquals(savedEmployee.getEmail(), employee.getEmail());
        assertEquals(savedEmployee.getBirthDate(), employee.getBirthDate());
        Assertions.assertThat(savedEmployee.getHobbies()).containsExactly("Hobby-0 test", "Hobby-1 test");
    }

    @Test
    public void testFindByEmail() {
        Employee savedEmployee = testEntityManager.persistAndFlush(createEmployee("test"));
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(savedEmployee.getEmail());
        assertTrue(optionalEmployee.isPresent());
        Employee employee = optionalEmployee.get();
        assertEquals(savedEmployee.getUuid(), employee.getUuid());
        assertEquals(savedEmployee.getFirstName(), employee.getFirstName());
        assertEquals(savedEmployee.getLastName(), employee.getLastName());
        assertEquals(savedEmployee.getEmail(), employee.getEmail());
        assertEquals(savedEmployee.getBirthDate(), employee.getBirthDate());
        Assertions.assertThat(savedEmployee.getHobbies()).containsExactly("Hobby-0 test", "Hobby-1 test");
    }

    @Test
    public void testGetAllEmployee() {
        testEntityManager.persistAndFlush(createEmployee("test"));
        testEntityManager.persistAndFlush(createEmployee("test1"));
        List<Employee> employees = employeeRepository.findAll();
        assertEquals(2, employees.size());
    }

    @Test
    public void testSaveEmployee() {
        Employee savedEmployee = employeeRepository.save(createEmployee("test"));
        assertNotNull(savedEmployee);
        Assertions.assertThat(savedEmployee.getUuid()).isNotBlank();
    }

    @Test
    public void testExistsByUuid() {
        Employee savedEmployee = testEntityManager.persistAndFlush(createEmployee("test"));
        assertTrue(employeeRepository.existsByUuid(savedEmployee.getUuid()));
        assertFalse(employeeRepository.existsByUuid(UUID.randomUUID().toString()));
    }

    @Test
    public void testExistsByEmail() {
        Employee savedEmployee = testEntityManager.persistAndFlush(createEmployee("test"));
        assertTrue(employeeRepository.existsByEmail(savedEmployee.getEmail()));
        assertFalse(employeeRepository.existsByEmail("john.wick@example.com"));
    }

    @Test
    public void testDeleteByEmployeeEntity() {
        Employee savedEmployee = testEntityManager.persistAndFlush(createEmployee("test"));
        employeeRepository.delete(savedEmployee);
        assertFalse(employeeRepository.existsByUuid(savedEmployee.getUuid()));
    }

    @Test
    public void testDeleteEmployeeByUuid() {
        Employee savedEmployee = testEntityManager.persistAndFlush(createEmployee("test"));
        employeeRepository.deleteById(savedEmployee.getUuid());
        assertFalse(employeeRepository.existsByUuid(savedEmployee.getUuid()));
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
