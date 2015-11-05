package itba.undiaparadar.services;

import android.content.SharedPreferences;

import com.google.inject.Inject;

/**
 * Created by mpurita on 9/23/15.
 */
public class SettingsServiceImpl implements SettingsService {
    private static final String EMAIL = "EMAIL";
    private static final String RADIUS_FILTER = "RADIUS_FILTER";

    @Inject
    private SharedPreferences sharedPreferences;

    @Override
    public void saveEmail(final String email) {
        sharedPreferences.edit().putString(EMAIL, email)
                .apply();
    }

    @Override
    public String retrieveEmail() {
        return sharedPreferences.getString(EMAIL, null);
    }

    @Override
    public void saveRadiusFilter(final boolean radiusFilter) {
        sharedPreferences.edit()
            .putBoolean(RADIUS_FILTER, radiusFilter)
            .apply();
    }

    @Override
    public boolean retrieveRadiusFilter() {
        return sharedPreferences.getBoolean(RADIUS_FILTER, false);
    }
}
