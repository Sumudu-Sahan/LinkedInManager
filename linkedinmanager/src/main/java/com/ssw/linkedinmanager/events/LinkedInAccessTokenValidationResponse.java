package com.ssw.linkedinmanager.events;

public interface LinkedInAccessTokenValidationResponse {
    void onValidationSuccess();

    void onValidationFailed();
}
