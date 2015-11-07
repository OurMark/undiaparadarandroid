package itba.undiaparadar.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mpurita on 11/7/15.
 */
public class NetworkUtils {

	public static boolean isInternetConnection(final Context context) {
		final ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return networkInfo != null && (networkInfo.isConnected() || networkInfo.isAvailable());
	}
}
