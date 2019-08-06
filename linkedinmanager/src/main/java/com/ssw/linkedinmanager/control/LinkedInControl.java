package com.ssw.linkedinmanager.control;

import com.ssw.linkedinmanager.deamon.LinkedInAsyncTask;
import com.ssw.linkedinmanager.events.LinkedInAccessTokenResponse;
import com.ssw.linkedinmanager.events.LinkedInEmailAddressResponse;
import com.ssw.linkedinmanager.events.LinkedInProfileDataResponse;

import static com.ssw.linkedinmanager.common.CommonInfo.MODE_ACCESS_TOKEN_REQUEST;
import static com.ssw.linkedinmanager.common.CommonInfo.MODE_EMAIL_ADDRESS_REQUEST;
import static com.ssw.linkedinmanager.common.CommonInfo.MODE_PROFILE_DATA_REQUEST;

public class LinkedInControl {
    private static final String TAG = "LinkedInControl";

    public void getAccessToken(String url, LinkedInAccessTokenResponse linkedInAccessTokenResponse) {
        new LinkedInAsyncTask(url, MODE_ACCESS_TOKEN_REQUEST, linkedInAccessTokenResponse).execute();
    }

    public void getProfileData(String url, LinkedInProfileDataResponse linkedInProfileDataResponse) {
        new LinkedInAsyncTask(url, MODE_PROFILE_DATA_REQUEST, linkedInProfileDataResponse).execute();
    }

    public void getEmailAddress(String url, LinkedInEmailAddressResponse linkedInEmailAddressResponse) {
        new LinkedInAsyncTask(url, MODE_EMAIL_ADDRESS_REQUEST, linkedInEmailAddressResponse).execute();
    }
}
