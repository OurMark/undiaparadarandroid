package itba.undiaparadar.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.activities.FilterActivity;
import itba.undiaparadar.adapter.MapFilterItemAdapter;
import itba.undiaparadar.interfaces.TitleProvider;
import itba.undiaparadar.model.PositiveAction;
import itba.undiaparadar.model.Topic;
import itba.undiaparadar.model.UnDiaParaDarMarker;
import itba.undiaparadar.services.TopicService;

public class MapFragment extends Fragment implements TitleProvider {
	private static final int CHANGE_FILTER = 1;
	private static final int NO_RADIUS = -1;
	private static final String TOPICS = "TOPICS";
	private SupportMapFragment mapFragment;
	private GoogleMap mMap;
	private HashMap<Long, Topic> topics;
	@Inject
	private TopicService topicService;
	private Map<Marker, UnDiaParaDarMarker> markers;
	private ArrayList<Topic> selectedTopics;
	private MapFilterItemAdapter adapter;
	private boolean editable;
	private Menu menu;
	private View root;

	public MapFragment() {
		this.markers = new HashMap<>();
	}

	public static Fragment newInstance() {
		return new MapFragment();
	}

	public static Fragment newInstance(final HashMap<Long, Topic> topics) {
		final Bundle bundle = new Bundle();
		bundle.putSerializable(TOPICS, topics);
		final Fragment fragment = new MapFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UnDiaParaDarApplication.injectMembers(this);
		final Bundle bundle = getArguments();
		if (bundle != null) {
			topics = (HashMap<Long, Topic>) bundle.getSerializable(TOPICS);
			setHasOptionsMenu(true);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
	                         final Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_map, container, false);

		if (topics == null) {
			topics = topicService.createTopics(getActivity());
			for (final Topic topic : topics.values()) {
				topic.select();
			}
		}
		selectedTopics = (ArrayList<Topic>) topicService.getSelectedTopics(topics.values());
		final GridView gridView = (GridView) root.findViewById(R.id.selected_topics);
		gridView.bringToFront();
		gridView.invalidate();
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long l) {
				if (editable) {
					final ImageView img = (ImageView) view.findViewById(R.id.topic_img);
					topicService.loadImageResId(adapter.getItem(position), img);
				}
			}
		});
		adapter = new MapFilterItemAdapter(getActivity(), selectedTopics, R.layout.map_filter_item);
		gridView.setAdapter(adapter);
		return root;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
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

	private void refreshMap() {
		mMap.clear();
		markers.clear();
		setupMap();
	}

	private void setupMap() {
		final ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setTitle("Loading");
		dialog.show();
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mMap.getUiSettings().setCompassEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		if (selectedTopics != null) {
			retrievePositiveActions(selectedTopics, dialog);
		} else {
			retrievePositiveActions(topics.values(), dialog);
		}
//        final LatLng latLng = new LatLng(-33.796923, 150.922433);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
	}

	private void retrievePositiveActions(final Collection<Topic> selectedTopics, final ProgressDialog dialog) {
		topicService.getPositiveActionsForTopics(selectedTopics, new Response.Listener<List<PositiveAction>>() {
			@Override
			public void onResponse(final List<PositiveAction> positiveActions) {
				dialog.dismiss();
				for (final PositiveAction positiveAction : positiveActions) {
					final MarkerOptions markerOptions = new MarkerOptions();
					markerOptions.position(new LatLng(positiveAction.getLatitude(),
							positiveAction.getLongitude()));
					markerOptions.title(positiveAction.getTitle());
					final Topic topic = topics.get(new Long(positiveAction.getTopicId()));
					final Bitmap icon = BitmapFactory.decodeResource(getResources(), topic.getEnableImageResId());
					final int dimen = (int) getResources().getDimension(R.dimen.m2);
					markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(icon,
							dimen, dimen, false)));
					final Marker marker = mMap.addMarker(markerOptions);
					markers.put(marker, new UnDiaParaDarMarker(topic, positiveAction));
					mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
						@Override
						public boolean onMarkerClick(final Marker marker) {
							updatePositiveActionView(markers.get(marker));
							return true;
						}
					});
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(final VolleyError volleyError) {
				dialog.dismiss();
				Log.d("Error", "error: " + volleyError);
			}
		});
	}

	private void updatePositiveActionView(final UnDiaParaDarMarker unDiaParaDarMarker) {
		final TextView title = (TextView) root.findViewById(R.id.title);
		title.setText(unDiaParaDarMarker.getPositiveAction().getTitle());
		title.setVisibility(View.VISIBLE);
	}

	@Override
	public String getTitle() {
		return getString(R.string.map);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == R.id.filter) {
			startActivityForResult(FilterActivity.getIntent(getActivity(), getCurrentTopics()), CHANGE_FILTER);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	private ArrayList<Topic> getCurrentTopics() {
		final List<Topic> adapterItems = adapter.getItems();
		for (final Topic topic : topics.values()) {
			if (!adapterItems.contains(topic)) {
				adapterItems.add(topic);
			}
		}
		return (ArrayList<Topic>) adapterItems;
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		this.menu = menu;
		this.menu.clear();
		inflater.inflate(R.menu.menu_map, this.menu);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CHANGE_FILTER) {
			if (resultCode == FilterActivity.FILTER_RESULT) {
				final ArrayList<Topic> topicsFiltered = (ArrayList<Topic>) data
						.getSerializableExtra(FilterActivity.TOPICS);
				final int radius = data.getIntExtra(FilterActivity.RADIUS, NO_RADIUS);
				adapter.setItems(topicsFiltered);
			}
		}
	}
}
