package com.madadipouya.workforce.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket productApi() {
        List<SecurityScheme> schemeList = new ArrayList<>();
        schemeList.add(new BasicAuth("basicAuth"));

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.madadipouya.workforce.rest.v1"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metadata())
                .securitySchemes(schemeList);
    }

    private ApiInfo metadata() {
        return new ApiInfo(
                "Workforce API documentation",
                "Documentation for Workforce API",
                "v1",
                "Free of charge",
                new Contact("Kasra Madadipouya", "http://workforce.madadipouya.com", "kasra_mp@live.com"),
                "", "", Collections.emptyList());
    }
}
