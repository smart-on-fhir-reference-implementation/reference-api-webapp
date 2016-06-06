package org.hspconsortium.platform.api.service;

import org.apache.commons.lang3.Validate;
import org.hspconsortium.platform.api.model.Sandbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class SandboxService {

    private Map<String, Sandbox> sandboxes;

    @Autowired
    private SandboxPersister sandboxPersister;

    public SandboxService() {
        sandboxes = new HashMap<>();
        sandboxes.put(Sandbox.HSPC.getTeamId(), Sandbox.HSPC);
    }

    public Collection<Sandbox> all() {
        return sandboxPersister.getSandboxes();
    }

    public Sandbox addOrReplace(String teamId, Sandbox sandbox) {
        Validate.isTrue(sandbox != null, "Sandbox must be provided");
        Validate.isTrue(teamId.equals(sandbox.getTeamId()), "Sandbox is not consistent with teamId");

        Collection<Sandbox> existing = sandboxPersister.getSandboxes();
        for (Sandbox existingSandbox : existing) {
            if (existingSandbox.teamId.equals(teamId)) {
                return existingSandbox;
            }
        }

        return sandboxPersister.createSandbox(sandbox);
    }

    public Sandbox get(String teamId) {
        Sandbox found = sandboxes.get(teamId);
        return found != null ? found : Sandbox.UNKNOWN;
    }

}
