package com.co.uniquindio.web.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Servicio de Compras API",
                "API que permite realizar, eliminar, listar, revisar historial de compras",
                "0.0.1-SNAPSHOT",
                "http://localhost:8080/compras",
                new Contact(
                        "Universidad del Quindio", "https://www.uniquindio.edu.co/", "juand.naranjos@uqvirtual.edu.co"),
                "LICENSE",
                "LICENSE URL",
                Collections.emptyList()
        );

    }
}
