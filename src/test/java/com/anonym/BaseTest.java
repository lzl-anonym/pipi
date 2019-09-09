package com.anonym;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTest {

    @Before
    public void before() {
        System.out.println("=====================Test Start=====================");
    }

    @After
    public void after() {
        System.out.println("======================Test End=====================");
    }

}