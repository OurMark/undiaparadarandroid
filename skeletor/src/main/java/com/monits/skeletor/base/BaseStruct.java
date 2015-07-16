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

/**
 * This class provides you the replacement fragment method.
 * You only must extend from this, if you only want to use
 * fragment replace method.
 */
package com.monits.skeletor.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This class is the responsible for replace a fragment
 *
 * @author Martin Purita {@literal <mpurita@monits.com>}
 *
 */
@SuppressFBWarnings(value = "ACEM_ABSTRACT_CLASS_EMPTY_METHODS",
	justification = "We want to provide a default implementation on not commonly used methods")
@SuppressWarnings("PMD.TooManyMethods")
public abstract class BaseStruct extends ActionBarActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onSetWindowFeature();
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getSupportActionBar().setTitle(getCurrentTitle());
	}

	/**
	 * You must define the title that you want to appear next
	 * to the logo
	 */
	@NonNull
	protected abstract String getCurrentTitle();


	/**
	 * Override if you want to add window features
	 */
	@SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
	public void onSetWindowFeature() {
		/*
		 *  No default implementation, and we don't want this to be defined every time on children.
		 *  This method should be overridden only when we want to add any window feature.
		 */
	}

	/**
	 * Replacement the content fragment with a new one
	 *
	 * @param f The new fragment
	 * @param popPrevious True if you want to pop back stack immediate
	 */
	public void replaceFragment(@NonNull final Fragment f, final boolean popPrevious) {
		replaceFragment(f, popPrevious, 0);
	}

	/**
	 * Replace the content fragment with no back stack. The id of
	 * the framelayout is the one we get from {@link #getMainFragmentPlaceholder()}
	 *
	 * @param f The new fragment
	 */
	public void addFragmentWithNoBackstack(@NonNull final Fragment f) {
		addFragmentWithNoBackstack(f, getMainFragmentPlaceholder());
	}

	/**
	 * Replace the content fragment with no back stack. You can specify
	 * the id
	 *
	 * @param f The new fragment
	 * @param fragmentLayoutId The id of the framelayout that you want to replace
	 */
	public void addFragmentWithNoBackstack(@NonNull final Fragment f,
					@IdRes final int fragmentLayoutId) {
		final FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(fragmentLayoutId, f);
		t.commit();
	}

	/**
	 * Replacement the content fragment with a new one
	 *
	 * @param f The new fragment
	 * @param popPrevious True if you want to pop back stack immediate
	 * @param transition The transition when the fragment is being replaced
	 */
	public void replaceFragment(@NonNull final Fragment f, final boolean popPrevious,
					final int transition) {
		replaceFragment(f, null, popPrevious, transition, true);
	}

	/**
	 * Replacement the content fragment with a new one
	 *
	 * @param f The new fragment
	 * @param tag A tag that you want to add to the fragment
	 * @param popPrevious True if you want to pop back stack immediate
	 * @param transition The transition when the fragment is being replaced
	 * @param fragmentId The id of the framelayout that you want to replace
	 * @param addToBackStack Tue you want to add back stack, false otherwise
	 */
	public void replaceFragment(@NonNull final Fragment f, @Nullable final String tag,
					final boolean popPrevious, final int transition, @IdRes final int fragmentId,
					final boolean addToBackStack) {
		if (popPrevious) {
			getSupportFragmentManager().popBackStackImmediate();
		}
		final FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(fragmentId, f, tag);
		if (transition > FragmentTransaction.TRANSIT_NONE) {
			t.setTransition(transition);
		}
		if (addToBackStack) {
			t.addToBackStack(null);
		}
		t.commit();
	}

	/**
	 * Replacement the content fragment with a new one. The frame layout is getting from
	 * {@link #getMainFragmentPlaceholder()}
	 *
	 * @param f The new fragment
	 * @param tag A tag that you want to add to the fragment
	 * @param popPrevious True if you want to pop back stack immediate
	 * @param transition The transition when the fragment is being replaced
	 * @param addToBackStack Tue you want to add back stack, false otherwise
	 */
	public void replaceFragment(@NonNull final Fragment f, @Nullable final String tag,
					final boolean popPrevious, final int transition, final boolean addToBackStack) {
		replaceFragment(f, tag, popPrevious, transition, getMainFragmentPlaceholder(), addToBackStack);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			final Intent upIntent = NavUtils.getParentActivityIntent(this);

			onUpPressed(upIntent);

			if (upIntent != null) {
				if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
					TaskStackBuilder.create(this)
						.addNextIntentWithParentStack(upIntent)
						.startActivities();
				} else {
					NavUtils.navigateUpTo(this, upIntent);
				}
			}

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * This method is call when you press back and if the content
	 * fragment exists
	 */
	public abstract void fragmentExistBackPressed(@Nullable final Fragment fragment);

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		final Fragment fragment = getSupportFragmentManager().findFragmentById(getMainFragmentPlaceholder());
		if (fragment != null) {
			fragmentExistBackPressed(fragment);
		}
	}

	/**
	 * This method is used when you call {@link #replaceFragment(android.support.v4.app.Fragment, boolean)}
	 * or any method of it's family without frame layout.
	 *
	 * @return The id of the frame layout that your class uses
	 */
	@IdRes
	protected abstract int getMainFragmentPlaceholder();

	/**
	 * This method let's you handle parent's activity intent. e.g: to provide extras or flags.
	 *
	 * @param upIntent the intent.
	 */
	@SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
	protected void onUpPressed(@Nullable final Intent upIntent) {
		/*
		 *  No default implementation, and we don't want this to be defined every time on children.
		 *  This method should be overridden only when we want to change UP navigation's default behavior.
		 */
	}
}
