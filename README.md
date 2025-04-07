# SPRING BOOT ACTUATOR
This project demonstrates how to dynamically refresh configuration properties in Spring Boot applications using the `/actuator/refresh` endpoint from Spring Cloud Actuator and `/actuator/busrefresh` endpoint from Spring Cloud Bus AMQP .

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

#### Third Service
Consumes configuration using @ConfigurationProperties.
**Configuration class**
```java
@Component
@ConfigurationProperties(prefix = "test")
public class PropertyService {
    Map<String, User> users;
    
    // Getters and Setters

    public static class User {
        String username;
        String email;
        List<String> permissions;
        
        // Getters and Setters
    }
    
}
```

**Properties**
```yaml
test:
  users:
    admin:
      username: "super-admin"
      email: "admin@example.com"
      permissions: ["read", "write", "delete"]
    guest:
      username: "visitor"
      email: null
      permissions: ["read"]
```


#### Fourth Service
Consumes configuration using @ConfigurationProperties.
**Configuration class**
```java
@Component
@ConfigurationProperties(prefix = "app")
public class PropertyService {
    private String name;
    private String version;
    private boolean production;
    private MetaData metaData;
    
    // Getters and Setters

    private static class MetaData {
        private LocalDate release_date;
        private String author;
        private List<String> tags;
        private List<Map<String, String>> dependencies;

        // Getters and Setters
    }
}
```

**Properties**
```yaml
app:
  name: "AwesomeApp"
  version: 4.0
  production: false
  metadata:
    release_date: 2025-04-15
    author: "Jane Doe"
    tags: ["web", "cloud", "saas"]
    dependencies:
      - name: "react"
        version: "^18.2.0"
      - name: "nodejs"
        version: "20.x"
```


## Key Concepts
### ⚠️ Not All Configurations Are Dynamically Refreshable
Certain system-level or static configurations (e.g., logging levels, server ports, classpath-injected properties) may require an application restart to apply changes.

### 🧩 Binding Strategies
| Service                                      | Binding Method           | Scope Required?     | Notes                                          |
|----------------------------------------------|--------------------------|---------------------|------------------------------------------------|
| First Service, Third Service, Fourth Service | @ConfigurationProperties | No additional scope | Automatically updated after /actuator/refresh  |
| Second Service                               | @Value                   | @RefreshScope       | Required to support runtime update via refresh |

### 🔁 /actuator/refresh
Spring Cloud Actuator provides the /actuator/refresh endpoint to refresh the application context and rebind configuration properties at runtime.  
- Refreshes only the targeted instance.

#### How It Works
1. Run the Config Server using:
```
mvn spring-boot:run
```
2. Set the environment variable for First and Second Services application.yml. Example
```
CONFIG_SERVER_URL=http://localhost:8888
```
3. Run the First and Second Service using:
```
mvn spring-boot:run
```
4. Update configuration properties and re-run in the Config Server
5. Trigger a refresh to apply the new configuration using:
NB: Replace {PORT} with the actual port of either First or Second Service.  
- Request:
```
curl -X POST http://localhost:{PORT}/actuator/refresh
```
- Example of response from server:
```
Refreshed keys : [test.env]
```
6. The targeted service context is refreshed, and the new values are applied.

### 🔁 /actuator/busrefresh
Refreshes all instances in the system via Spring Cloud Bus (e.g., using RabbitMQ or Kafka).

#### How It Works
1. Start RabbitMQ using the docker command:
```
docker-compose up -d
```
2. Run the Config Server using:
```
mvn spring-boot:run
```
3. Set the environment variable for First and Second Services application.yml. Example
```
CONFIG_SERVER_URL=http://localhost:8888
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=user
RABBITMQ_PASSWORD=password
```
4. Run the Third and Fourth Service using:
```
mvn spring-boot:run
```
5. Update configuration properties and re-run in the Config Server
6. Trigger a bus-wide refresh from any service:
   NB: Replace {PORT} with the actual port of either Third or Fourth Service.
- Request
```
curl -X POST http://localhost:{PORT}/actuator/busrefresh
```
- Example of response from server:
```
Keys refreshed [test.users.admin.username]
```
7. Spring Cloud Bus propagates the refresh event to all services, and each one reloads its context.

## Requirements
- Java 21+
- Spring Boot
- Spring Cloud (Actuator & Config Client)
- Maven


