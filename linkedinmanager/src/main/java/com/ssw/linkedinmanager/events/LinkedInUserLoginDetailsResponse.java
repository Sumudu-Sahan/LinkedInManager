package com.ssw.linkedinmanager.events;

public interface LinkedInUserLoginDetailsResponse {
    void loggedMode(int mode);
    void tokenExpired();
    void notLogged();
}
