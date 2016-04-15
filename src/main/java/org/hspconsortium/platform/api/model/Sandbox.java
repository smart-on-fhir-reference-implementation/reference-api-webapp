package org.hspconsortium.platform.api.model;

public class Sandbox {

    public static final Sandbox HSPC = new Sandbox("hspc");

    public static final Sandbox UNKNOWN = new Sandbox("unknown");

    public String teamId;

    protected Sandbox() {
    }

    public Sandbox(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamId() {
        return teamId;
    }

    protected void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "Sandbox{" +
                "teamId='" + teamId + '\'' +
                '}';
    }
}
