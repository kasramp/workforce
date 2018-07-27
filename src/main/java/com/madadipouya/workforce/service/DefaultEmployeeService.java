package com.madadipouya.workforce.service;

import com.madadipouya.workforce.model.Employee;
import com.madadipouya.workforce.repository.EmployeeRepository;
import com.madadipouya.workforce.service.exception.DuplicateEmailAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultEmployeeService implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public DefaultEmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(String employeeUuid, Employee newEmployee) throws DuplicateEmailAddressException {
        Employee employee = getEmployee(employeeUuid).orElseThrow(() ->
                new EntityNotFoundException(String.format("Unable to find Employee with '%s' uuid", employeeUuid)));
        boolean isSameEmail = employee.getEmail().equalsIgnoreCase(newEmployee.getEmail());
        if (!isSameEmail && employeeRepository.existsByEmail(newEmployee.getEmail())) {
            throw new DuplicateEmailAddressException("email", "provided email already exists");
        }
        return employeeRepository.save(copyProperties(newEmployee, employee));
    }

    @Override
    @Cacheable("employees")
    public Optional<Employee> getEmployee(String employeeUuid) {
        return employeeRepository.findByUuid(employeeUuid);
    }

    @Override
    @Cacheable("employees")
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    @Cacheable("employees")
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public void delete(Employee employee) {
        employeeRepository.delete(employee);
    }

    @Override
    public void delete(String employeeUuid) {
        if (!isEmployeeExist(employeeUuid)) {
            throw new EntityNotFoundException(String.format("Unable to find Employee with '%s' uuid", employeeUuid));
        }
        employeeRepository.deleteById(employeeUuid);
    }

    @Override
    public boolean isEmployeeExist(String employeeUuid) {
        return employeeRepository.existsByUuid(employeeUuid);
    }

    Employee copyProperties(Employee from, Employee to) {
        to.setFirstName(from.getFirstName());
        to.setLastName(from.getLastName());
        to.setEmail(from.getEmail());
        to.setBirthDate(from.getBirthDate());
        to.setHobbies(from.getHobbies());
        return to;
    }
}