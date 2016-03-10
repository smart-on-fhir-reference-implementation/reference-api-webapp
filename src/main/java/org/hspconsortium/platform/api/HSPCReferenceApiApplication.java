package org.hspconsortium.platform.api;

import org.hspconsortium.platform.api.fhir.repository.MetadataRepository;
import org.hspconsortium.platform.api.oauth2.MethodSecurityConfig;
import org.hspconsortium.platform.api.oauth2.OAuth2ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@EnableAutoConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, /*securedEnabled = true, */proxyTargetClass = true)
@Import({OAuth2ResourceConfig.class, MethodSecurityConfig.class})
@ComponentScan({"org.hspconsortium.platform.api"})
public class HSPCReferenceApiApplication extends SpringBootServletInitializer {

    @Autowired
    private WebApplicationContext myAppCtx;

    @Autowired
    private MetadataRepository metadataRepository;

    public static void main(String[] args) {
        SpringApplication.run(HSPCReferenceApiApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(HSPCReferenceApiApplication.class);
    }

    @Bean
    public HapiFhirServlet hapiFhirServer() {
        return new HapiFhirServlet(myAppCtx, metadataRepository);
    }

    @Bean
    @Autowired
    public ServletRegistrationBean hapiFhirServletRegistrationBean(HapiFhirServlet hapiFhirServlet) {
        return new ServletRegistrationBean(hapiFhirServlet, "/data/*");
    }

    @Bean
    @Autowired
    public ServletRegistrationBean smartServicesRegistrationBean(DispatcherServlet dispatcherServlet) {
        return new ServletRegistrationBean(dispatcherServlet, "/data/_services/smart/*");
    }
}
