package itba.undiaparadar.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.Profile;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.adapter.LastPledgeItemAdapter;
import itba.undiaparadar.interfaces.TitleProvider;
import itba.undiaparadar.model.Topic;
import itba.undiaparadar.model.UnDiaParaDarMarker;
import itba.undiaparadar.utils.CircularImageView;

public class ProfileFragment extends Fragment implements TitleProvider {
	private static final int MAX_PHOTO_DIMENSION = 1920;
	private static final int BORDER_WIDTH = 10;
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
		final CircularImageView profileImage = (CircularImageView) root.findViewById(R.id.profile_img);
		profileImage.setBorderWidth(BORDER_WIDTH);
		profileImage.setImageUrl(userProfile
						.getProfilePictureUri(
								MAX_PHOTO_DIMENSION,
								MAX_PHOTO_DIMENSION).toString(),
				imageLoader);
		final TextView profileName = (TextView) root.findViewById(R.id.profile_name);
		profileName.setText(userProfile.getName());

		final ListView listView = (ListView) root.findViewById(R.id.last_pledge_list);
		final LastPledgeItemAdapter adapter = new LastPledgeItemAdapter(getActivity());
		final List<UnDiaParaDarMarker> topics = new ArrayList<>();
		topics.add(new UnDiaParaDarMarker(new Topic(14, R.drawable.adopcion_de_mascotas, getString(R.string.adopcion_mascotas), R.drawable.adopcion_de_mascotas_gris), null));
		topics.add(new UnDiaParaDarMarker(new Topic(14, R.drawable.adopcion_de_mascotas, getString(R.string.adopcion_mascotas), R.drawable.adopcion_de_mascotas_gris), null));
		topics.add(new UnDiaParaDarMarker(new Topic(14, R.drawable.adopcion_de_mascotas, getString(R.string.adopcion_mascotas), R.drawable.adopcion_de_mascotas_gris), null));
		adapter.setItems(topics);
		listView.setAdapter(adapter);


		return root;
	}

	@Override
	public String getTitle() {
		return getString(R.string.profile);
	}
}
