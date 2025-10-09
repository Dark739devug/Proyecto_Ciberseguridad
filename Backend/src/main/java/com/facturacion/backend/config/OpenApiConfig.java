package com.facturacion.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Sistema de Facturación",
                version = "1.0",
                description = "Documentación de la API REST para el Sistema de Facturación, incluyendo autenticación JWT."
        )
)
@SecurityScheme(
        name = "bearerAuth", // Nombre de referencia que usaremos en los controladores
        type = SecuritySchemeType.HTTP,
        scheme = "bearer", // Indica que es un esquema de Bearer Token
        bearerFormat = "JWT" // Indica el formato del token
)
public class OpenApiConfig {
    // Esta clase solo necesita las anotaciones para configurar Swagger/OpenAPI
}