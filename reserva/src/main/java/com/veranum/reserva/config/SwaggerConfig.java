package com.veranum.reserva.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration permite que Spring reconozca esta clase como fuente de configuración.
@Configuration
public class SwaggerConfig {

    // @Bean registra en Spring la configuración general de OpenAPI usada por Swagger UI.
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Microservicio Reserva")
                        .version("1.0")
                        .description("Documentación de la API REST del microservicio reserva de Hoteles Veranum"));
    }
}