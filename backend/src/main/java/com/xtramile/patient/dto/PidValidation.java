package com.xtramile.patient.dto;

import com.xtramile.patient.util.PidValidator;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PidValidation.PidValidatorImpl.class)
@Documented
public @interface PidValidation {
    String message() default "Invalid PID format. Supported: Medicare (11 digits), IHI (16 digits), or MRN (6-12 alphanumeric)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class PidValidatorImpl implements ConstraintValidator<PidValidation, String> {
        @Override
        public void initialize(PidValidation constraintAnnotation) {
        }

        @Override
        public boolean isValid(String pid, ConstraintValidatorContext context) {
            if (pid == null || pid.isEmpty()) {
                return true;
            }
            return PidValidator.isValid(pid);
        }
    }
}

