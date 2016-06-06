package org.hspconsortium.platform.api.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{tenant}")
public class MultitenantManagementController {

    @RequestMapping("/management")
    public String management(@PathVariable("tenant") String tenant) {
        return "Management for " + tenant;
    }

}
