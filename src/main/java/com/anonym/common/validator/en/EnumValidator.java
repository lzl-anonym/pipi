package com.anonym.common.validator.en;

import com.anonym.common.domain.BaseEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;


public class EnumValidator implements ConstraintValidator<CheckEnum, Object> {


    private Class<? extends BaseEnum> enumClass;


    private boolean required;

    @Override
    public void initialize(CheckEnum constraintAnnotation) {
        enumClass = constraintAnnotation.enumClazz();
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (null == value) {
            return !required;
        }

        if (value instanceof List) {
            return this.checkList((List<Object>) value);
        }

        return this.hasEnum(value);
    }

    private boolean checkList(List<Object> list) {
        if (required && list.isEmpty()) {
            return false;
        }
        for (Object obj : list) {
            boolean hasEnum = this.hasEnum(obj);
            if (!hasEnum) {
                return false;
            }
        }
        return true;
    }

    private boolean hasEnum(Object value) {
        BaseEnum[] enums = enumClass.getEnumConstants();
        for (BaseEnum baseEnum : enums) {
            if (baseEnum.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
