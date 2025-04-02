package com.bexos.third_service.controller;

import com.bexos.third_service.service.PropertyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class PropertyController {
    private final PropertyService propertyService;

    @Value("${test.users.admin.username}")
    private String username;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public String getUsername() {
        return username;
    }

    @GetMapping("/users")
    public PropertyService getPropertyService() {
        return propertyService;
    }
}
