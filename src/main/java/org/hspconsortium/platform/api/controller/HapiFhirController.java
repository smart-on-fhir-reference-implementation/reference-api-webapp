package org.hspconsortium.platform.api.controller;

import org.hspconsortium.platform.api.fhir.repository.MetadataRepository;
import org.hspconsortium.platform.api.smart.LaunchOrchestrationEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.ServletWrappingController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

@RestController
@Profile("singletenant")
public class HapiFhirController extends ServletWrappingController {

    @Autowired
    private WebApplicationContext myAppCtx;

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private LaunchOrchestrationEndpoint launchOrchestrationEndpoint;

    @Autowired
    public HapiFhirController(WebApplicationContext myAppCtx, MetadataRepository metadataRepository,
                              @Value("${hspc.platform.api.fhir.contextPath}") String fhirContextPath) {
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
        HapiFhirServletContextHolder.getInstance().init(myAppCtx, metadataRepository, fhirContextPath);
    }

    @Override
    public void setInitParameters(Properties initParameters) {
        super.setInitParameters(initParameters);
    }

    @RequestMapping(value = {"/${hspc.platform.api.fhir.contextPath}", "/${hspc.platform.api.fhir.contextPath}/**"})
    public void handleChild(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.handleRequest(request, response);
    }

    @RequestMapping(value = "/${hspc.platform.api.fhir.contextPath}/_services/smart/Launch", method = RequestMethod.GET)
    public String smartLaunchHello(HttpServletRequest request, HttpServletResponse response) {
        return launchOrchestrationEndpoint.hello(request, response);
    }

    @RequestMapping(value = "/${hspc.platform.api.fhir.contextPath}/_services/smart/Launch", method = RequestMethod.POST)
    public void smartLaunch(HttpServletRequest request, HttpServletResponse response, @RequestBody String jsonString) {
        launchOrchestrationEndpoint.handleLaunchRequest(request, response, jsonString);
    }
}
