package itba.undiaparadar.services;

/**
 * Created by mpurita on 9/23/15.
 */
public interface SettingsService {

    void saveEmail(final String email);

    String retrieveEmail();
}
