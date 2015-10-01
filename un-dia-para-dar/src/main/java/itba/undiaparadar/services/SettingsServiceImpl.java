package itba.undiaparadar.services;

import android.content.SharedPreferences;

import com.google.inject.Inject;

/**
 * Created by mpurita on 9/23/15.
 */
public class SettingsServiceImpl implements SettingsService {
    private static final String EMAIL = "EMAIL";

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
}
