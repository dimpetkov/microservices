package com.officemap.authorization;

import com.officemap.authorization.userlogin.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.Valid;

import static com.officemap.common.Url.URL_V2;

@RestController
@Tag(name = "Authorization Token Controller",
        description = "Contains JWT validation. To generate a token, use the Login API - localhost:8091/api/v1/auth/login")
@RequestMapping(URL_V2)
public class AuthorizationController {

    WebClient authorizationWebClient = WebClient.builder()
//            .baseUrl("http://employee-management-auth:8091")
            .baseUrl("http://localhost:8091")
            .build();

    @Operation(summary = "Verify token validity")
    @PostMapping("authorize")
    public Object verifyTokenValidity(@RequestHeader("Authorization") String token) {
       return authorizationWebClient.post().uri("api/v1/secured/tokenValidator")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Operation(summary = "Login functionality, for testing purposes")
    @PostMapping("login")
    public Object loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        return authorizationWebClient.post().uri("api/v1/auth/login")
                .bodyValue(loginRequest)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}
