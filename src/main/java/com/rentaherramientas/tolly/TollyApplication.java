package com.rentaherramientas.tolly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Tolly
 *
 * Arquitectura Hexagonal con Spring Boot 3.3.0 y Spring Security 6
 */
@SpringBootApplication
public class TollyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TollyApplication.class, args);
    }
}
