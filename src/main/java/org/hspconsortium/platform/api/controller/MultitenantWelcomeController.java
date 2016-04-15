package org.hspconsortium.platform.api.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{tenant}")
public class MultitenantWelcomeController {

    @RequestMapping("/")
    public String management(@PathVariable("tenant") String tenant) {
        return "Welcome to the FHIR server for " + tenant;
    }

}
