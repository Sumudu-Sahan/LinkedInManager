package com.ssw.linkedinmanager.deamon;

import android.os.AsyncTask;
import android.util.Log;

import com.ssw.linkedinmanager.events.LinkedInAccessTokenResponse;
import com.ssw.linkedinmanager.events.LinkedInEmailAddressResponse;
import com.ssw.linkedinmanager.events.LinkedInProfileDataResponse;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static com.ssw.linkedinmanager.common.CommonInfo.MODE_ACCESS_TOKEN_REQUEST;
import static com.ssw.linkedinmanager.common.CommonInfo.MODE_EMAIL_ADDRESS_REQUEST;
import static com.ssw.linkedinmanager.common.CommonInfo.MODE_PROFILE_DATA_REQUEST;

public class LinkedInAsyncTask extends AsyncTask<Void, Void, String> {
    private String callingURLString;
    private int mode;

    private LinkedInEmailAddressResponse linkedInEmailAddressResponse;
    private LinkedInAccessTokenResponse linkedInAccessTokenResponse;
    private LinkedInProfileDataResponse linkedInProfileDataResponse;

    public LinkedInAsyncTask(String callingURLString, int mode, LinkedInEmailAddressResponse linkedInEmailAddressResponse) {
        this.callingURLString = callingURLString;
        this.mode = mode;
        this.linkedInEmailAddressResponse = linkedInEmailAddressResponse;
    }

    public LinkedInAsyncTask(String callingURLString, int mode, LinkedInAccessTokenResponse linkedInAccessTokenResponse) {
        this.callingURLString = callingURLString;
        this.mode = mode;
        this.linkedInAccessTokenResponse = linkedInAccessTokenResponse;
    }

    public LinkedInAsyncTask(String callingURLString, int mode, LinkedInProfileDataResponse linkedInProfileDataResponse) {
        this.callingURLString = callingURLString;

        this.mode = mode;
        this.linkedInProfileDataResponse = linkedInProfileDataResponse;
    }

    @Override
    protected String doInBackground(Void... params) {
        URLConnection urlConn = null;
        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(callingURLString);
            urlConn = url.openConnection();

            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            return stringBuffer.toString();
        } catch (Exception ignored) {
            return "";
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ignored) {

                }
            }
        }
    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            switch (mode) {
                case MODE_EMAIL_ADDRESS_REQUEST:
                    try {
                        linkedInEmailAddressResponse.onSuccessResponse(new JSONObject(response));
                    } catch (Exception ignored) {
                        linkedInEmailAddressResponse.onFailedResponse();
                    }
                    break;

                case MODE_PROFILE_DATA_REQUEST:
                    try {
                        linkedInProfileDataResponse.onRequestSuccess(new JSONObject(response));
                    } catch (Exception ignored) {
                        linkedInProfileDataResponse.onRequestFailed();
                    }
                    break;

                case MODE_ACCESS_TOKEN_REQUEST:
                    try {
                        linkedInAccessTokenResponse.onAuthenticationSuccess(new JSONObject(response));
                    } catch (Exception ignored) {
                        linkedInAccessTokenResponse.onAuthenticationFailed();
                    }
                    break;
            }

        } else {
            switch (mode) {
                case MODE_EMAIL_ADDRESS_REQUEST:
                    linkedInEmailAddressResponse.onFailedResponse();
                    break;

                case MODE_PROFILE_DATA_REQUEST:
                    linkedInProfileDataResponse.onRequestFailed();
                    break;

                case MODE_ACCESS_TOKEN_REQUEST:
                    linkedInAccessTokenResponse.onAuthenticationFailed();
                    break;
            }
        }
    }
}
