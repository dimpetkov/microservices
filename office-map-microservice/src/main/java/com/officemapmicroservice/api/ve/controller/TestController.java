package com.officemapmicroservice.api.ve.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class TestController {
    @GetMapping("/")
    public boolean isConnected() {
        return true;
    }
}
