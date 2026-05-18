package com.veranum.servicio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ServicioApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioApplication.class, args);
    }
}