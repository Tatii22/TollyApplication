package com.rentaherramientas.tolly.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para la API
 * Plataforma de Renta de Herramientas y Equipos de Construcción - Tolly
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
            .info(new Info()
                .title("Tolly API – Plataforma de Renta de Herramientas")
                .version("1.0.0")
                .description("""
                    API REST para la Plataforma de Renta de Herramientas y Equipos de Construcción **Tolly**.

                    ## Autenticación
                    La API utiliza **JWT (JSON Web Tokens)** para la autenticación y autorización de usuarios.

                    ### Flujo de autenticación:
                    1. Registro de usuario: `POST /auth/register`
                    2. Inicio de sesión: `POST /auth/login`
                    3. Uso del token en los endpoints protegidos:
                       `Authorization: Bearer <access_token>`

                    ## Refresh Token
                    Cuando el access token expire, se puede obtener uno nuevo usando:
                    `POST /auth/refresh`

                    ## Roles del sistema
                    - **ROLE_ADMIN**: Administrador de la plataforma
                    - **ROLE_PROVEEDOR**: Proveedor de herramientas
                    - **ROLE_CLIENTE**: Cliente que realiza alquileres
                    """)
                .contact(new Contact()
                    .name("Equipo Tolly")
                    .email("soporte@tolly.com"))
                .license(new License()
                    .name("Uso Educativo")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:" + serverPort)
                    .description("Servidor de desarrollo"),
                new Server()
                    .url("https://api.tolly.com")
                    .description("Servidor de producción")))
            .addSecurityItem(new SecurityRequirement()
                .addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT obtenido al iniciar sesión")));
    }
}
