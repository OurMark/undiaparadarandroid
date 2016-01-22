package itba.undiaparadar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import itba.undiaparadar.R;

public class ConfirmPledge extends Fragment {

	public static Fragment newInstance() {
		return new ConfirmPledge();
	}

	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.activity_pledge_verification, container, false);

		return root;
	}
}
