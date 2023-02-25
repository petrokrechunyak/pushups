package com.alphabetas.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@SpringBootApplication
public class CallerRemakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CallerRemakeApplication.class, args);
    }

}
