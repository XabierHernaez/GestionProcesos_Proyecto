package com.example.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @class RestApiApplication
 * @brief Clase principal que arranca la aplicación Spring Boot.
 *
 * Esta clase contiene el método main, que es el punto de entrada de la aplicación.
 * Se encarga de iniciar todo lo relacionado con Springboot.
 */
@SpringBootApplication
public class RestApiApplication {

    /**
     * @brief Método principal para iniciar la aplicación.gi
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }
}

