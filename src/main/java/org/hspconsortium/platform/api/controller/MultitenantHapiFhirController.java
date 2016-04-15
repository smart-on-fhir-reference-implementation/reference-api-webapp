package org.hspconsortium.platform.api.controller;

import org.hspconsortium.platform.api.fhir.repository.MetadataRepository;
import org.hspconsortium.platform.api.smart.LaunchOrchestrationEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.ServletWrappingController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

@RestController
@RequestMapping("/{tenant}")
public class MultitenantHapiFhirController extends ServletWrappingController {

    @Autowired
    private WebApplicationContext myAppCtx;

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private LaunchOrchestrationEndpoint launchOrchestrationEndpoint;

    @Autowired
    public MultitenantHapiFhirController(WebApplicationContext myAppCtx, MetadataRepository metadataRepository) {
        setServletClass(HapiFhirServlet.class);
        setServletName("hapiFhirServlet");
        setSupportedMethods(
                RequestMethod.GET.toString(),
                RequestMethod.PUT.toString(),
                RequestMethod.POST.toString(),
                RequestMethod.PATCH.toString(),
                RequestMethod.DELETE.toString(),
                RequestMethod.HEAD.toString(),
                RequestMethod.OPTIONS.toString(),
                RequestMethod.TRACE.toString()
        );
        HapiFhirServletContextHolder.getInstance().init(myAppCtx, metadataRepository, "data");
    }

    @Override
    public void setInitParameters(Properties initParameters) {
        super.setInitParameters(initParameters);
    }

    @RequestMapping(value = {"/data", "/data/**"})
    public void handle(@PathVariable("tenant") String tenant, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        this.handleRequest(request, response);
    }

    @RequestMapping(value = "/data/_services/smart/Launch", method = RequestMethod.GET)
    public String smartLaunchHello(HttpServletRequest request, HttpServletResponse response) {
        return launchOrchestrationEndpoint.hello(request, response);
    }

    @RequestMapping(value = "/data/_services/smart/Launch", method = RequestMethod.POST)
    public void smartLaunch(HttpServletRequest request, HttpServletResponse response, @RequestBody String jsonString) {
        launchOrchestrationEndpoint.handleLaunchRequest(request, response, jsonString);
    }
}
