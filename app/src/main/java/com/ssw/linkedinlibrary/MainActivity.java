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
import com.ssw.linkedinmanager.ui.LinkedInRequestManager;

public class MainActivity extends AppCompatActivity implements LinkedInManagerResponse {
    private static final String TAG = "MainActivity";

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
        //provide Redirection URL in this format <https://www.example.com>. This URL is also available in LinkedIn Developer Console
        linkedInRequestManager = new LinkedInRequestManager(activity, this, "YOUR CLIENT ID HERE", "YOUR CLIENT SECRET HERE", "YOUR REDIRECTION URL HERE");
        
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
                linkedInRequestManager.showAuthenticateView(LinkedInRequestManager.MODE_BOTH_OPTIONS);
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
