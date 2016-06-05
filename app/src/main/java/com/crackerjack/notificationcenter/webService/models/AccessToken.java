package com.crackerjack.notificationcenter.webService.models;

/**
 * Created by pratik on 05/06/16.
 */

public class AccessToken {
    private String id;
    private long ttl;
    private String created;
    private long eId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public long geteId() {
        return eId;
    }

    public void seteId(long eId) {
        this.eId = eId;
    }

}