package itba.undiaparadar.fragments;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import itba.undiaparadar.R;
import itba.undiaparadar.interfaces.TitleProvider;

public class MapFragment extends Fragment implements TitleProvider {
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;

    public static Fragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ActionBarActivity actionBarActivity = (ActionBarActivity) getActivity();
        actionBarActivity.getSupportActionBar().setTitle(getTitle());
        final FragmentManager fm = getChildFragmentManager();

        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
      /*
       * Map Initialization will come later, just making sure that, if we have a new instance of the fragment,
       * Also have the GoogleMap reference from that fragment.
       */
            mMap = null;
        }

        fm.beginTransaction().replace(R.id.map, mapFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupMapIfNeeded();
    }

    /**
     * Sets up the map configuration.
     * <p>
     * This should be called after {@link MapFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)} otherwise,
     * map could return null and never set up at all.
     */
    private void setupMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = mapFragment.getMap();

      /*
       * Check if we were successful in obtaining the map. This can fail if GooglePlayServices are not available, or if
       * StoresMapFragment has not gone through onCreateView.
       */
            if (mMap != null) {
                setupMap();
            }
        }
    }

    private void setupMap() {
        final LatLng latLng = new LatLng(-33.796923, 150.922433);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("My Spot")
                .snippet("This is my spot!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }

    @Override
    public String getTitle() {
        return getString(R.string.map);
    }
}
