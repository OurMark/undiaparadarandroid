package itba.undiaparadar.fragments;


import android.net.Uri;
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
import com.facebook.ProfileTracker;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.parse.CountCallback;
import com.parse.ParseException;

import javax.inject.Inject;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.interfaces.TitleProvider;
import itba.undiaparadar.services.PledgeService;
import itba.undiaparadar.services.UserService;

public class ProfileFragment extends Fragment implements TitleProvider {
	private static final String UDPD_FACEBOOK = "https://www.facebook.com/UndiaparadarArgentina/?fref=ts";
	private static final int MAX_PHOTO_DIMENSION = 1920;
	private static final int BORDER_WIDTH = 10;
	private Profile userProfile;
	@Inject
	private ImageLoader imageLoader;
	private View root;
	@Inject
	private PledgeService pledgeService;
	@Inject
	private UserService userService;

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
		profileTracker.startTracking();
		userProfile = Profile.getCurrentProfile();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
		final Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_profile, container, false);
		updateView();

		final ShareButton shareButton = (ShareButton) root.findViewById(R.id.share_button);
		final ShareLinkContent content = new ShareLinkContent.Builder()
				.setContentUrl(Uri.parse(UDPD_FACEBOOK))
				.build();
		shareButton.setShareContent(content);

		LikeView likeView = (LikeView) root.findViewById(R.id.like_button);
		likeView.setObjectIdAndType(
				UDPD_FACEBOOK,
				LikeView.ObjectType.PAGE);

		setupProfileInfo();

		return root;
	}

	private void setupProfileInfo() {
		final TextView positiveActionsTotal = (TextView) root.findViewById(R.id.positive_actions_total);
		positiveActionsTotal.setText(String.valueOf(0));
		final TextView positiveActionsDone = (TextView) root.findViewById(R.id.positive_actions_done);
		positiveActionsDone.setText(String.valueOf(0));
		pledgeService.getPledgeDone(userService.getUser().getUserId(), new CountCallback() {
			@Override
			public void done(int count, ParseException e) {
				if (count < 0) {
					count = 0;
				}
				positiveActionsDone.setText(String.valueOf(count));
			}
		});
		pledgeService.getPledgeTotal(userService.getUser().getUserId(), new CountCallback() {
			@Override
			public void done(int count, ParseException e) {
				if (count < 0) {
					count = 0;
				}
				positiveActionsTotal.setText(String.valueOf(count));
			}
		});
	}

	private void updateView() {
		final NetworkImageView profileImage = (NetworkImageView) root.findViewById(R.id.profile_img);
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
