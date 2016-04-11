package org.hspconsortium.platform.api;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu1;
import ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.server.*;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import org.hspconsortium.platform.api.fhir.repository.MetadataRepository;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

public class HapiFhirServlet extends RestfulServer {

	private static final long serialVersionUID = 1L;

	private WebApplicationContext myAppCtx;

    private MetadataRepository metadataRepository;

	private String fhirMappingPath;

	public HapiFhirServlet() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initialize() throws ServletException {
		super.initialize();

		// get the context holder values
		myAppCtx = HapiFhirServletContextHolder.getInstance().getMyAppCtx();
		metadataRepository = HapiFhirServletContextHolder.getInstance().getMetadataRepository();
		fhirMappingPath = HapiFhirServletContextHolder.getInstance().getFhirMappingPath();

		/* 
		 * We want to support FHIR DSTU2 format. This means that the server
		 * will use the DSTU2 bundle format and other DSTU2 encoding changes.
		 *
		 * If you want to use DSTU1 instead, change the following line, and change the 2 occurrences of dstu2 in web.xml to dstu1
		 */
		FhirVersionEnum fhirVersion = FhirVersionEnum.DSTU2;
		setFhirContext(new FhirContext(fhirVersion));

		/*
		 * The hapi-fhir-server-resourceproviders-dev.xml file is a spring configuration
		 * file which is automatically generated as a part of hapi-fhir-jpaserver-base and
		 * contains bean definitions for a resource provider for each resource type
		 */
		String resourceProviderBeanName = "myResourceProvidersDstu" + (fhirVersion == FhirVersionEnum.DSTU1 ? "1" : "2");
		List<IResourceProvider> beans = myAppCtx.getBean(resourceProviderBeanName, List.class);
        setResourceProviders((List)beans);
		
		/* 
		 * The system provider implements non-resource-type methods, such as
		 * transaction, and global history.
		 */
		Object systemProvider;
		if (fhirVersion == FhirVersionEnum.DSTU1) {
			systemProvider = myAppCtx.getBean("mySystemProviderDstu1", JpaSystemProviderDstu1.class);
		} else {
			systemProvider = myAppCtx.getBean("mySystemProviderDstu2", JpaSystemProviderDstu2.class);
		}
		setPlainProviders(systemProvider);

		/*
		 * The conformance provider exports the supported resources, search parameters, etc for
		 * this server. The JPA version adds resource counts to the exported statement, so it
		 * is a nice addition.
		 */
		IFhirSystemDao<Bundle, MetaDt> systemDao = myAppCtx.getBean("mySystemDaoDstu2", IFhirSystemDao.class);
		HSPCConformanceProviderDstu2 confProvider = new HSPCConformanceProviderDstu2(this, systemDao, myAppCtx.getBean(DaoConfig.class), metadataRepository);

        confProvider.setImplementationDescription("HSPC FHIR Server");
        setServerConformanceProvider(confProvider);

		/*
		 * Enable ETag Support (this is already the default)
		 */
		setETagSupport(ETagSupportEnum.ENABLED);

		/*
		 * This server tries to dynamically generate narratives
		 */
		FhirContext ctx = getFhirContext();
		ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		/*
		 * This tells the server to use "browser friendly" MIME types if it 
		 * detects that the request is coming from a browser, in the hopes that the 
		 * browser won't just treat the content as a binary payload and try 
		 * to download it (which is what generally happens if you load a 
		 * FHIR URL in a browser). 
		 * 
		 * This means that the server isn't technically complying with the 
		 * FHIR specification for direct browser requests, but this mode
		 * is very helpful for testing and troubleshooting since it means 
		 * you can look at FHIR URLs directly in a browser.  
		 */
		setUseBrowserFriendlyContentTypes(true);

		/*
		 * Default to XML and pretty printing
		 */
		setDefaultPrettyPrint(true);
		setDefaultResponseEncoding(EncodingEnum.JSON);

		/*
		 * This is a simple paging strategy that keeps the last 10 searches in memory
		 */
		setPagingProvider(new FifoMemoryPagingProvider(10));

		/*
		 * Load interceptors for the server from Spring (these are defined in FhirServerConfig.java)
		 */
		Collection<IServerInterceptor> interceptorBeans = myAppCtx.getBeansOfType(IServerInterceptor.class).values();
		for (IServerInterceptor interceptor : interceptorBeans) {
			this.registerInterceptor(interceptor);
		}
	}

	/** account for tenant and mapping */
	@Override
	protected String getRequestPath(String requestFullPath, String servletContextPath, String servletPath) {

		// trim off the servletContextPath
		String remainder = requestFullPath.substring(escapedLength(servletContextPath));

		if (remainder.length() > 0 && remainder.charAt(0) == '/') {
			remainder = remainder.substring(1);
		}

		// followed by tenant and fhir mapping
		String[] split = remainder.split("/", 3);

		// capture the whole path after the fhir mapping
		StringBuffer stringBuffer = new StringBuffer();
		boolean foundFhirMappingPath = false;
		for (String part : split) {
			if (foundFhirMappingPath) {
				stringBuffer.append(part);
				stringBuffer.append("/");
			}
			if (fhirMappingPath.equals(part)) {
				foundFhirMappingPath = true;
			}
		}

		return stringBuffer.length() > 0
				? stringBuffer.substring(0, stringBuffer.length() - 1)
				: "";
	}

	/**
	 * Returns the server base URL (with no trailing '/') for a given request
	 */
	@Override
	public String getServerBaseForRequest(HttpServletRequest theRequest) {
		String fhirServerBase = getServerAddressStrategy().determineServerBase(getServletContext(), theRequest);

		String[] split = fhirServerBase.split("/");

		StringBuffer result = new StringBuffer();
		for (String current : split) {
			result.append(current);

			if (fhirMappingPath.equals(current)) {
				// found the base for request
				return result.toString();
			}

			// continue
			result.append("/");
		}

		throw new RuntimeException("Something bad happened, only matched: " + result.toString());
	}

}
