package itba.undiaparadar.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.Profile;

import javax.inject.Inject;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;

public class ProfileFragment extends Fragment {
	private Profile userProfile;
	@Inject
	private ImageLoader imageLoader;

	public static Fragment newInstance() {
		return new ProfileFragment();
	}

	@Override
	public void onCreate(final @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UnDiaParaDarApplication.injectMembers(this);
		userProfile = Profile.getCurrentProfile();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
		final Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.fragment_profile, container, false);
		final NetworkImageView profileImage = (NetworkImageView) root.findViewById(R.id.profile_img);
		profileImage.setImageUrl(userProfile
						.getProfilePictureUri(
								profileImage.getMaxWidth(),
								profileImage.getHeight()).toString(),
				imageLoader);
		final TextView profileName = (TextView) root.findViewById(R.id.profile_name);
		profileName.setText(userProfile.getName());
		return root;
	}
}
