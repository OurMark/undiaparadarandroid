package itba.undiaparadar.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.adapter.MapFilterItemAdapter;
import itba.undiaparadar.model.Topic;
import itba.undiaparadar.services.SettingsService;
import itba.undiaparadar.services.TopicService;
import itba.undiaparadar.utils.Constants;

/**
 * This class represents the filter screen. Here is where you can
 * change which topics you want to search and if you want to search
 * in a specific radius or not
 *
 * @author Martin Purita - martinpurita@gmail.com
 */
public class FilterActivity extends CircularRevealActivity {
	// Google injections
	@Inject
	private TopicService topicService;
	@Inject
	private SettingsService settingService;

	// View elements
	private ViewHolder holder;

	public static Intent getIntent(final Context context, final Collection<Topic> topics) {
		final Intent intent = new Intent(context, FilterActivity.class);
		intent.putExtra(Constants.ExtraKeys.TOPICS, new ArrayList<>(topics));
		return intent;
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UnDiaParaDarApplication.injectMembers(this);
		holder = new ViewHolder();
		populateViewHolder();
		setUpView();
	}

	private void populateViewHolder() {
		holder.topics = (ArrayList<Topic>) getIntent().getSerializableExtra(Constants.ExtraKeys.TOPICS);
		holder.accept = (Button) findViewById(R.id.accept);
		holder.radiusSeekBar = (SeekBar) findViewById(R.id.radius_seek_bar);
		holder.radiusSwitch = (Switch) findViewById(R.id.radius_switch);
		holder.adapter = new MapFilterItemAdapter(this, holder.topics, R.layout.filter_item);
	}

	@Override
	int getContentLayoutId() {
		return R.layout.filter_actiivty;
	}

	private void setUpView() {
		setUpRadiusView();
		setUpTopicsView();
		setUpContinueListeners();
	}

	private void setUpContinueListeners() {
		final Button cancel = (Button) findViewById(R.id.cancel);

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				exitReveal();
			}
		});

		holder.accept.setOnClickListener(createAcceptListener());
	}

	@NonNull
	private View.OnClickListener createAcceptListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final Intent data = new Intent();
				final ArrayList<Topic> selectedTopic = (ArrayList<Topic>) topicService.getSelectedTopics(holder.topics);
				data.putExtra(Constants.ExtraKeys.TOPICS, selectedTopic);
				if (holder.radiusSwitch.isChecked()) {
					data.putExtra(Constants.ExtraKeys.RADIUS, holder.radiusSeekBar.getProgress() + Constants.Value.MIN_RADIUS);
				}
				setResult(Constants.ResultCode.FILTER, data);
				settingService.saveRadiusFilter(holder.radiusSwitch.isChecked());
				exitReveal();
			}
		};
	}

	private void setUpRadiusView() {
		final TextView radiusNumbers = (TextView) findViewById(R.id.radius_number);
		radiusNumbers.setText(getString(R.string.radius_covered, holder.radiusSeekBar.getProgress() + Constants.Value.MIN_RADIUS));

		holder.radiusSeekBar.setOnSeekBarChangeListener(createSeekBarChangeListener(radiusNumbers));
		holder.radiusSwitch.setChecked(settingService.retrieveRadiusFilter());
		final TextView radiusTitle = (TextView) findViewById(R.id.radius_title);
		enableDisableRadiusFilter(holder.radiusSwitch.isChecked(), holder.radiusSeekBar, radiusNumbers, radiusTitle);
		holder.radiusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
				enableDisableRadiusFilter(isChecked, holder.radiusSeekBar, radiusNumbers, radiusTitle);
			}
		});
	}

	@NonNull
	private SeekBar.OnSeekBarChangeListener createSeekBarChangeListener(final TextView radiusNumbers) {
		return new SeekBar.OnSeekBarChangeListener() {
			int progress = 0;

			@Override
			public void onProgressChanged(final SeekBar seekBar, final int progresValue, final boolean fromUser) {
				progress = progresValue;
				radiusNumbers.setText(getString(R.string.radius_covered, Constants.Value.MIN_RADIUS + holder.radiusSeekBar.getProgress()));
			}

			@Override
			public void onStartTrackingTouch(final SeekBar seekBar) {
				// No need to implement
			}

			@Override
			public void onStopTrackingTouch(final SeekBar seekBar) {
				// No need to implement
			}
		};
	}

	private void enableDisableRadiusFilter(final boolean isChecked, final SeekBar radiusSeekBar,
		final TextView radiusNumbers, final TextView radiusTitle) {
		radiusSeekBar.setEnabled(isChecked);
		radiusNumbers.setEnabled(isChecked);
		radiusTitle.setEnabled(isChecked);
	}

	private void setUpTopicsView() {
		final ToggleButton selectAll = (ToggleButton) findViewById(R.id.select_all);
		final ToggleButton unselectAll = (ToggleButton) findViewById(R.id.unselect_all);
		selectAll.setOnClickListener(createSelectAllListener(selectAll, unselectAll));
		unselectAll.setOnClickListener(createUnselectAllListener(selectAll, unselectAll));
		final GridView topicsGrid = (GridView) findViewById(R.id.grid_topics);
		topicsGrid.setOnItemClickListener(createOnItemClickListener(selectAll, unselectAll));
		topicsGrid.setAdapter(holder.adapter);
	}

	@NonNull
	private AdapterView.OnItemClickListener createOnItemClickListener(final ToggleButton selectAll, final ToggleButton unselectAll) {
		return new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long l) {
				final ImageView img = (ImageView) view.findViewById(R.id.topic_img);
				topicService.loadImageResId(holder.adapter.getItem(position), img);
				holder.accept.setEnabled(!topicService.getSelectedTopics(holder.adapter.getItems()).isEmpty());
				selectAll.setChecked(false);
				unselectAll.setChecked(false);
			}
		};
	}

	@NonNull
	private View.OnClickListener createUnselectAllListener(final ToggleButton selectAll, final ToggleButton unselectAll) {
		return new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				unselectAll.setChecked(true);
				selectAll.setChecked(false);
				holder.accept.setEnabled(false);
				holder.adapter.unselectAllTopics();
			}
		};
	}

	@NonNull
	private View.OnClickListener createSelectAllListener(final ToggleButton selectAll, final ToggleButton unselectAll) {
		return new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				selectAll.setChecked(true);
				unselectAll.setChecked(false);
				holder.accept.setEnabled(true);
				holder.adapter.selectAllTopics();
			}
		};
	}

	private static class ViewHolder {
		private List<Topic> topics;
		private Switch radiusSwitch;
		private SeekBar radiusSeekBar;
		private MapFilterItemAdapter adapter;
		private Button accept;
	}
}
