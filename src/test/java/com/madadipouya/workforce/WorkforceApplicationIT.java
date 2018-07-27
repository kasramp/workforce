package com.madadipouya.workforce;

import com.madadipouya.workforce.model.Employee;
import com.madadipouya.workforce.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for happy paths only
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = {"classpath:test.properties"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkforceApplicationIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeService employeeService;

    @PostConstruct
    public void afterPropertiesSet() {
        BasicAuthorizationInterceptor bai = new BasicAuthorizationInterceptor("admin", "password");
        restTemplate.getRestTemplate().getInterceptors().add(bai);
    }

    @Test
    public void testCreateEmployee() {
        Employee employee = createEmployee("kasra", "madadipouya", "1989-07-25", "reading", "writing");
        ResponseEntity<EmployeeResponseInternal> response = restTemplate.postForEntity("/v1/employees", employee, EmployeeResponseInternal.class);

        EmployeeResponseInternal createdEmployee = response.getBody();
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertThat(createdEmployee.getUuid()).isNotBlank();
        assertEquals("kasra", createdEmployee.getFirstName());
        assertEquals("madadipouya", createdEmployee.getLastName());
        assertEquals("kasra.madadipouya@example.com", createdEmployee.getEmail());
        assertEquals(LocalDate.parse("1989-07-25"), createdEmployee.getBirthDate());
        assertNotNull(createdEmployee.getHobbies());
        assertEquals(2, createdEmployee.getHobbies().size());
        assertThat(createdEmployee.getHobbies()).containsExactly("reading", "writing");
    }

    @Test
    public void testGetEmployees() {
        Employee employee = createEmployee("kasra", "madadipouya", "1989-07-25", "reading", "writing");
        Employee employee1 = createEmployee("john", "wick", "1980-01-01", "fighting", "running");

        assertEquals(HttpStatus.CREATED, restTemplate.postForEntity("/v1/employees", employee, EmployeeResponseInternal.class).getStatusCode());
        assertEquals(HttpStatus.CREATED, restTemplate.postForEntity("/v1/employees", employee1, EmployeeResponseInternal.class).getStatusCode());

        ResponseEntity<List<EmployeeResponseInternal>> response = restTemplate
                .exchange("/v1/employees", HttpMethod.GET, null, new ParameterizedTypeReference<List<EmployeeResponseInternal>>() {
                });

        List<EmployeeResponseInternal> employees = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(employees);
        assertEquals(2, employees.size());
        assertThat(employees.get(0).getUuid()).isNotBlank();
        assertEquals("kasra", employees.get(0).getFirstName());
        assertEquals("madadipouya", employees.get(0).getLastName());
        assertEquals("kasra.madadipouya@example.com", employees.get(0).getEmail());
        assertEquals(LocalDate.parse("1989-07-25"), employees.get(0).getBirthDate());
        assertNotNull(employees.get(0).getHobbies());
        assertEquals(2, employees.get(0).getHobbies().size());
        assertThat(employees.get(0).getHobbies()).containsExactly("reading", "writing");

        assertThat(employees.get(1).getUuid()).isNotBlank();
        assertEquals("john", employees.get(1).getFirstName());
        assertEquals("wick", employees.get(1).getLastName());
        assertEquals("john.wick@example.com", employees.get(1).getEmail());
        assertEquals(LocalDate.parse("1980-01-01"), employees.get(1).getBirthDate());
        assertNotNull(employees.get(1).getHobbies());
        assertEquals(2, employees.get(1).getHobbies().size());
        assertThat(employees.get(1).getHobbies()).containsExactly("fighting", "running");
    }

    @Test
    public void testDeleteEmployee() {
        Employee employee = createEmployee("kasra", "madadipouya", "1989-07-25", "reading", "writing");
        ResponseEntity<EmployeeResponseInternal> response = restTemplate.postForEntity("/v1/employees", employee, EmployeeResponseInternal.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        String employeeUuid = response.getBody().getUuid();
        assertEquals(HttpStatus.NO_CONTENT,
                restTemplate.exchange("/v1/employees/{uuid}", HttpMethod.DELETE, null,
                        EmployeeResponseInternal.class, employeeUuid).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, restTemplate.getForEntity("/v1/employees/{uuid}",
                EmployeeResponseInternal.class, employeeUuid).getStatusCode());
    }

    @Test
    public void testUpdateEmployee() {
        Employee employeeToCreateAndUpdate = createEmployee("kasra", "madadipouya", "1989-07-25", "reading", "writing");
        ResponseEntity<EmployeeResponseInternal> response = restTemplate.postForEntity("/v1/employees", employeeToCreateAndUpdate, EmployeeResponseInternal.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        String employeeUuid = response.getBody().getUuid();

        employeeToCreateAndUpdate.setFirstName("john");
        employeeToCreateAndUpdate.setLastName("wick");
        employeeToCreateAndUpdate.setEmail("john.wick@example.com");
        employeeToCreateAndUpdate.setBirthDate(LocalDate.parse("1980-01-01"));
        employeeToCreateAndUpdate.setHobbies(Arrays.asList("fighting", "running"));

        response = restTemplate.exchange("/v1/employees/{uuid}", HttpMethod.PUT, new HttpEntity<>(employeeToCreateAndUpdate),
                EmployeeResponseInternal.class, employeeUuid);

        EmployeeResponseInternal employee = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(employee.getUuid()).isNotBlank();
        assertEquals(employeeUuid, employee.getUuid());
        assertEquals("john", employee.getFirstName());
        assertEquals("wick", employee.getLastName());
        assertEquals("john.wick@example.com", employee.getEmail());
        assertEquals(LocalDate.parse("1980-01-01"), employee.getBirthDate());
        assertNotNull(employee.getHobbies());
        assertEquals(2, employee.getHobbies().size());
        assertThat(employee.getHobbies()).containsExactly("fighting", "running");
    }

    @Test
    public void testGetEmployee() {
        Employee employeeToCreate = createEmployee("kasra", "madadipouya", "1989-07-25", "reading", "writing");
        ResponseEntity<EmployeeResponseInternal> response = restTemplate.postForEntity("/v1/employees", employeeToCreate, EmployeeResponseInternal.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        String employeeUuid = response.getBody().getUuid();

        response = restTemplate.getForEntity("/v1/employees/{uuid}",
                EmployeeResponseInternal.class, employeeUuid);

        EmployeeResponseInternal employee = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(employee.getUuid()).isNotBlank();
        assertEquals(employeeUuid, employee.getUuid());
        assertEquals("kasra", employee.getFirstName());
        assertEquals("madadipouya", employee.getLastName());
        assertEquals("kasra.madadipouya@example.com", employee.getEmail());
        assertEquals(LocalDate.parse("1989-07-25"), employee.getBirthDate());
        assertNotNull(employee.getHobbies());
        assertEquals(2, employee.getHobbies().size());
        assertThat(employee.getHobbies()).containsExactly("reading", "writing");

    }


    private Employee createEmployee(String firstName, String lastName, String birthday, String... hobbies) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(String.format("%s.%s@example.com", firstName, lastName));
        employee.setBirthDate(LocalDate.parse(birthday));
        employee.setHobbies(Arrays.asList(hobbies));
        return employee;
    }

    /**
     * Internal utility class to be used to deserialize response from controller.
     * This class is needed because {@link Employee} does not allow deserializer to
     * set uuid. Hence, it is not able to test {@link Employee}'s uuid.
     */
    private static class EmployeeResponseInternal {

        private String uuid;

        private String firstName;

        private String lastName;

        private String email;

        private LocalDate birthDate;

        private List<String> hobbies;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
        }

        public List<String> getHobbies() {
            return hobbies;
        }

        public void setHobbies(List<String> hobbies) {
            this.hobbies = hobbies;
        }
    }
}
