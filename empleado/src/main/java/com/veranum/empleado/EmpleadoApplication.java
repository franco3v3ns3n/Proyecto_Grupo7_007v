package com.veranum.empleado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class EmpleadoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmpleadoApplication.class, args);
    }
}
