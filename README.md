# SPRING BOOT ACTUATOR
This project demonstrates how to dynamically refresh configuration properties in Spring Boot applications using the `/actuator/refresh` endpoint from Spring Cloud Actuator.

## Overview
The project includes:

#### Config Server
Hosts the external configuration files. It acts as the centralized source of configuration.

#### First Service
Consumes configuration using @ConfigurationProperties. Ideal for structured and grouped properties.

**Configuration class**
```java
@Component
@ConfigurationProperties(prefix = "test")
public class PropertyService {
    private String env;
    private List<String> servers;
    private Credentials credentials;

    // Getters & Setters
}
```
**Properties**
```yaml
test:
  env: dev
  servers:
    - server1.example.com
    - server2.example.com
    - server3.example.com
  credentials:
    username: user
    password: secret
    roles:
      dev: developer
      qa: tester
      prod: admin
```
#### Second Service
Consumes configuration using @Value, combined with @RefreshScope to support dynamic refresh.

**Configuration class**
```java
@RestController
@RefreshScope
@RequestMapping("/test")
public class PropertyController {
    @Value("${test.env}")
    private String env;

    @Value("${test.databases[0]}")
    private String server1;
    
    // etc .....
}
```
**Properties**
```yaml
test:
  env: dev
  servers: server1.example.com,server2.example.com,server3.example.com
  databases:
    - mysql
    - postgres
    - dynamoDB
  credentials:
    username: user
    password: secret
    roles:
      dev: developer
      qa: tester
      prod: admin
```


## Key Concepts
### 🔁 /actuator/refresh
Spring Cloud Actuator provides the /actuator/refresh endpoint to refresh the application context and rebind configuration properties at runtime.

### 🧩 Binding Strategies
| Service        | Binding Method           | Scope Required?     | Notes                                          |
|----------------|--------------------------|---------------------|------------------------------------------------|
| First Service  | @ConfigurationProperties | No additional scope | Automatically updated after /actuator/refresh  |
| Second Service | @Value                   | @RefreshScope       | Required to support runtime update via refresh |

## How It Works
1. On startup, each service fetches its configuration from the Config Server.
2. Update configuration properties and re-run in the Config Server
3. Trigger a refresh using:
```
curl -X POST http://localhost:{PORT}/actuator/refresh
```
4. The service context is refreshed, and the new values are picked up according to the binding strategy used.

## Requirements
- Java 21+
- Spring Boot
- Spring Cloud (Actuator & Config Client)
- Maven


