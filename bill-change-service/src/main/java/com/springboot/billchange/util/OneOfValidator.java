package com.springboot.billchange.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class OneOfValidator implements ConstraintValidator<OneOf, String> {
    private String[] values;

    public final void initialize(final OneOf annotation) {
        values = annotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(values).anyMatch(i -> Double.valueOf(i).doubleValue() == Double.valueOf(value).doubleValue());
    }
}