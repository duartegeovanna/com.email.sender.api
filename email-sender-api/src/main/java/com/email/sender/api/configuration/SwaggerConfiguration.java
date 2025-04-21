package com.email.sender.api.configuration;

import com.email.sender.api.properties.SwaggerProperties;
import io.micrometer.core.instrument.util.IOUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Configuration
@AllArgsConstructor
public class SwaggerConfiguration implements WebMvcConfigurer {

    private final SwaggerProperties swaggerProperties;

    static {
        var schema = new Schema<LocalTime>();
        schema.example(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .type("string");
        SpringDocUtils.getConfig().replaceWithSchema(LocalTime.class, schema);
    }

    @Bean
    public OpenAPI customOpenAPI(SwaggerProperties swaggerProperties) {
        return new OpenAPI().components(new Components())
                .info(new Info().title(swaggerProperties.getTitle())
                        .description(swaggerProperties.getDescription()))
                .servers(Collections.singletonList(new Server().url(swaggerProperties.getApiUrl())));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**/*.html")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .resourceChain(false)
                .addResolver(new WebJarsResourceResolver())
                .addResolver(new PathResourceResolver())
                .addTransformer(new IndexPageTransformer());
    }

    public class IndexPageTransformer implements ResourceTransformer {

        private String overwriteDefaultUrl(String html) {
            return html.replace("https://petstore.swagger.io/v2/swagger.json",
                    swaggerProperties.getUrl());
        }

        @Override
        public Resource transform(HttpServletRequest httpServletRequest, Resource resource,
                                  ResourceTransformerChain resourceTransformerChain) throws IOException {
            if (!resource.getURL().toString().endsWith("/index.html")) {
                return resource;
            }
            String html = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
            html = overwriteDefaultUrl(html);
            return new TransformedResource(resource, html.getBytes());
        }
    }
}
