package com.madadipouya.workforce.service;

import com.madadipouya.workforce.model.Employee;
import com.madadipouya.workforce.service.exception.DuplicateEmailAddressException;

import java.util.List;
import java.util.Optional;

/**
 * This service handles all interaction with the repository {@link com.madadipouya.workforce.repository.EmployeeRepository}.
 * Contains all business logic.
 */
public interface EmployeeService {

    /**
     * Creates a new Employee
     *
     * @param employee a {@link Employee} to persist
     * @return a persisted instance of {@link Employee} with {@link Employee#uuid}
     */
    Employee createEmployee(Employee employee);

    /**
     * Updates a current Employee
     *
     * @param employeeUuid uuid of the {@link Employee}
     * @param employee     an {@link Employee} to update
     * @return an updated instance of {@link Employee}
     * @throws DuplicateEmailAddressException if {@link Employee#email} already exists under another {@link Employee}
     */
    Employee updateEmployee(String employeeUuid, Employee employee) throws DuplicateEmailAddressException;

    /**
     * Retrieves an {@link Employee} based on uuid
     *
     * @param employeeUuid uuid of Employee to fetch
     * @return an instance of {@link Employee} if Employee exist, otherwise empty optional
     */
    Optional<Employee> getEmployee(String employeeUuid);

    /**
     * Retrieves an {@link Employee} based on email
     *
     * @param email email of Employee to fetch
     * @return an instance of {@link Employee} if Employee exist, otherwise empty optional
     */
    Optional<Employee> getEmployeeByEmail(String email);

    /**
     * Retrieves all Employees
     *
     * @return a list of {@link Employee}
     */
    List<Employee> getEmployees();

    /**
     * Hard deletes an Employee
     *
     * @param employee an Employee to delete
     */
    void delete(Employee employee);

    /**
     * Hard deletes an Employee
     *
     * @param employeeUuid uuid of an Employee to delete
     */
    void delete(String employeeUuid);

    /**
     * Checks whether an Employee exist based on given uuid
     *
     * @param employeeUuid uuid of the {@link Employee} to delete
     * @return {@code true} if Employee exists, {@code false} otherwise
     */
    boolean isEmployeeExist(String employeeUuid);
}
