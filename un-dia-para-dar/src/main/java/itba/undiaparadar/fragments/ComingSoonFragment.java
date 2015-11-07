package itba.undiaparadar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import itba.undiaparadar.R;
import itba.undiaparadar.interfaces.TitleProvider;

/**
 * Created by mpurita on 11/7/15.
 */
public class ComingSoonFragment extends Fragment implements TitleProvider {
	private static final String TITLE = "TITLE";

	public static Fragment newInstance(final String title) {
		final Fragment fragment = new ComingSoonFragment();
		final Bundle bundle = new Bundle();
		bundle.putString(TITLE, title);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
		final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.coming_soon, null, false);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public String getTitle() {
		final Bundle bundle = getArguments();
		if (bundle != null) {
			final String title = bundle.getString(TITLE);
			return title;
		}
		return "";
	}
}
