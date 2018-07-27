package com.madadipouya.workforce.validator;

import com.madadipouya.workforce.model.Employee;
import com.madadipouya.workforce.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UniqueEmailValidatorTest {

    @InjectMocks
    private UniqueEmailValidator uniqueEmailValidator;

    @Mock
    private EmployeeService employeeService;

    @Test
    public void testIsValid() {
        String email = "example@example.com";
        when(employeeService.getEmployeeByEmail(anyString()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(mock(Employee.class)));
        boolean result = uniqueEmailValidator.isValid(email, mock(ConstraintValidatorContext.class));
        assertEquals(true, result);
        result = uniqueEmailValidator.isValid(email, mock(ConstraintValidatorContext.class));
        assertEquals(false, result);
        verify(employeeService, times(2)).getEmployeeByEmail(email);
    }
}
