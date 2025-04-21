package com.email.sender.api.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "springdoc.swagger-ui")
public class SwaggerProperties {

    private String title;
    private String description;
    private String apiUrl;
    private String url;
}
