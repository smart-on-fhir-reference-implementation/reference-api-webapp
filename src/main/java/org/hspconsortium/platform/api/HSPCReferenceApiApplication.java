package org.hspconsortium.platform.api;

import org.hspconsortium.platform.api.oauth2.MethodSecurityConfig;
import org.hspconsortium.platform.api.oauth2.OAuth2ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableAutoConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, /*securedEnabled = true, */proxyTargetClass = true)
@Import({OAuth2ResourceConfig.class, MethodSecurityConfig.class})
@ComponentScan({"org.hspconsortium.platform.api"})
public class HSPCReferenceApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(HSPCReferenceApiApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(HSPCReferenceApiApplication.class);
    }
}
