package com.ait.cornjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringCornApplication  {

    private static final Logger log = LoggerFactory.getLogger(SpringCornApplication.class);
    
    public static void main(String args[]) {
        SpringApplication.run(SpringCornApplication.class);
    }
}