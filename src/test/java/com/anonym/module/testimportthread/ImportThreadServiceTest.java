package com.anonym.module.testimportthread;

import com.anonym.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ImportThreadServiceTest extends BaseTest {

    @Autowired
    private ImportThreadService importThreadService;

    @Test
    public void handleData() {

        importThreadService.handleData();

    }
}