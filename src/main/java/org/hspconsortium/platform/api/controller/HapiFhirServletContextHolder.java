package org.hspconsortium.platform.api.controller;

import org.hspconsortium.platform.api.fhir.repository.MetadataRepository;
import org.springframework.web.context.WebApplicationContext;

public class HapiFhirServletContextHolder {

    private static HapiFhirServletContextHolder hapiFhirServletContextHolder = new HapiFhirServletContextHolder();

    public static HapiFhirServletContextHolder getInstance() {
        return hapiFhirServletContextHolder;
    }

    private WebApplicationContext myAppCtx;

    private MetadataRepository metadataRepository;

    private String fhirMappingPath;

    public void init(WebApplicationContext myAppCtx, MetadataRepository metadataRepository, String fhirMappingPath) {
        this.myAppCtx = myAppCtx;
        this.metadataRepository = metadataRepository;
        this.fhirMappingPath = fhirMappingPath;
    }

    public WebApplicationContext getMyAppCtx() {
        return myAppCtx;
    }

    public MetadataRepository getMetadataRepository() {
        return metadataRepository;
    }

    public String getFhirMappingPath() {
        return fhirMappingPath;
    }
}
