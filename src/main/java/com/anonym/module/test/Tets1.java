package com.anonym.module.test;

/**
 * @author lizongliang
 * @date 2019-11-12 10:27
 */
public class Tets1 {


    public static void main(String[] args) {
        System.out.println(sum(10));
    }

    private static int sum(int n) {
        if (n == 1) {
            return n;
        } else {
            return n + sum(n - 1);

        }
    }


}
