package itba.undiaparadar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.inject.Inject;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.services.SettingsService;
import itba.undiaparadar.services.UserService;

public class LoginActivity extends Activity {
//	private CallbackManager callbackManager;
	private AccessTokenTracker accessTokenTracker;
	@Inject
	private SettingsService settingsService;
	@Inject
	private UserService userService;

	public LoginActivity() {
		UnDiaParaDarApplication.injectMembers(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(this);
		ParseFacebookUtils.initialize(this);
		setContentView(R.layout.activity_login);
		this.accessTokenTracker = new AccessTokenTracker() {
			@Override
			protected void onCurrentAccessTokenChanged(final AccessToken oldAccessToken,
				final AccessToken newAccessToken) {
				updateWithToken(newAccessToken);
			}
		};
//		callbackManager = CallbackManager.Factory.create();
		final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setReadPermissions("public_profile", "email");
		updateWithToken(AccessToken.getCurrentAccessToken());
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this,
						Arrays.asList("public_profile", "email") ,new LogInCallback() {
							@Override
							public void done (ParseUser user, ParseException err){
								if (user == null) {
									Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
								} else if (user.isNew()) {
									Log.d("MyApp", "User signed up and logged in through Facebook!");
									userService.getUserDetailsFromFB();
								} else {
									Log.d("MyApp", "User logged in through Facebook!");
									userService.getUserDetailsFromParse();
								}
							}
						}

				);
			}
		});
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode,
		final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		callbackManager.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
	}

	private void updateWithToken(AccessToken currentAccessToken) {

		if (currentAccessToken != null) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					LoginActivity.this.startActivity(MainActivity.getIntent(LoginActivity.this));
					finish();
				}
			}, 0);
		}
	}
}
