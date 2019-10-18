package com.anonym.module.test;

import java.util.Optional;

/**
 * @author lizongliang
 * @date 2019-10-18 10:08
 */
public class OptionalTest {
    public static void main(String[] args) {

        // 1. of  为非null的值创建一个Optional
        //调用工厂方法创建Optional实例
        Optional<String> name = Optional.of("Sanaulla");
        //传入参数为null，抛出NullPointerException.
//        Optional<String> someNull = Optional.of(null);
        // 2. ofNullable  为指定的值创建一个Optional，如果指定的值为null，则返回一个空的Optional。
        Optional empty = Optional.ofNullable(null);
    }
}
