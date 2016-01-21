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
public class PledgeVerificationFragment extends Fragment implements TitleProvider {

	public static Fragment newInstance() {
		return new PledgeVerificationFragment();
	}

	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
		final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_pledge_verification, null, false);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public String getTitle() {
		return getString(R.string.pledge_verification);
	}
}
