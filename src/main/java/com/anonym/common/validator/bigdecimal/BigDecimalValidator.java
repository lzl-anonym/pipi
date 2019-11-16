package com.anonym.common.validator.bigdecimal;

import com.anonym.utils.SmartBigDecimalUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * BigDecimal 类校验器
 */
public class BigDecimalValidator implements ConstraintValidator<CheckBigDecimal, BigDecimal> {

    /**
     * 获取定义的数值
     */
    private BigDecimal value;

    /**
     * 获取比较符
     */
    private ComparisonSymbolEnum symbolEnum;

    /**
     * 是否必须
     */
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
            // 等于
            case EQUAL:
                return SmartBigDecimalUtils.equals(decimal, value);
            // 不等于
            case NOT_EQUAL:
                return !SmartBigDecimalUtils.equals(decimal, value);
            // 小于
            case LESS_THAN:
                return SmartBigDecimalUtils.isLessThan(decimal, value);
            // 小于等于
            case LESS_THAN_OR_EQUAL:
                return SmartBigDecimalUtils.isLessThan(decimal, value) || SmartBigDecimalUtils.equals(decimal, value);
            // 大于
            case GREATER_THAN:
                return SmartBigDecimalUtils.isGreaterThan(decimal, value);
            // 大于等于
            case GREATER_THAN_OR_EQUAL:
                return SmartBigDecimalUtils.isGreaterThan(decimal, value) || SmartBigDecimalUtils.equals(decimal, value);
            default:
        }

        return false;
    }
}
