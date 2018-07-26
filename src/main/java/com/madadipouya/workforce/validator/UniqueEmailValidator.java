package com.madadipouya.workforce.validator;

import com.madadipouya.workforce.metadata.UniqueEmail;
import com.madadipouya.workforce.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private EmployeeService employeeService;

    @Autowired
    public UniqueEmailValidator(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !employeeService.getEmployeeByEmail(email).isPresent();
    }
}