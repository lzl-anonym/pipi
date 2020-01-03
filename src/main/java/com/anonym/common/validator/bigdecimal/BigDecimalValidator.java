package com.anonym.common.validator.bigdecimal;

import com.anonym.utils.SmartBigDecimalUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;


public class BigDecimalValidator implements ConstraintValidator<CheckBigDecimal, BigDecimal> {


    private BigDecimal value;


    private ComparisonSymbolEnum symbolEnum;


    private boolean required;

    @Override
    public void initialize(CheckBigDecimal constraintAnnotation) {

        value = new BigDecimal(constraintAnnotation.value());
        symbolEnum = constraintAnnotation.symbolEnum();
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(BigDecimal decimal, ConstraintValidatorContext constraintValidatorContext) {

        if (null == decimal) {
            return !required;
        }

        switch (symbolEnum) {
            case EQUAL:
                return SmartBigDecimalUtils.equals(decimal, value);
            case NOT_EQUAL:
                return !SmartBigDecimalUtils.equals(decimal, value);
            case LESS_THAN:
                return SmartBigDecimalUtils.isLessThan(decimal, value);
            case LESS_THAN_OR_EQUAL:
                return SmartBigDecimalUtils.isLessThan(decimal, value) || SmartBigDecimalUtils.equals(decimal, value);
            case GREATER_THAN:
                return SmartBigDecimalUtils.isGreaterThan(decimal, value);
            case GREATER_THAN_OR_EQUAL:
                return SmartBigDecimalUtils.isGreaterThan(decimal, value) || SmartBigDecimalUtils.equals(decimal, value);
            default:
        }

        return false;
    }
}
