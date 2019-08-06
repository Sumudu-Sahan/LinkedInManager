package com.ssw.linkedinmanager.dto;

import java.io.Serializable;

public class LinkedInUserProfile implements Serializable {
    public static final long serialVersionUID = 12345678904354321L;

    private LinkedInNameBean userName;
    private String imageURL;

    public LinkedInNameBean getUserName() {
        return userName;
    }

    public void setUserName(LinkedInNameBean userName) {
        this.userName = userName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
