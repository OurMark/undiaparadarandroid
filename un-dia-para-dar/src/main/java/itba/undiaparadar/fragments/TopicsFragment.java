package itba.undiaparadar.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.inject.Inject;
import com.monits.skeletor.interfaces.FragmentFactory;

import java.util.HashMap;
import java.util.List;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.adapter.TopicsItemAdapter;
import itba.undiaparadar.interfaces.TitleProvider;
import itba.undiaparadar.model.Topic;
import itba.undiaparadar.services.TopicService;
import itba.undiaparadar.utils.NetworkUtils;

public class TopicsFragment extends Fragment implements TitleProvider {
	private View root;
	@Inject
	private TopicService topicService;
	private HashMap<Long, Topic> topics;

	public static Fragment newInstance() {
		return new TopicsFragment();
	}

	public TopicsFragment() {
		UnDiaParaDarApplication.injectMembers(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_topics, container, false);
		populateGrid();

		return root;
	}

	private void populateGrid() {
		final GridView gridView = (GridView) root.findViewById(R.id.topics_grid);

		topics = createTopics();
		final TopicsItemAdapter adapter = new TopicsItemAdapter(getActivity(), topics.values());

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> adapterView, final View view,
			                        final int position, final long id) {
				final ImageView img = (ImageView) view.findViewById(R.id.topic_img);
				topicService.loadImageResId(adapter.getItem(position), img);
				if (isAnyTopicSelected()) {
					setHasOptionsMenu(true);
				} else {
					setHasOptionsMenu(false);
				}
			}
		});

		gridView.setAdapter(adapter);


	}

	private HashMap<Long, Topic> createTopics() {
		return topicService.createTopics(getActivity());
	}


	@Override
	public String getTitle() {
		return getString(R.string.find_what_to_give);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == R.id.go_to_map) {
			final List<Topic> selectedTopics = topicService.getSelectedTopics(topics.values());
			if (!selectedTopics.isEmpty()) {
				final Fragment fragment;
				if (NetworkUtils.isInternetConnection(getActivity())) {
					fragment = MapFragment.newInstance(topics);
				} else {
					final FragmentFactory fragmentFactory = new MapFragmentFactory();
					fragment = NoConnectionFragment.newInstance(fragmentFactory);
				}
				getFragmentManager()
						.beginTransaction()
						.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
						.replace(R.id.main_content, fragment)
						.commit();
			}
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.menu_topic, menu);
	}

	private boolean isAnyTopicSelected() {
		for (final Topic topic : topics.values()) {
			if (topic.isSelected()) {
				return true;
			}
		}
		return false;
	}

	private class MapFragmentFactory implements FragmentFactory {
		private static final long serialVersionUID = -7188229760615810972L;

		@Override
		public Fragment newFragment() {
			return MapFragment.newInstance(topics);
		}
	}
}
