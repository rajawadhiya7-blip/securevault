package com.securevault.securevault.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SecureVault API")
                        .version("1.0")
                        .description("Enterprise secrets and credential management"))
                .tags(List.of(
                        new Tag().name("1. Authentication").description("Register and login endpoints"),
                        new Tag().name("2. Secrets").description("Secret management endpoints"),
                        new Tag().name("3. User").description("User profile endpoints")
                ));
    }
}