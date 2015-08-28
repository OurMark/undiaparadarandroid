package itba.undiaparadar.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import itba.undiaparadar.R;
import itba.undiaparadar.interfaces.TitleProvider;

public class MapFragment extends Fragment implements TitleProvider {

	public static Fragment newInstance() {
		return new MapFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_map, container, false);
	}


	@Override
	public String getTitle() {
		return getString(R.string.map);
	}
}
