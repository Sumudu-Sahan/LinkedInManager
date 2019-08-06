package com.ssw.linkedinmanager.dto;

import java.io.Serializable;

public class LinkedInEmailAddress implements Serializable {
    public static final long serialVersionUID = 12345678904354321L;

    private String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
