package itba.undiaparadar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import itba.undiaparadar.R;

/**
 * Created by mpurita on 11/7/15.
 */
public class ComingSoonFragment extends Fragment {

	public static Fragment newInstance() {
		return new ComingSoonFragment();
	}

	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
		final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.coming_soon, null, false);
	}
}
