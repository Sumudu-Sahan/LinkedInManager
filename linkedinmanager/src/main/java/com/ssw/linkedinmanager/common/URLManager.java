package com.ssw.linkedinmanager.common;

public class URLManager {
    public static final String URL_FOR_AUTHORIZATION_CODE = "https://www.linkedin.com/oauth/v2/authorization";
    public static final String URL_FOR_ACCESS_TOKEN = "https://www.linkedin.com/oauth/v2/accessToken";
    public static final String URL_FOR_PROFILE_DATA = "https://api.linkedin.com/v2/me?projection=(id,firstName,lastName,profilePicture(displayImage~:playableStreams))&oauth2_access_token=";
    public static final String URL_FOR_EMAIL_ADDRESS = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))&oauth2_access_token=";
}
