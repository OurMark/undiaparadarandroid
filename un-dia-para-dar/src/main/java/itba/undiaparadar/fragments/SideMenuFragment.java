package itba.undiaparadar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.monits.skeletor.interfaces.FragmentFactory;

import javax.inject.Inject;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.activities.MainActivity;
import itba.undiaparadar.listeners.BrowserListener;
import itba.undiaparadar.services.SettingsService;
import itba.undiaparadar.utils.NetworkUtils;

public class SideMenuFragment extends Fragment {
	private static final String TERMS_AND_CONDITIONS_URL = "http://undiaparadar.net/tyc/#.VgNBu49Vikq";
	private final SparseArray<FragmentFactory> fragmentMap = new SparseArray<>();
	private Profile userProfile;
	@Inject
	private ImageLoader imageLoader;
	@Inject
	private SettingsService settingsService;
	private View root;

	public static SideMenuFragment newInstance() {
		return new SideMenuFragment();
	}

	public SideMenuFragment() {
		UnDiaParaDarApplication.injectMembers(this);
		fragmentMap.put(R.id.profile_menu, new ProfileFragmentFactory());
		fragmentMap.put(R.id.topics_menu, new TopicsFragmentFactory());
		fragmentMap.put(R.id.map_menu, new MapFragmentFactory());
		fragmentMap.put(R.id.achievements_menu, new AchievementsFragmentFactory());
		fragmentMap.put(R.id.statistics_menu, new StatisticsFragmentFactory());
		fragmentMap.put(R.id.what_is_udpd_menu, new WhatIsUDPDFragmentFactory());

	}

	@Override
	public void onCreate(final @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userProfile = Profile.getCurrentProfile();
		final ProfileTracker profileTracker = new ProfileTracker() {
			@Override
			protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
				userProfile = currentProfile;
				updateView();
			}
		};
		profileTracker.startTracking();

	}

	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
	                         final Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.side_menu, container, false);

		updateView();
		return root;
	}

	private void updateView() {
		final NetworkImageView profileImage = (NetworkImageView) root.findViewById(R.id.profile_img);
		profileImage.setDefaultImageResId(R.drawable.menu_profile_placeholder);
		if (userProfile != null) {
			profileImage.setImageUrl(userProfile
							.getProfilePictureUri(
									profileImage.getMaxWidth(),
									profileImage.getHeight()).toString(),
					imageLoader);
			final TextView profileName = (TextView) root.findViewById(R.id.profile_name);
			profileName.setText(userProfile.getName());
		}
	}

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final MainActivity topActivity = (MainActivity) getActivity();
		topActivity.setSidebarListener(view, fragmentMap);

		final LinearLayout terms = (LinearLayout) view.findViewById(R.id.term_and_conditions_menu);

		terms.setOnClickListener(new BrowserListener(getActivity(), TERMS_AND_CONDITIONS_URL));
	}

	private static class ProfileFragmentFactory implements FragmentFactory {
		private static final long serialVersionUID = -7188229760615810974L;

		@Override
		public Fragment newFragment() {
			return ProfileFragment.newInstance();
		}
	}

	private static class TopicsFragmentFactory implements FragmentFactory {
		private static final long serialVersionUID = -7188229760615810973L;

		@Override
		public Fragment newFragment() {
			return TopicsFragment.newInstance();
		}
	}

	private static class MapFragmentFactory implements FragmentFactory {
		private static final long serialVersionUID = -7188229760615810972L;

		@Override
		public Fragment newFragment() {
			final Fragment fragment;
			if (NetworkUtils.isInternetConnection(UnDiaParaDarApplication.getAppContext())) {
				fragment = MapFragment.newInstance();
			} else {
				fragment = NoConnectionFragment.newInstance(this);
			}
			return fragment;
		}
	}

	private static class AchievementsFragmentFactory implements FragmentFactory {
		private static final long serialVersionUID = -7188229760615810972L;

		@Override
		public Fragment newFragment() {
			return null;//AchievementsFragment.newInstance();
		}
	}

	private static class StatisticsFragmentFactory implements FragmentFactory {
		private static final long serialVersionUID = -7188229760615810972L;

		@Override
		public Fragment newFragment() {
			return null;//AchievementsFragment.newInstance();
		}
	}

	private static class WhatIsUDPDFragmentFactory implements FragmentFactory {
		private static final long serialVersionUID = -7188229760615810972L;

		@Override
		public Fragment newFragment() {
			return null;//AchievementsFragment.newInstance();
		}
	}
}
