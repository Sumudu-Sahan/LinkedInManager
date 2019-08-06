package com.ssw.linkedinmanager.events;

import org.json.JSONObject;

public interface LinkedInAccessTokenResponse {
    void onAuthenticationSuccess(JSONObject jsonObject);
    void onAuthenticationFailed();
}
