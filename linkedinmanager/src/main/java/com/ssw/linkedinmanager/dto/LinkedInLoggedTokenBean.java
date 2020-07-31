package com.ssw.linkedinmanager.dto;

import java.io.Serializable;

public class LinkedInLoggedTokenBean implements Serializable {
    public static final long serialVersionUID = 12345678904354321L;

    private String linkedInAccessToken;
    private int requestMode;
    private boolean loggedIn;

    public LinkedInLoggedTokenBean(boolean loggedIn, int requestMode, String linkedInAccessToken){
        this.loggedIn = loggedIn;
        this.requestMode = requestMode;
        this.linkedInAccessToken = linkedInAccessToken;
    }

    public String getLinkedInAccessToken() {
        return linkedInAccessToken;
    }

    public void setLinkedInAccessToken(String linkedInAccessToken) {
        this.linkedInAccessToken = linkedInAccessToken;
    }

    public int getRequestMode() {
        return requestMode;
    }

    public void setRequestMode(int requestMode) {
        this.requestMode = requestMode;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
