package itba.undiaparadar.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.monits.skeletor.interfaces.FragmentFactory;

import itba.undiaparadar.R;
import itba.undiaparadar.utils.GifDrawable;
import itba.undiaparadar.utils.NetworkUtils;

/**
 * Created by mpurita on 11/7/15.
 */
public class NoConnectionFragment extends Fragment {
	private static final String FRAGMENT_FACTORY = "FRAGMENT_FACTORY";

	public static Fragment newInstance(final FragmentFactory fragmentFactory) {
		final Fragment fragment = new NoConnectionFragment();
		final Bundle bundle = new Bundle();
		bundle.putSerializable(FRAGMENT_FACTORY, fragmentFactory);

		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
	}

	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.no_connection, container, false);
		final ImageView brokenHeart = (ImageView) root.findViewById(R.id.broken_heart);
		final Drawable brokenHeartGif = new GifDrawable(R.raw.logo_no_connection, getActivity());
		brokenHeart.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		brokenHeart.setImageDrawable(brokenHeartGif);

		final Button connectionRetry = (Button) root.findViewById(R.id.connection_retry);
		connectionRetry.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (NetworkUtils.isInternetConnection(getActivity())) {
					final Bundle bundle = getArguments();
					if (bundle != null) {
						final FragmentFactory fragmentFactory = (FragmentFactory) bundle
								.getSerializable(FRAGMENT_FACTORY);
						getFragmentManager()
								.beginTransaction()
								.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
								.replace(R.id.main_content, fragmentFactory.newFragment())
								.commit();
					}
				}
			}
		});

		return root;
	}


}
