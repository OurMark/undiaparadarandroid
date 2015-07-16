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

package com.monits.skeletor.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;

import com.monits.skeletor.R;
import com.monits.skeletor.activity.SlideMenuStruct;
import com.monits.skeletor.interfaces.FragmentFactory;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This class is responsible to perform the necessary
 * operations for replace the fragments, create the
 * {@link DrawerLayout}.
 * This class must no be extended. Use the subclass
 * {@link com.monits.skeletor.activity.SlideMenuStruct}
 *
 * @author Martin Purita {@literal <mpurita@monits.com>}
 */
@SuppressFBWarnings(value = "ACEM_ABSTRACT_CLASS_EMPTY_METHODS",
	justification = "We want to provide a default implementation on not commonly used methods")
public abstract class BaseSlideMenuStruct extends BaseStruct {

	protected DrawerLayout mDrawerLayout;
	private View mSlideFrame;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.base_activity);

		mSlideFrame = findViewById(R.id.slide_frame);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		if (savedInstanceState == null) {
			// Creating a new activity, not going back to an old one
			final Fragment mainContent;
			final FragmentFactory fragmentFactory = (FragmentFactory) getIntent()
					.getSerializableExtra(SlideMenuStruct.EXTRA_FRAGMENT_FACTORY);
			if (fragmentFactory == null) {
				mainContent = newDefaultContentFragment();
			} else {
				mainContent = fragmentFactory.newFragment();
			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.slide_frame, newDefaultSidebarFragment())
					.replace(R.id.main_content, mainContent)
					.commit();
		}
	}

	/**
	 * Replace the sidebar fragment
	 *
	 * @param sidebarFragment The new sidebar fragment
	 */
	public void setSidebarFragment(@NonNull final Fragment sidebarFragment) {
		addFragmentWithNoBackstack(sidebarFragment, R.id.slide_frame);
	}

	/**
	 * Replace the content fragment
	 *
	 * @param fragment The new content fragment
	 * @param addToBackstack True if you want to add to
	 * back stack, False otherwise
	 */
	public void setContentFragment(@NonNull final Fragment fragment, final boolean addToBackstack) {
		replaceFragment(fragment, null, false,
						FragmentTransaction.TRANSIT_FRAGMENT_FADE,
						R.id.main_content, addToBackstack);
		closeSlideMenu();
	}

	/**
	 * Get the content fragment
	 *
	 * @return The content fragment
	 */
	@NonNull
	protected Fragment getContentFragment() {
		return getSupportFragmentManager().findFragmentById(R.id.main_content);
	}

	/**
	 * Get the sidebar fragment
	 *
	 * @return The sidebar fragment
	 */
	@NonNull
	protected Fragment getSidebarFragment() {
		return getSupportFragmentManager().findFragmentById(R.id.slide_frame);
	}

	public void closeSlideMenu() {
		if (mDrawerLayout.isDrawerOpen(mSlideFrame)) {
			mDrawerLayout.closeDrawer(mSlideFrame);
		}
	}

	/**
	 * Change the default width of the sidebar. The default value is 250dp
	 *
	 * @param width
	 *            The number of the new width
	 * @param px
	 *            Indicates whether the value is in pixel or density-independent
	 *            pixel(dp)
	 */
	public void setSidebarWidth(final int width, final boolean px) {
		final LayoutParams params = mSlideFrame.getLayoutParams();
		final int newWidth;

		if (px) {
			newWidth = width;
		} else {
			newWidth = dpToPx(width);
		}

		params.width = newWidth;
	}

	/**
	 * Change the default height of the sidebar. The default value is
	 * {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT}
	 *
	 * @param height
	 *            The number of the new height
	 * @param px
	 *            Indicates whether the value is in pixel or density-independent
	 *            pixel(dp)
	 */
	public void setSidebarHeight(final int height, final boolean px) {
		final LayoutParams params = mSlideFrame.getLayoutParams();
		final int newHeight;

		if (px) {
			newHeight = height;
		} else {
			newHeight = dpToPx(height);
		}

		params.height = newHeight;
	}

	/**
	 * Change the default width and height of the sidebar
	 *
	 * @param width
	 *            The number of the new width
	 * @param height
	 *            The number of the new height
	 * @param px
	 *            Indicates whether the values are in pixel or
	 *            density-independent pixel(dp)
	 */
	public void setSidebarWidthAndHeight(final int width, final int height, final boolean px) {
		setSidebarHeight(height, px);
		setSidebarWidth(width, px);
	}

	protected void toggleSlideMenu() {
		if (mDrawerLayout.isDrawerOpen(mSlideFrame)) {
			mDrawerLayout.closeDrawer(mSlideFrame);
		} else {
			mDrawerLayout.openDrawer(mSlideFrame);
		}
		hideKeyboard();
	}

	private void hideKeyboard() {
		if (getCurrentFocus() != null) {
			final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	private int dpToPx(final int dp) {
		final DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics));
	}

	/**
	 * You must define which will be the default content fragment.
	 *
	 * @return The default fragment
	 */
	@NonNull
	protected abstract Fragment newDefaultContentFragment();

	/**
	 * You must define which will be the default sidebar fragment.
	 *
	 * @return The default fragment
	 */
	@NonNull
	protected abstract Fragment newDefaultSidebarFragment();


	@Override
	protected int getMainFragmentPlaceholder() {
		return R.id.main_content;
	}

	@SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
	@Override
	public void fragmentExistBackPressed(final Fragment fragment) {
		// Default empty implementation
	}
}