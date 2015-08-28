package itba.undiaparadar.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import itba.undiaparadar.R;
import itba.undiaparadar.interfaces.TitleProvider;

public class NewsFragment extends Fragment implements TitleProvider {

	public static Fragment newInstance() {
		return new NewsFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_news, container, false);
	}


	@Override
	public String getTitle() {
		return getString(R.string.news);
	}
}
