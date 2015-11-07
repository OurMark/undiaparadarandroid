package itba.undiaparadar.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.facebook.Profile;
import com.facebook.ProfileTracker;

import javax.inject.Inject;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.interfaces.TitleProvider;
import itba.undiaparadar.utils.CircularImageView;

public class ProfileFragment extends Fragment implements TitleProvider {
	private static final int MAX_PHOTO_DIMENSION = 1920;
	private static final int BORDER_WIDTH = 10;
	private Profile userProfile;
	@Inject
	private ImageLoader imageLoader;
	private View root;

	public static Fragment newInstance() {
		return new ProfileFragment();
	}

	@Override
	public void onCreate(final @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UnDiaParaDarApplication.injectMembers(this);
		final ProfileTracker profileTracker = new ProfileTracker() {
			@Override
			protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
				userProfile = currentProfile;
				updateView();
			}
		};
		userProfile = Profile.getCurrentProfile();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
		final Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_profile, container, false);
		updateView();

		return root;
	}

	private void updateView() {
		final CircularImageView profileImage = (CircularImageView) root.findViewById(R.id.profile_img);
		profileImage.setBorderWidth(BORDER_WIDTH);
		profileImage.setImageUrl(userProfile
						.getProfilePictureUri(
								MAX_PHOTO_DIMENSION,
								MAX_PHOTO_DIMENSION).toString(),
				imageLoader);
		final TextView profileName = (TextView) root.findViewById(R.id.profile_name);
		profileName.setText(userProfile.getName());
	}

	@Override
	public String getTitle() {
		return getString(R.string.profile);
	}
}
