package com.ssw.linkedinmanager.dto;

import java.io.Serializable;

public class LinkedInTokenValidationSuccessBean implements Serializable {
    public static final long serialVersionUID = 12345678904354321L;

    /**
     * active : true
     * client_id : 81e7uqgktjdm8y
     * authorized_at : 1596177723
     * created_at : 1596177723
     * status : active
     * expires_at : 1601361725
     * scope : r_emailaddress,r_liteprofile
     */

    private boolean active;
    private String client_id;
    private int authorized_at;
    private int created_at;
    private String status;
    private int expires_at;
    private String scope;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public int getAuthorized_at() {
        return authorized_at;
    }

    public void setAuthorized_at(int authorized_at) {
        this.authorized_at = authorized_at;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(int expires_at) {
        this.expires_at = expires_at;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
