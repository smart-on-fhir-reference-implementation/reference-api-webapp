package org.hspconsortium.platform.api.controller;

import org.hspconsortium.platform.api.model.Sandbox;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @RequestMapping("/")
    public String management() {
        return "Welcome to the FHIR server for " + Sandbox.HSPC;
    }

}
