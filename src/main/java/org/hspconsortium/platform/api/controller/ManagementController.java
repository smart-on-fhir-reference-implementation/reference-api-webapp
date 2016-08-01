package org.hspconsortium.platform.api.controller;

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hspconsortium.platform.api.model.Sandbox;
import org.hspconsortium.platform.api.service.SandboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/management")
public class ManagementController {

    @Autowired
    private SandboxService sandboxService;

    @RequestMapping("/")
    public String management() {
        return "Management endpoint";
    }

    @RequestMapping(value = "/sandbox", method = RequestMethod.GET)
    public Collection<Sandbox> all() {
        return sandboxService.all();
    }

    @RequestMapping(value = "/sandbox/{teamId}", method = RequestMethod.PUT)
    public Sandbox create(@PathVariable String teamId, @RequestBody Sandbox sandbox) {
        return sandboxService.addOrReplace(teamId, sandbox);
    }

    @RequestMapping(value = "/sandbox/{teamId}", method = RequestMethod.GET)
    public Sandbox get(@PathVariable String teamId) {
        Sandbox found = sandboxService.get(teamId);
        if (found.equals(Sandbox.UNKNOWN)) {
            throw new ResourceNotFoundException("Sandbox {" + teamId + "} is not found");
        }
        return found;
    }

    @RequestMapping(value = "/sandbox/{teamId}", method = RequestMethod.DELETE)
    public boolean delete(@PathVariable String teamId) {
        return sandboxService.remove(teamId);
    }

}
