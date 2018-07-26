package com.madadipouya.workforce.metadata;


import com.madadipouya.workforce.model.Employee;
import com.madadipouya.workforce.validator.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom validator to check whether email address is unique
 * when request payload submitted. It's used only for {@link com.madadipouya.workforce.rest.v1.EmployeeController#createEmployee(Employee)}
 */

@Documented
@Retention(RUNTIME)
@Target({FIELD, ANNOTATION_TYPE, PARAMETER})
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {

    String message() default "Email address should be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
