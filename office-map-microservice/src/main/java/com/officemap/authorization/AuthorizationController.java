package com.officemap.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RestController
public class AuthorizationController {
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public AuthorizationController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @PostMapping
    public void verifyTokenValidity(String authorizationToken) {
        String response =  webClientBuilder.build().post()
                .uri("http://employee-management-auth:8091/api/v1/secured/tokenValidator")
                .header("Authorization", authorizationToken)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == HttpStatus.UNAUTHORIZED.value(),
                        clientResponse -> Mono.error(new UnauthorizedTokenException("UNAUTHORIZED TOKEN")))
                .bodyToMono(String.class)
                .block();
        ResponseEntity.ok(response);
    }
    /* Token is valid
    * Status code 200 */
}
