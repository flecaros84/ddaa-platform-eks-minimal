package com.ddaa.ddaaservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ddaaServiceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("DDAA Service API - Version simplificada")
                        .version("0.0.1")
                        .description("API de negocio simplificada para despliegue local y posterior despliegue en AWS EKS."))
                .servers(List.of(
                        new Server().url("http://localhost:8082").description("DDAA Service local directo"),
                        new Server().url("http://localhost:3000").description("Frontend local con proxy Nginx")
                ));
    }
}
