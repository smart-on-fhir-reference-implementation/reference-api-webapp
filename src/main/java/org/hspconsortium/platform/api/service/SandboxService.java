package org.hspconsortium.platform.api.service;

import org.apache.commons.lang3.Validate;
import org.hspconsortium.platform.api.model.Sandbox;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class SandboxService {

    private Map<String, Sandbox> sandboxes;

    public SandboxService() {
        sandboxes = new HashMap<>();
        sandboxes.put(Sandbox.HSPC.getTeamId(), Sandbox.HSPC);
    }

    public Collection<Sandbox> all() {
        return sandboxes.values();
    }

    public Sandbox addOrReplace(String teamId, Sandbox sandbox) {
        Validate.isTrue(sandbox != null, "Sandbox must be provided");
        Validate.isTrue(teamId.equals(sandbox.getTeamId()), "Sandbox is not consistent with teamId");

        sandboxes.put(teamId, sandbox);

        return sandbox;
    }

    public Sandbox get(String teamId) {
        Sandbox found = sandboxes.get(teamId);
        return found != null ? found : Sandbox.UNKNOWN;
    }

}
