/*
 * Copyright 2010 - 2014 Monits
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.monits.skeletor.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.monits.skeletor.R;
import com.monits.skeletor.base.BaseSlideMenuStruct;
import com.monits.skeletor.interfaces.FragmentFactory;

/**
 * This class provides you the skeleton of the left slide menu. 
 * It allows you to customize the layout. 
 * It gives you the ease of creating the listener for each menu item. 
 * Only you have to indicate which is the new fragment for each item.
 * 
 * @author Martin Purita {@literal <mpurita@monits.com>}
 */
public abstract class SlideMenuStruct extends BaseSlideMenuStruct {
	public static final String EXTRA_FRAGMENT_FACTORY = "fragmentFactory";
	public static final String EXTRA_CLICKED_ID = "clickedId";

	private ActionBarDrawerToggle mDrawerToggle;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				getOpenDrawerString(), getCloseDrawerString());
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	/**
	 * The default string when the drawer opens if an empty string. 
	 * If you want to change the icon you must override this method
	 * 
	 * @return The string id that you want to appear when the
	 * drawer is opened
	 */
	@StringRes
	public int getOpenDrawerString() {
		return R.string.empty_string;
	}
	
	/**
	 * The default string when the drawer closes if an empty string. 
	 * If you want to change the icon you must override this method
	 * 
	 * @return The string id that you want to appear when the
	 * drawer is closed
	 */
	@StringRes
	public int getCloseDrawerString() {
		return R.string.empty_string;
	}
	
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			toggleSlideMenu();
		} else {
			closeSlideMenu();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(final Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(final Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Set OnItemClickListener for an adapter view. For each position change the
	 * content fragment with the new one.
	 * 
	 * @param rootView The adapter view where you want to set the listener
	 * @param array An array that contains a {@link FragmentFactory} for
	 * each id.
	 */
	public void setSidebarListener(@NonNull final AdapterView<?> rootView,
					@NonNull final SparseArray<FragmentFactory> array) {
		final AdapterView.OnItemClickListener onItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent,
							final View view, final int position, final long id) {
				launchIntentForFragment((int) id, array.get((int) id));
			}
		};
		rootView.setOnItemClickListener(onItemClickListener);
	}

	private void launchIntentForFragment(@IdRes final int id,
					@NonNull final FragmentFactory fragmentFactory) {
		final Intent intent = new Intent(getApplicationContext(), getActivityClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra(EXTRA_CLICKED_ID, id);
		intent.putExtra(EXTRA_FRAGMENT_FACTORY, fragmentFactory);
		putExtraInIntent(intent);
		startActivity(intent);
	}

	@NonNull
	protected Class<? extends Activity> getActivityClass() {
		return getClass();
	}

	/**
	 * Set OnClickListener for a view. For each id we set the onClickListener.
	 * 
	 * @param rootView The parent view where we have to find the view 
	 * that you want to have the listener
	 * @param array An array that contains a {@link FragmentFactory} for
	 * each id.	 
	 */
	public void setSidebarListener(@NonNull final View rootView,
					@NonNull final SparseArray<FragmentFactory> array) {
		final View.OnClickListener onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				launchIntentForFragment(v.getId(), array.get(v.getId()));
			}
		};
		for (int i = 0; i < array.size(); i++) {
			final View view = rootView.findViewById(array.keyAt(i));
			if (view != null) {
				view.setOnClickListener(onClickListener);
			}
		}
	}
	
	/**
	 * Add extras to the intent. The intent will already have a serialized 
	 * extra, called 'fragmentClass', that contains the class of the fragment
	 * that is going to replace the old one. The intent will already have
	 * an int extra, called 'clickedId', that contains the id of the clicked view
	 * 
	 * @param intent The new intent for the new fragment
	 */
	protected abstract void putExtraInIntent(@NonNull final Intent intent);


	@Override
	protected void onNewIntent(final Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);

		if (intent.hasExtra(EXTRA_FRAGMENT_FACTORY)) {
			final FragmentFactory fragmentFactory = (FragmentFactory) intent
							.getSerializableExtra(EXTRA_FRAGMENT_FACTORY);
			if (fragmentFactory != null) {
				final Fragment f = fragmentFactory.newFragment();
				setContentFragment(f, true);
				postReplaceFragment(f, intent);
			}
		}
	}

	/**
	 * Add functionality after the fragment was replaced. 
	 * 
	 * IMPORTANT!! This method is called before the fragment
	 * calls the onCreate method
	 * 
	 * @param f The new fragment that will appear
	 * @param intent The intent for the new fragment
	 */
	protected abstract void postReplaceFragment(@NonNull final Fragment f,
					@NonNull final Intent intent);
}
