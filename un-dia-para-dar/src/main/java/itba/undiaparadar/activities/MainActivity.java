package itba.undiaparadar.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.monits.skeletor.activity.SlideMenuStruct;

import itba.undiaparadar.R;
import itba.undiaparadar.fragments.ProfileFragment;
import itba.undiaparadar.fragments.SideMenuFragment;
import itba.undiaparadar.interfaces.TitleProvider;

public class MainActivity extends SlideMenuStruct {

	public static Intent getIntent(final Context context) {
		return new Intent(context, MainActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@NonNull
	@Override
	protected String getCurrentTitle() {
		final Fragment contentFragment = getContentFragment();
		if (contentFragment instanceof TitleProvider) {
			final TitleProvider titleProvider = (TitleProvider) contentFragment;
			return titleProvider.getTitle();
		}
		return getString(R.string.app_name);
	}

	@NonNull
	@Override
	protected Fragment newDefaultContentFragment() {
		return ProfileFragment.newInstance();
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
}
