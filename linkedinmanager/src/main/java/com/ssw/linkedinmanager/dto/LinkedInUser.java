package com.ssw.linkedinmanager.dto;

import java.io.Serializable;

public class LinkedInUser implements Serializable {
    public static final long serialVersionUID = 12345678904354321L;

    private LinkedInUserProfile userProfile;
    private LinkedInEmailAddress linkedInEmailAddress;

    public LinkedInUserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(LinkedInUserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public LinkedInEmailAddress getLinkedInEmailAddress() {
        return linkedInEmailAddress;
    }

    public void setLinkedInEmailAddress(LinkedInEmailAddress linkedInEmailAddress) {
        this.linkedInEmailAddress = linkedInEmailAddress;
    }
}
