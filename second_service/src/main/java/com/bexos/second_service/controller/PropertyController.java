package com.bexos.second_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RefreshScope
@RequestMapping("/test")
public class PropertyController {
    @Value("${test.env}")
    private String env;

    @Value("${test.databases[0]}")
    private String server1;

    @Value("${test.databases[1]}")
    private String server2;

    @Value("${test.databases[2]}")
    private String server3;

    @Value("#{'${test.servers}'.split(',')}")
    private List<String> servers;

    @Value("${test.credentials.username}")
    private String username;

    @Value("${test.credentials.password}")
    private String password;

    @GetMapping("/env")
    public String getEnv() {
        return env;
    }

    @GetMapping("/server-1")
    public String getServer1() {
        return server1;
    }

    @GetMapping("/server-2")
    public String getServer2() {
        return server2;
    }

    @GetMapping("/server-3")
    public String getServer3() {
        return server3;
    }

    @GetMapping("/servers")
    public List<String> getServers() {
        return servers;
    }

    @GetMapping("/username")
    public String getUsername() {
        return username;
    }

    @GetMapping("/password")
    public String getPassword() {
        return password;
    }
}
