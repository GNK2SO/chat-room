package com.gnk2so.chatroom.config.doc;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

  public static final String[] RESOURCES = { 
		"/swagger-resources/**", 
		"/swagger-ui.html", 
    "/v2/api-docs",
    "/webjars/**"
  };

  @Bean
  public Docket greetingApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        .build()
        .consumes(new HashSet<>(Arrays.asList("application/json")))
        .produces(new HashSet<>(Arrays.asList("application/json")))
        .useDefaultResponseMessages(false)
        .apiInfo(metaData())
        .securitySchemes(Arrays.asList(apiKey()));
  }

  private ApiInfo metaData() {
    return new ApiInfoBuilder()
        .title("Token-Based Authentication")
        .description("The API is organized around REST, using standard HTTP protocol codes and verbs and JWT authentication.")
        .version("1.0.0")
        .license("Apache License Version 2.0")
        .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
        .build();
  }

  private ApiKey apiKey() {
    return new ApiKey("JWT", "Authorization", "header");
  }

}