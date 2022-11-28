package com.officemap.swaggerui;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Authorization",
        description = "A JWT token is required to access this API. Token can be obtained by accessing the Login API and providing correct username and password. ",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer")
@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(
        title = "Office-Map-Employee-Placement API",
        version = "2.0",
        description = "Office Map and Employees Placements structure"))
public class OpenAPI30Configuration {

}
