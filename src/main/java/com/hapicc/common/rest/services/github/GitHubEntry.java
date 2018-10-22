package com.hapicc.common.rest.services.github;

public class GitHubEntry {

    private String oid;
    private String name;
    private String type;

    public GitHubEntry() {
    }

    public GitHubEntry(String oid, String name, String type) {
        this.oid = oid;
        this.name = name;
        this.type = type;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
