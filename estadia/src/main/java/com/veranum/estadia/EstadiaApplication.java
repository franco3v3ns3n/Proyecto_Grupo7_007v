package com.veranum.estadia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class EstadiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EstadiaApplication.class, args);
    }
}
