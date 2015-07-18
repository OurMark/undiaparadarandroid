package itba.undiaparadar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.Profile;
import com.monits.skeletor.interfaces.FragmentFactory;

import javax.inject.Inject;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.activities.MainActivity;

public class SideMenuFragment extends Fragment {
	private final SparseArray<FragmentFactory> fragmentMap = new SparseArray<>();
	private Profile userProfile;
	@Inject
	private ImageLoader imageLoader;

	public static SideMenuFragment newInstance() {
		return new SideMenuFragment();
	}

	public SideMenuFragment() {
		UnDiaParaDarApplication.injectMembers(this);
		fragmentMap.put(R.id.profile_menu, new ProfileFragmentFactory());
	}

	@Override
	public void onCreate(final @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userProfile = Profile.getCurrentProfile();
	}

	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
	                         final Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.side_menu, container, false);

		final NetworkImageView profileImage = (NetworkImageView) root.findViewById(R.id.profile_img);
		profileImage.setImageUrl(userProfile
				.getProfilePictureUri(
						profileImage.getMaxWidth(),
						profileImage.getHeight()).toString(),
				imageLoader);
		final TextView profileName = (TextView) root.findViewById(R.id.profile_name);
		profileName.setText(userProfile.getName()   );

		return root;
	}

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final MainActivity topActivity = (MainActivity) getActivity();
		topActivity.setSidebarListener(view, fragmentMap);
	}

	private static class ProfileFragmentFactory implements FragmentFactory {
		private static final long serialVersionUID = -7188229760615810973L;

		@Override
		public Fragment newFragment() {
			return ProfileFragment.newInstance();
		}
	}
}
