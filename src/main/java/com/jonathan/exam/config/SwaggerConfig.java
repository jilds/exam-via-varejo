package com.jonathan.exam.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .produces(Collections.singleton("application/json"))
                .consumes(Collections.singleton("application/json"))
                .select().apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .build();

    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("ID Cotton Integration REST API")
                .contact(this.contato())
                .build();
    }

    private Contact contato() {
        return new Contact("Jonathan - Java Developer", "", "jilds@outlook.com");
    }
}