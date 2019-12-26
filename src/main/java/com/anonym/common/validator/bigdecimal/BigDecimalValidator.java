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
        // 初始化属性
        value = new BigDecimal(constraintAnnotation.value());
        symbolEnum = constraintAnnotation.symbolEnum();
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(BigDecimal decimal, ConstraintValidatorContext constraintValidatorContext) {

        // 如果数值为空，校验是否必须
        if (null == decimal) {
            return !required;
        }

        // 根据操作符，校验结果
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
