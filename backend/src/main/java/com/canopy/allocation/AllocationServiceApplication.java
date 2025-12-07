package com.canopy.allocation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*

This is the main entry point of spring boot application. 
When program starts, spring boot sets up the application,
loads all configurations, and runs the Allocation service backend.

*/

@SpringBootApplication
public class AllocationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AllocationServiceApplication.class, args);
    }
}
