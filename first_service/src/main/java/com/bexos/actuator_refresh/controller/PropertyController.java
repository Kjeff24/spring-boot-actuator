package com.bexos.actuator_refresh.controller;

import com.bexos.actuator_refresh.service.PropertyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class PropertyController {
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/env")
    public String env() {
        return propertyService.getEnv();
    }

    @GetMapping("/server")
    public List<String> server() {
        return propertyService.getServers();
    }

    @GetMapping("/credentials")
    public PropertyService.Credentials credentials() {
        return propertyService.getCredentials();
    }
}
