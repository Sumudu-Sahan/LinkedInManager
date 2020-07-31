# LinkedInManager
Android Library to Authenticate with LinkedIn

## What is LinkedInManager
LinkedInManager is an android library which can be used to implement LinkedIn Sign in for any android application.

```
onGetCodeSuccess(String code) - will return the authorization code to obtain the access token

onGetCodeFailed() - Failed situation when trying to get the authorization code which is used to obtain the access token

onGetAccessTokenSuccess(LinkedInAccessToken linkedInAccessToken) - will return the LinkedIn Access token for services which is mentioned in the scope
  
onGetAccessTokenFailed() - Failed situation when trying to get the access token

onGetProfileDataSuccess(LinkedInUserProfile linkedInUserProfile) - will return the user's profile data including first name, last name, profile ID and user image
  
onGetProfileDataFailed - Failed situation when trying to get the profile data

onGetEmailAddressSuccess(LinkedInEmailAddress linkedInEmailAddress) - will return the user's email address

onGetEmailAddressFailed() - Failed situation when trying to get the profile data
```

## V1.01.00 Added Features
### logout function
### check if the user is logged in or not option
### check the user logged mode


## Implementation

### Step : 1 - Add the JitPack repository to your project root build.gradle file
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

### Step : 2- Add the dependency
```
dependencies {
        implementation 'com.github.Sumudu-Sahan:LinkedInManager:1.01.00'
}
```

### Step : 3 - Inherit your activity from linkedInManagerResponse
```
public class MainActivity extends AppCompatActivity implements LinkedInManagerResponse{}
```

### Step : 4 - Create a LinkedInRequestManager instance and initialize it.
```
LinkedInRequestManager linkedInRequestManager = new LinkedInRequestManager(Activity, LinkedInManagerResponse, "CLIENT ID", "CLIENT SECRET", "REDIRECTION URL", allowCancelDialogPrompt);
```

CLIENT ID, CLIENT SECRET and REDIRECTION URL is available at LinkedIn Developer Console. variable allowCancelDialogPrompt is a boolean

### Step : 5 - invoke the showAuthenticateView() with a mode to start the sign in process
```
linkedInRequestManager.showAuthenticateView(mode);
```

### Available modes
```
LinkedInRequestManager.MODE_EMAIL_ADDRESS_ONLY - will return only the email address
LinkedInRequestManager.MODE_LITE_PROFILE_ONLY - will return only the profile data
LinkedInRequestManager.MODE_BOTH_OPTIONS - will return both email address and profile data
```

## To get the user's First name, last name and profile ID, use the following overrided method in your activity which is inherited from LinkedInManagerResponse
```
@Override
    public void onGetProfileDataSuccess(LinkedInUserProfile linkedInUserProfile) {
            linkedInUserProfile.getImageURL(); // user's Image URL
            linkedInUserProfile.getUserName().getFirstName().getLocalized().getEn_US(); // User's first name
            linkedInUserProfile.getUserName().getLastName().getLocalized().getEn_US(); // User's last name
            linkedInUserProfile.getUserName().getId(); // User's profile ID
    }
    
```
## To get the user's email address, use the following overrided method in your activity which is inherited from LinkedInManagerResponse
```
@Override
    public void onGetEmailAddressSuccess(LinkedInEmailAddress linkedInEmailAddress) {
        linkedInEmailAddress.getEmailAddress()); // User's email address
    }
		
```
# OPTIONAL 
## If you need to get the User's Access token, use the following overrided method in your activity which is inherited from LinkedInManagerResponse
```
@Override
public void onGetAccessTokenSuccess(LinkedInAccessToken linkedInAccessToken) {
        linkedInAccessToken.getAccess_token(); //User's access token
}
```

## If you need to get the User's authorization code, user the following overrided method in your activity which is inherited from LinkedInManagerResponse
```
@Override
    public void onGetCodeSuccess(String code) {

    }
```

## If you need to check whether the user is already logged in or not, use the following code segment inside your activity.
```
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
```

## If you need to check whether the user is already logged in and logged mode, user the following code segment inside your activity.
```
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
```

## To close authentication dialog manually, use below code line.
```
linkedInRequestManager.dismissAuthenticateView();
```

## To logout the user manually, use below code line.
```
linkedInRequestManager.logout();
```

That's it. 
Happy Coding :smiley: :smiley: :smiley:
