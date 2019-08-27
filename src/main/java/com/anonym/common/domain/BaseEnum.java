package com.anonym.common.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Objects;


public interface BaseEnum {

    /**
     * 获取枚举类的值
     *
     * @return Integer
     */
    Object getValue();

    /**
     * 获取枚举类的说明
     *
     * @return String
     */
    String getDesc();

    /**
     * 比较参数是否与枚举类的value相同
     *
     * @param value
     * @return boolean
     */
    default boolean equalsValue(Object value) {
        return Objects.equals(getValue(), value);
    }

    /**
     * 比较枚举类是否相同
     *
     * @param baseEnum
     * @return boolean
     */
    default boolean equals(BaseEnum baseEnum) {
        return Objects.equals(getValue(), baseEnum.getValue()) && Objects.equals(getDesc(), baseEnum.getDesc());
    }

    /**
     * 返回枚举类的说明
     *
     * @param clazz 枚举类类对象
     * @return
     */
    static String getInfo(Class<? extends BaseEnum> clazz) {
        BaseEnum[] enums = clazz.getEnumConstants();
        JSONArray jsonArray = new JSONArray();
        JSONObject enumJsonObject;
        JSONObject jsonObject;
        for (BaseEnum e : enums) {

            jsonObject = new JSONObject();
            jsonObject.put("value", e.getValue());
            jsonObject.put("desc", e.getDesc());

            enumJsonObject = new JSONObject();
            enumJsonObject.put(e.toString(), jsonObject);

            jsonArray.add(enumJsonObject);
        }
        return jsonArray.toString();
    }
}
