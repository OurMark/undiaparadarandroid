package itba.undiaparadar.activities;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.FacebookSdk;

import itba.undiaparadar.R;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(this);
		setContentView(R.layout.activity_login);

	}
}
