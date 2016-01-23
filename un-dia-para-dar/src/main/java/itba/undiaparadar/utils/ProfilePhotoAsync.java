package itba.undiaparadar.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.inject.Inject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.services.UserService;

public class ProfilePhotoAsync extends AsyncTask<String, String, String> {
    private Bitmap bitmap;
    @Inject
    private UserService userService;

    String url;
    public ProfilePhotoAsync(String url) {
        UnDiaParaDarApplication.injectMembers(this);
        this.url = url;
    }
    @Override
    protected String doInBackground(String... params) {
        // Fetching data from URI and storing in bitmap
        bitmap = DownloadImageBitmap(url);
        userService.getUser().setPicture(bitmap);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        userService.saveNewUser();
    }

    public static Bitmap DownloadImageBitmap(final String url) {
        Bitmap bm = null;
        try {
            final URL aURL = new URL(url);
            final URLConnection conn = aURL.openConnection();
            conn.connect();
            final InputStream is = conn.getInputStream();
            final BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (final IOException e) {
            Log.e("IMAGE", "Error getting bitmap", e);
        }
        return bm;
    }
}