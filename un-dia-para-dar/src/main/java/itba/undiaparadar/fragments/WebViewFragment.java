package itba.undiaparadar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import itba.undiaparadar.R;

/**
 * Created by mpurita on 11/20/15.
 */
public class WebViewFragment extends Fragment {
	private static final String FILE_NAME = "FILE_NAME";
	private static final String ASSET_PATH = "file:///android_asset/";

	public static Fragment newInstance(final String fileName) {
		final Fragment fragment = new WebViewFragment();
		final Bundle arguments = new Bundle();

		arguments.putString(FILE_NAME, fileName);

		fragment.setArguments(arguments);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.web_view, container, false);

		final Bundle arguments = getArguments();

		final String fileName = arguments.getString(FILE_NAME);
		final WebView webView = (WebView) root.findViewById(R.id.webView);
		webView.loadUrl(ASSET_PATH + fileName);

		return root;
	}
}
