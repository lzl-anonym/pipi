package com.anonym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author lizongliang
 */
@EnableSwagger2
@SpringBootApplication
public class AnonymApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnonymApplication.class, args);
	}

}
