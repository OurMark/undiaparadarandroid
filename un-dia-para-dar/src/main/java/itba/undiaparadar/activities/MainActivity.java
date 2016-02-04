package itba.undiaparadar.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.monits.skeletor.activity.SlideMenuStruct;

import itba.undiaparadar.R;
import itba.undiaparadar.fragments.SideMenuFragment;
import itba.undiaparadar.fragments.TopicsFragment;
import itba.undiaparadar.interfaces.TitleProvider;

public class MainActivity extends SlideMenuStruct {
	private CallbackManager callbackManager;
	private static final String NOTIFICATION_ID = "NOTIFICATION_ID";

	public static Intent getIntent(final Context context) {
		return new Intent(context, MainActivity.class);
	}

	public static Intent getIntent(final Context context, final int notificationId) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.putExtra(NOTIFICATION_ID, notificationId);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(this);
		callbackManager = CallbackManager.Factory.create();
		int notificationId = getIntent().getIntExtra(NOTIFICATION_ID, -1);
		if (notificationId != -1) {
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(notificationId);
		}
	}

	@NonNull
	@Override
	protected String getCurrentTitle() {
		final Fragment contentFragment = getContentFragment();
		if (contentFragment instanceof TitleProvider) {
			final TitleProvider titleProvider = (TitleProvider) contentFragment;
			return titleProvider.getTitle();
		}
		return getString(R.string.hashtag_un_dia_para_dar);
	}

	@NonNull
	@Override
	protected Fragment newDefaultContentFragment() {
		return TopicsFragment.newInstance();
	}

	@NonNull
	@Override
	protected Fragment newDefaultSidebarFragment() {
		return SideMenuFragment.newInstance();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void putExtraInIntent(@NonNull Intent intent) {
		/* Inherited that is not used */
	}

	@Override
	protected void postReplaceFragment(@NonNull Fragment f, @NonNull Intent intent) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
}
