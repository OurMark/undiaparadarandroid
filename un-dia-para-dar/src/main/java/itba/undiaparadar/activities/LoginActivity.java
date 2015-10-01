package itba.undiaparadar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.inject.Inject;

import org.json.JSONObject;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.services.SettingsService;

public class LoginActivity extends Activity {
	private CallbackManager callbackManager;
	private AccessTokenTracker accessTokenTracker;
	@Inject
	private SettingsService settingsService;

	public LoginActivity() {
		UnDiaParaDarApplication.injectMembers(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(this);
		setContentView(R.layout.activity_login);
		this.accessTokenTracker = new AccessTokenTracker() {
			@Override
			protected void onCurrentAccessTokenChanged(final AccessToken oldAccessToken,
				final AccessToken newAccessToken) {
				updateWithToken(newAccessToken);
			}
		};
		callbackManager = CallbackManager.Factory.create();
		final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setReadPermissions("public_profile", "email");
		updateWithToken(AccessToken.getCurrentAccessToken());
		loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(final LoginResult loginResult) {
				if (loginResult.getAccessToken() != null) {
					GraphRequest.newMeRequest(
							loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
								@Override
								public void onCompleted(final JSONObject me, final GraphResponse response) {
									if (response.getError() != null) {
										// handle error
									} else {
										settingsService.saveEmail(me.optString("email"));
										LoginActivity.this.startActivity(MainActivity.getIntent(LoginActivity.this));
										finish();
									}
								}
							}).executeAsync();
				}
			}

			@Override
			public void onCancel() {
				/* Nothing to do here */
				Log.d("error", "facebook error");
			}

			@Override
			public void onError(final FacebookException exception) {
				// TODO: Handle error
				Log.d("error", "facebook error");
			}
		});
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode,
	                                final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
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
