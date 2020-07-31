package com.ssw.linkedinmanager.ui;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ssw.linkedinmanager.R;
import com.ssw.linkedinmanager.common.CommonInfo;
import com.ssw.linkedinmanager.common.ExceptionManager;
import com.ssw.linkedinmanager.common.ObjectSerializer;
import com.ssw.linkedinmanager.common.PreferenceManager;
import com.ssw.linkedinmanager.control.LinkedInControl;
import com.ssw.linkedinmanager.dto.LinkedInAccessToken;
import com.ssw.linkedinmanager.dto.LinkedInEmailAddress;
import com.ssw.linkedinmanager.dto.LinkedInLoggedTokenBean;
import com.ssw.linkedinmanager.dto.LinkedInNameBean;
import com.ssw.linkedinmanager.dto.LinkedInUser;
import com.ssw.linkedinmanager.dto.LinkedInUserProfile;
import com.ssw.linkedinmanager.events.LinkedInAccessTokenResponse;
import com.ssw.linkedinmanager.events.LinkedInAccessTokenValidationResponse;
import com.ssw.linkedinmanager.events.LinkedInEmailAddressResponse;
import com.ssw.linkedinmanager.events.LinkedInManagerResponse;
import com.ssw.linkedinmanager.events.LinkedInProfileDataResponse;
import com.ssw.linkedinmanager.events.LinkedInUserLoginDetailsResponse;
import com.ssw.linkedinmanager.events.LinkedInUserLoginValidationResponse;

import org.json.JSONObject;

import static com.ssw.linkedinmanager.common.URLManager.URL_FOR_ACCESS_TOKEN;
import static com.ssw.linkedinmanager.common.URLManager.URL_FOR_ACCESS_TOKEN_VALIDATION;
import static com.ssw.linkedinmanager.common.URLManager.URL_FOR_AUTHORIZATION_CODE;
import static com.ssw.linkedinmanager.common.URLManager.URL_FOR_EMAIL_ADDRESS;
import static com.ssw.linkedinmanager.common.URLManager.URL_FOR_PROFILE_DATA;

public class LinkedInRequestManager {
    private Dialog dialog;

    private Activity activity;
    private LinkedInManagerResponse linkedInManagerResponse;

    private String clientID;
    private String clientSecret;
    private String redirectionURL;

    public static final int MODE_EMAIL_ADDRESS_ONLY = 0;
    public static final int MODE_LITE_PROFILE_ONLY = 1;
    public static final int MODE_BOTH_OPTIONS = 2;

    private static final String SCOPE_LITE_PROFILE = "r_liteprofile";
    private static final String SCOPE_EMAIL_ADDRESS = "r_emailaddress";

    private final LinkedInUserProfile linkedInUserProfile = new LinkedInUserProfile();
    private final LinkedInEmailAddress linkedInEmailAddress = new LinkedInEmailAddress();
    private final LinkedInUser linkedInUser = new LinkedInUser();

    private int mode;

    private boolean allowCancelDialogPrompt;

    public LinkedInRequestManager(Activity activity, LinkedInManagerResponse linkedInManagerResponse, String clientID, String clientSecret, String redirectionURL, boolean allowCancelDialogPrompt) {
        this.activity = activity;
        this.linkedInManagerResponse = linkedInManagerResponse;

        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.redirectionURL = redirectionURL;
        this.allowCancelDialogPrompt = allowCancelDialogPrompt;
    }

    public void dismissAuthenticateView() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
            ExceptionManager.exceptionLog(e);
        }
    }

    public void showAuthenticateView(int mode) {
        this.mode = mode;
        try {
            LinkedInLoggedTokenBean linkedInLoggedTokenBean = (LinkedInLoggedTokenBean) ObjectSerializer.deserialize(PreferenceManager.getInstance(activity.getApplicationContext()).getString(CommonInfo.STRING_COMMON_PREFERENCE, CommonInfo.STRING_ACCESS_TOKEN, ""));
            if (linkedInLoggedTokenBean.isLoggedIn() && linkedInLoggedTokenBean.getRequestMode() == mode) {
                isValidToken(linkedInLoggedTokenBean.getLinkedInAccessToken(), mode);
            } else {
                if (PreferenceManager.getInstance(activity.getApplicationContext()).removePreferenceKey(CommonInfo.STRING_COMMON_PREFERENCE, CommonInfo.STRING_ACCESS_TOKEN)) {
                    startAuthenticate(mode);
                }
            }
        } catch (Exception e) {
            ExceptionManager.exceptionLog(e);
            if (PreferenceManager.getInstance(activity.getApplicationContext()).removePreferenceKey(CommonInfo.STRING_COMMON_PREFERENCE, CommonInfo.STRING_ACCESS_TOKEN)) {
                startAuthenticate(mode);
            }
        }
    }

    public boolean logout() {
        return PreferenceManager.getInstance(activity.getApplicationContext()).removePreferenceKey(CommonInfo.STRING_COMMON_PREFERENCE, CommonInfo.STRING_ACCESS_TOKEN);
    }

    public void isLoggedIn(final LinkedInUserLoginValidationResponse linkedInUserLoginValidationResponse) {
        try {
            LinkedInLoggedTokenBean linkedInLoggedTokenBean = (LinkedInLoggedTokenBean) ObjectSerializer.deserialize(PreferenceManager.getInstance(activity.getApplicationContext()).getString(CommonInfo.STRING_COMMON_PREFERENCE, CommonInfo.STRING_ACCESS_TOKEN, ""));
            if (linkedInLoggedTokenBean.isLoggedIn() && linkedInLoggedTokenBean.getRequestMode() == mode) {
                new LinkedInControl().checkAccessTokenValidity(URL_FOR_ACCESS_TOKEN_VALIDATION, "client_id=" + clientID + "&client_secret=" + clientSecret + "&token=" + linkedInLoggedTokenBean.getLinkedInAccessToken(), new LinkedInAccessTokenValidationResponse() {
                    @Override
                    public void onValidationSuccess() {
                        linkedInUserLoginValidationResponse.activeLogin();
                    }

                    @Override
                    public void onValidationFailed() {
                        linkedInUserLoginValidationResponse.tokenExpired();
                    }
                });
            } else {
                logout();
                linkedInUserLoginValidationResponse.notLogged();
            }
        } catch (Exception e) {
            ExceptionManager.exceptionLog(e);
            logout();
            linkedInUserLoginValidationResponse.notLogged();
        }
    }

    public void getLoggedRequestedMode(final LinkedInUserLoginDetailsResponse linkedInUserLoginDetailsResponse) {
        try {
            final LinkedInLoggedTokenBean linkedInLoggedTokenBean = (LinkedInLoggedTokenBean) ObjectSerializer.deserialize(PreferenceManager.getInstance(activity.getApplicationContext()).getString(CommonInfo.STRING_COMMON_PREFERENCE, CommonInfo.STRING_ACCESS_TOKEN, ""));
            if (linkedInLoggedTokenBean.isLoggedIn()) {
                new LinkedInControl().checkAccessTokenValidity(URL_FOR_ACCESS_TOKEN_VALIDATION, "client_id=" + clientID + "&client_secret=" + clientSecret + "&token=" + linkedInLoggedTokenBean.getLinkedInAccessToken(), new LinkedInAccessTokenValidationResponse() {
                    @Override
                    public void onValidationSuccess() {
                        linkedInUserLoginDetailsResponse.loggedMode(linkedInLoggedTokenBean.getRequestMode());
                    }

                    @Override
                    public void onValidationFailed() {
                        linkedInUserLoginDetailsResponse.tokenExpired();
                    }
                });
            } else {
                logout();
                linkedInUserLoginDetailsResponse.notLogged();
            }
        } catch (Exception e) {
            ExceptionManager.exceptionLog(e);
            logout();
            linkedInUserLoginDetailsResponse.notLogged();
        }
    }

    private void isValidToken(final String accessToken, final int mode) {
        new LinkedInControl().checkAccessTokenValidity(URL_FOR_ACCESS_TOKEN_VALIDATION, "client_id=" + clientID + "&client_secret=" + clientSecret + "&token=" + accessToken, new LinkedInAccessTokenValidationResponse() {
            @Override
            public void onValidationSuccess() {
                LinkedInAccessToken linkedInAccessToken = new LinkedInAccessToken();
                linkedInAccessToken.setAccess_token(accessToken);
                getRelevantData(linkedInAccessToken);
            }

            @Override
            public void onValidationFailed() {
                startAuthenticate(mode);
            }
        });
    }

    private void startAuthenticate(int mode) {
        String url = URL_FOR_AUTHORIZATION_CODE + "?response_type=code&client_id=" + clientID + "&redirect_uri=" + redirectionURL + "&state=aRandomString&scope=";
        switch (mode) {
            case MODE_LITE_PROFILE_ONLY:
                url += SCOPE_LITE_PROFILE;
                break;

            case MODE_BOTH_OPTIONS:
                url += SCOPE_LITE_PROFILE + "," + SCOPE_EMAIL_ADDRESS;
                break;

            case MODE_EMAIL_ADDRESS_ONLY:
            default:
                url += SCOPE_EMAIL_ADDRESS;
                break;
        }

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(allowCancelDialogPrompt);
        dialog.setContentView(R.layout.linkedin_popup_layout);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final WebView linkedInWebView = dialog.findViewById(R.id.linkedInWebView);
        WebSettings settings = linkedInWebView.getSettings();
        try {
            settings.setAllowFileAccess(false);
        } catch (Exception e) {
            ExceptionManager.exceptionLog(e);
        }
        linkedInWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        try {
            linkedInWebView.getSettings().setAllowFileAccess(false);
        } catch (Exception e) {
            ExceptionManager.exceptionLog(e);
        }

        linkedInWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(redirectionURL + "/?code")) {
                    String newString = url.replace((redirectionURL + "/?code="), "").trim();
                    String code = newString.substring(0, newString.indexOf("&state=aRandomString"));
                    getAccessToken(code);
                    linkedInManagerResponse.onGetCodeSuccess(code);
                    return true;
                } else {
                    linkedInManagerResponse.onGetCodeFailed();
                    return false;
                }
            }
        });
        linkedInWebView.loadUrl(url);
        dialog.show();
    }

    private void getAccessToken(String code) {
        String accessTokenURL = URL_FOR_ACCESS_TOKEN + "?"
                + "client_id=" + clientID
                + "&client_secret=" + clientSecret
                + "&grant_type=authorization_code"
                + "&redirect_uri=" + redirectionURL
                + "&code=" + code;

        new LinkedInControl().getAccessToken(accessTokenURL, new LinkedInAccessTokenResponse() {
            @Override
            public void onAuthenticationSuccess(JSONObject jsonObject) {
                Gson gson = new Gson();
                LinkedInAccessToken linkedInAccessToken = gson.fromJson(jsonObject.toString(), LinkedInAccessToken.class);
                linkedInManagerResponse.onGetAccessTokenSuccess(linkedInAccessToken);
                getRelevantData(linkedInAccessToken);
            }

            @Override
            public void onAuthenticationFailed() {
                linkedInManagerResponse.onGetAccessTokenFailed();
            }
        });
    }

    private LinkedInUserProfile getLinkedInProfileData(final LinkedInAccessToken linkedInAccessToken) {
        new LinkedInControl().getProfileData(URL_FOR_PROFILE_DATA + linkedInAccessToken.getAccess_token(), new LinkedInProfileDataResponse() {
            @Override
            public void onRequestSuccess(JSONObject jsonObject) {
                try {
                    Gson gson = new Gson();
                    LinkedInNameBean linkedInNameBean = gson.fromJson(jsonObject.toString(), LinkedInNameBean.class);
                    linkedInUserProfile.setUserName(linkedInNameBean);
                    try {
                        String imageURL = jsonObject
                                .getJSONObject("profilePicture")
                                .getJSONObject("displayImage~")
                                .getJSONArray("elements")
                                .getJSONObject(3)
                                .getJSONArray("identifiers")
                                .getJSONObject(0)
                                .getString("identifier");
                        linkedInUserProfile.setImageURL(imageURL);
                    } catch (Exception e) {
                        ExceptionManager.exceptionLog(e);
                        linkedInUserProfile.setImageURL("");
                    }

                    try {
                        PreferenceManager.getInstance(activity.getApplicationContext()).putString(CommonInfo.STRING_COMMON_PREFERENCE, CommonInfo.STRING_ACCESS_TOKEN, ObjectSerializer.serialize(new LinkedInLoggedTokenBean(true, mode, linkedInAccessToken.getAccess_token())));
                    } catch (Exception e) {
                        ExceptionManager.exceptionLog(e);
                    }
                    linkedInManagerResponse.onGetProfileDataSuccess(linkedInUserProfile);
                } catch (Exception e) {
                    ExceptionManager.exceptionLog(e);
                    linkedInManagerResponse.onGetProfileDataFailed();
                }
            }

            @Override
            public void onRequestFailed() {
                linkedInManagerResponse.onGetProfileDataFailed();
            }
        });
        return linkedInUserProfile;
    }

    private LinkedInEmailAddress getLinkedInEmailAddress(final LinkedInAccessToken linkedInAccessToken) {
        new LinkedInControl().getEmailAddress(URL_FOR_EMAIL_ADDRESS + linkedInAccessToken.getAccess_token(), new LinkedInEmailAddressResponse() {
            @Override
            public void onSuccessResponse(JSONObject jsonObject) {
                try {
                    String emailAddress = jsonObject
                            .getJSONArray("elements")
                            .getJSONObject(0)
                            .getJSONObject("handle~")
                            .getString("emailAddress");

                    try {
                        PreferenceManager.getInstance(activity.getApplicationContext()).putString(CommonInfo.STRING_COMMON_PREFERENCE, CommonInfo.STRING_ACCESS_TOKEN, ObjectSerializer.serialize(new LinkedInLoggedTokenBean(true, mode, linkedInAccessToken.getAccess_token())));
                    } catch (Exception e) {
                        ExceptionManager.exceptionLog(e);
                    }
                    linkedInEmailAddress.setEmailAddress(emailAddress);
                    linkedInManagerResponse.onGetEmailAddressSuccess(linkedInEmailAddress);
                } catch (Exception e) {
                    ExceptionManager.exceptionLog(e);
                    linkedInEmailAddress.setEmailAddress("");
                    linkedInManagerResponse.onGetEmailAddressFailed();
                }
            }

            @Override
            public void onFailedResponse() {
                linkedInManagerResponse.onGetEmailAddressFailed();
            }
        });
        return linkedInEmailAddress;
    }

    private LinkedInUser getRelevantData(LinkedInAccessToken linkedInAccessToken) {
        switch (mode) {
            case MODE_LITE_PROFILE_ONLY:
                linkedInUser.setUserProfile(getLinkedInProfileData(linkedInAccessToken));
                break;

            case MODE_BOTH_OPTIONS:
                linkedInUser.setLinkedInEmailAddress(getLinkedInEmailAddress(linkedInAccessToken));
                linkedInUser.setUserProfile(getLinkedInProfileData(linkedInAccessToken));
                break;

            case MODE_EMAIL_ADDRESS_ONLY:
            default:
                linkedInUser.setLinkedInEmailAddress(getLinkedInEmailAddress(linkedInAccessToken));
                break;
        }

        return linkedInUser;
    }
}
