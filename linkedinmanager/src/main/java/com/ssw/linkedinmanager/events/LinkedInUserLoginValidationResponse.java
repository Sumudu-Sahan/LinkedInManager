package com.ssw.linkedinmanager.events;

public interface LinkedInUserLoginValidationResponse {
    void activeLogin();
    void tokenExpired();
    void notLogged();
}
