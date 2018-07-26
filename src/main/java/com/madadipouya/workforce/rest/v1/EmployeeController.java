package com.madadipouya.workforce.rest.v1;


import com.madadipouya.workforce.model.Employee;
import com.madadipouya.workforce.service.EmployeeService;
import com.madadipouya.workforce.service.exception.DuplicateEmailAddressException;
import com.madadipouya.workforce.validator.group.Create;
import com.madadipouya.workforce.validator.group.Update;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(value = "v1/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @ApiOperation(value = "Gets a list of Employees", response = Employee.class, tags = "Employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fetches Employees list successfully"),
            @ApiResponse(code = 401, message = "Client has not authenticated"),
            @ApiResponse(code = 500, message = "Service fails to process the request")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Employee> getAllEmployees() {
        return employeeService.getEmployees();
    }

    @ApiOperation(value = "Creates a new Employees", response = Employee.class, tags = "Employee")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully creates the Employee"),
            @ApiResponse(code = 400, message = "Incorrect payload provided"),
            @ApiResponse(code = 401, message = "Client has not authenticated"),
            @ApiResponse(code = 500, message = "Service fails to process the request")})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@Validated(Create.class) @RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @ApiOperation(value = "Retrieves an Employees", response = Employee.class, tags = "Employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieves the Employee"),
            @ApiResponse(code = 401, message = "Client has not authenticated"),
            @ApiResponse(code = 404, message = "Invalid Employee Uuid provided"),
            @ApiResponse(code = 500, message = "Service fails to process the request")})
    @GetMapping(value = "/{employeeUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Employee getEmployee(@PathVariable String employeeUuid) {
        return employeeService.getEmployee(employeeUuid)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Unable to find Employee with '%s' uuid", employeeUuid)));
    }

    @ApiOperation(value = "Update an Employees", response = Employee.class, tags = "Employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updates the Employee"),
            @ApiResponse(code = 400, message = "Incorrect payload provided"),
            @ApiResponse(code = 401, message = "Client has not authenticated"),
            @ApiResponse(code = 404, message = "Invalid Employee Uuid provided"),
            @ApiResponse(code = 500, message = "Service fails to process the request")})
    @PutMapping(value = "/{employeeUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Employee updateEmployee(@PathVariable String employeeUuid, @Validated(Update.class) @RequestBody Employee employee) throws DuplicateEmailAddressException {
        return employeeService.updateEmployee(employeeUuid, employee);
    }

    @ApiOperation(value = "Deletes an Employees", response = Employee.class, tags = "Employee")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deletes the Employee"),
            @ApiResponse(code = 401, message = "Client has not authenticated"),
            @ApiResponse(code = 404, message = "Invalid Employee Uuid provided"),
            @ApiResponse(code = 500, message = "Service fails to process the request")})
    @DeleteMapping(value = "/{employeeUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable String employeeUuid) {
        employeeService.delete(employeeUuid);
    }
}
