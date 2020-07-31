package com.ssw.linkedinmanager.deamon;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.ssw.linkedinmanager.common.ExceptionManager;
import com.ssw.linkedinmanager.dto.LinkedInTokenValidationSuccessBean;
import com.ssw.linkedinmanager.events.LinkedInAccessTokenResponse;
import com.ssw.linkedinmanager.events.LinkedInAccessTokenValidationResponse;
import com.ssw.linkedinmanager.events.LinkedInEmailAddressResponse;
import com.ssw.linkedinmanager.events.LinkedInProfileDataResponse;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import static com.ssw.linkedinmanager.common.CommonInfo.MODE_ACCESS_TOKEN_REQUEST;
import static com.ssw.linkedinmanager.common.CommonInfo.MODE_ACCESS_TOKEN_VALIDATION;
import static com.ssw.linkedinmanager.common.CommonInfo.MODE_EMAIL_ADDRESS_REQUEST;
import static com.ssw.linkedinmanager.common.CommonInfo.MODE_PROFILE_DATA_REQUEST;

public class LinkedInAsyncTask extends AsyncTask<Void, Void, String> {
    private String callingURLString;
    private String urlParameters;
    private int mode;

    private LinkedInEmailAddressResponse linkedInEmailAddressResponse;
    private LinkedInAccessTokenResponse linkedInAccessTokenResponse;
    private LinkedInProfileDataResponse linkedInProfileDataResponse;
    private LinkedInAccessTokenValidationResponse linkedInAccessTokenValidationResponse;

    private Gson gson = new Gson();

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

    public LinkedInAsyncTask(String callingURLString, String urlParameters, int mode, LinkedInAccessTokenValidationResponse linkedInAccessTokenValidationResponse) {
        this.callingURLString = callingURLString;
        this.mode = mode;
        this.linkedInAccessTokenValidationResponse = linkedInAccessTokenValidationResponse;
        this.urlParameters = urlParameters;
    }

    @Override
    protected String doInBackground(Void... params) {
        BufferedReader bufferedReader = null;
        if (mode == MODE_ACCESS_TOKEN_VALIDATION) {
            try {
                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                int postDataLength = postData.length;
                URL url = new URL(callingURLString);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("charset", "utf-8");
                conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                conn.setUseCaches(false);
                try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                    wr.write(postData);
                }

                bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder stringBuffer = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                return stringBuffer.toString();
            } catch (Exception e) {
                ExceptionManager.exceptionLog(e);
                return "";
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        ExceptionManager.exceptionLog(e);
                    }
                }
            }
        } else {
            URLConnection urlConn;
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
            } catch (Exception e) {
                ExceptionManager.exceptionLog(e);
                return "";
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        ExceptionManager.exceptionLog(e);
                    }
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
                    } catch (Exception e) {
                        ExceptionManager.exceptionLog(e);
                        linkedInEmailAddressResponse.onFailedResponse();
                    }
                    break;

                case MODE_PROFILE_DATA_REQUEST:
                    try {
                        linkedInProfileDataResponse.onRequestSuccess(new JSONObject(response));
                    } catch (Exception e) {
                        ExceptionManager.exceptionLog(e);
                        linkedInProfileDataResponse.onRequestFailed();
                    }
                    break;

                case MODE_ACCESS_TOKEN_REQUEST:
                    try {
                        linkedInAccessTokenResponse.onAuthenticationSuccess(new JSONObject(response));
                    } catch (Exception e) {
                        ExceptionManager.exceptionLog(e);
                        linkedInAccessTokenResponse.onAuthenticationFailed();
                    }
                    break;

                case MODE_ACCESS_TOKEN_VALIDATION:
                    try {
                        LinkedInTokenValidationSuccessBean linkedInTokenValidationSuccessBean = gson.fromJson(response, LinkedInTokenValidationSuccessBean.class);
                        if (linkedInTokenValidationSuccessBean.isActive() && linkedInTokenValidationSuccessBean.getStatus().equals("active")) {
                            linkedInAccessTokenValidationResponse.onValidationSuccess();
                        } else {
                            linkedInAccessTokenValidationResponse.onValidationFailed();
                        }
                    } catch (Exception e) {
                        ExceptionManager.exceptionLog(e);
                        linkedInAccessTokenValidationResponse.onValidationFailed();
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
                case MODE_ACCESS_TOKEN_VALIDATION:
                    linkedInAccessTokenValidationResponse.onValidationFailed();
                    break;
            }
        }
    }
}
