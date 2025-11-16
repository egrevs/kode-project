package com.egrevs.project.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Kode Project API")
                        .description("API для демо проекта")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("egrevs")
                        )
                )
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Local Development Server")
                );
    }
}
