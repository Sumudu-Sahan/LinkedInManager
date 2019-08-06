package com.ssw.linkedinmanager.events;

import com.ssw.linkedinmanager.dto.LinkedInAccessToken;
import com.ssw.linkedinmanager.dto.LinkedInEmailAddress;
import com.ssw.linkedinmanager.dto.LinkedInUserProfile;

public interface LinkedInManagerResponse {
    void onGetAccessTokenFailed();

    void onGetAccessTokenSuccess(LinkedInAccessToken linkedInAccessToken);

    void onGetCodeFailed();

    void onGetCodeSuccess(String code);

    void onGetProfileDataFailed();

    void onGetProfileDataSuccess(LinkedInUserProfile linkedInUserProfile);

    void onGetEmailAddressFailed();

    void onGetEmailAddressSuccess(LinkedInEmailAddress linkedInEmailAddress);
}
