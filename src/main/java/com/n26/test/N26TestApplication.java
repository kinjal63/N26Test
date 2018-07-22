package com.n26.test;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by kinjal.patel on 21/7/18
 */
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages="com.n26.test")
public class N26TestApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(N26TestApplication.class, args);
	}
}
