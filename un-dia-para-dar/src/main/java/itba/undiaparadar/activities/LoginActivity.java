package itba.undiaparadar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import itba.undiaparadar.R;

public class LoginActivity extends Activity {
	private CallbackManager callbackManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(this);
		setContentView(R.layout.activity_login);
		callbackManager = CallbackManager.Factory.create();
		final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setReadPermissions("public_profile");
		loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(final LoginResult loginResult) {
				if (loginResult.getAccessToken() != null) {
					LoginActivity.this.startActivity(MainActivity.getIntent(LoginActivity.this));
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
}
