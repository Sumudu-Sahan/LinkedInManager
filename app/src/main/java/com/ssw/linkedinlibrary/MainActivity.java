package com.ssw.linkedinlibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.ssw.linkedinmanager.dto.LinkedInAccessToken;
import com.ssw.linkedinmanager.dto.LinkedInEmailAddress;
import com.ssw.linkedinmanager.dto.LinkedInUserProfile;
import com.ssw.linkedinmanager.events.LinkedInManagerResponse;
import com.ssw.linkedinmanager.events.LinkedInUserLoginDetailsResponse;
import com.ssw.linkedinmanager.events.LinkedInUserLoginValidationResponse;
import com.ssw.linkedinmanager.ui.LinkedInRequestManager;

public class MainActivity extends AppCompatActivity implements LinkedInManagerResponse {

    private Context context;
    private Activity activity;

    private AppCompatImageView ivImage;
    private TextView tvFirstName, tvLastName, tvEmailAddress;
    private Button btnSignInWithLinkedIn;

    private LinkedInRequestManager linkedInRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initObjects();
    }

    private void initObjects() {
        context = MainActivity.this;
        activity = MainActivity.this;

        //Client ID and Client Secret is in the LinkedIn Developer Console
        //provide Redirection URL which is available in developer console. This URL is available in LinkedIn Developer Console
        linkedInRequestManager = new LinkedInRequestManager(activity, this, "YOUR CLIENT ID", "YOUR CLIENT SECRET", "REDIRECTION URL", true);

        initComponents();
    }

    private void initComponents() {
        ivImage = findViewById(R.id.ivImage);

        tvFirstName = findViewById(R.id.tvFirstName);
        tvLastName = findViewById(R.id.tvLastName);
        tvEmailAddress = findViewById(R.id.tvEmailAddress);


        btnSignInWithLinkedIn = findViewById(R.id.btnSignInWithLinkedIn);
        btnSignInWithLinkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLinkedInSignIn();
            }
        });
    }

    private void startLinkedInSignIn() {
        linkedInRequestManager.showAuthenticateView(LinkedInRequestManager.MODE_BOTH_OPTIONS);

        //LinkedInRequestManager.MODE_BOTH_OPTIONS - can get email and user profile data with user profile image
        //LinkedInRequestManager.MODE_EMAIL_ADDRESS_ONLY - can get only the user profile email address
        //LinkedInRequestManager.MODE_LITE_PROFILE_ONLY - can get the user profile details with profile image
    }

    private void closeAuthenticationView() {
        linkedInRequestManager.dismissAuthenticateView();
    }

    private void isUserLoggedIn() {
        linkedInRequestManager.isLoggedIn(new LinkedInUserLoginValidationResponse() {
            @Override
            public void activeLogin() {
                //Session token is active. can use to get data from linkedin
            }

            @Override
            public void tokenExpired() {
                //token has been expired. need to obtain a new code
            }

            @Override
            public void notLogged() {
                //user is not logged into the application
            }
        });
    }

    private void logout() {
        //logout the user
        linkedInRequestManager.logout();
    }

    private void checkUserLoggedPermissions() {
        linkedInRequestManager.getLoggedRequestedMode(new LinkedInUserLoginDetailsResponse() {
            @Override
            public void loggedMode(int mode) {
                //user is already logged in. active token. mode is available
                switch (mode) {
                    case LinkedInRequestManager.MODE_LITE_PROFILE_ONLY:
                        break;

                    case LinkedInRequestManager.MODE_EMAIL_ADDRESS_ONLY:
                        break;

                    case LinkedInRequestManager.MODE_BOTH_OPTIONS:
                        break;
                }
            }

            @Override
            public void tokenExpired() {
                //token has been expired. need to obtain a new code
            }

            @Override
            public void notLogged() {
                //user is not logged into the application
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onGetAccessTokenFailed() {

    }

    @Override
    public void onGetAccessTokenSuccess(LinkedInAccessToken linkedInAccessToken) {

    }

    @Override
    public void onGetCodeFailed() {

    }

    @Override
    public void onGetCodeSuccess(String code) {

    }

    @Override
    public void onGetProfileDataFailed() {

    }

    @Override
    public void onGetProfileDataSuccess(LinkedInUserProfile linkedInUserProfile) {
        tvFirstName.setText("First Name : " + linkedInUserProfile.getUserName().getFirstName().getLocalized().getEn_US());
        tvLastName.setText("Last Name : " + linkedInUserProfile.getUserName().getLastName().getLocalized().getEn_US());

        Glide
                .with(context)
                .load(linkedInUserProfile.getImageURL())
                .into(ivImage);

        linkedInRequestManager.dismissAuthenticateView();
    }

    @Override
    public void onGetEmailAddressFailed() {

    }

    @Override
    public void onGetEmailAddressSuccess(LinkedInEmailAddress linkedInEmailAddress) {
        tvEmailAddress.setText("Email Address : " + linkedInEmailAddress.getEmailAddress());

        linkedInRequestManager.dismissAuthenticateView();
    }
}
