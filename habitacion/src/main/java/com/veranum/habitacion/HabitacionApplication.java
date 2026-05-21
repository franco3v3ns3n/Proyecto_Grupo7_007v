package com.veranum.habitacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class HabitacionApplication {

    public static void main(String[] args) {
        SpringApplication.run(HabitacionApplication.class, args);
    }
}
