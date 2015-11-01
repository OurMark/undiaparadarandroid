package itba.undiaparadar.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.adapter.MapFilterItemAdapter;
import itba.undiaparadar.model.Topic;
import itba.undiaparadar.services.TopicService;

/**
 * Created by mpurita on 10/31/15.
 */
public class FilterActivity extends Activity {
	private static final String TOPICS = "TOPICS";
	private static final int CIRCULAR_REVEAL_TRANSITION = 500;
	private FrameLayout rootView;
	private ViewTreeObserver.OnGlobalLayoutListener viewTreeObserverListener;
	private List<Topic> topics;
	@Inject
	private TopicService topicService;

	public static Intent getIntent(final Context context, final Collection<Topic> topics) {
		final Intent intent = new Intent(context, FilterActivity.class);
		intent.putExtra(TOPICS, new ArrayList<>(topics));
		return intent;
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UnDiaParaDarApplication.injectMembers(this);
		overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
		setContentView(R.layout.filter_activity);
		topics = (ArrayList<Topic>) getIntent().getSerializableExtra(TOPICS);
		if (savedInstanceState == null) {
			initializeAnimation();
		}
		setUpView();
	}

	private void setUpView() {
		final TextView radiusNumbers = (TextView) findViewById(R.id.radius_number);
		final SeekBar radiusSeekBar = (SeekBar) findViewById(R.id.radius_seek_bar);
		radiusNumbers.setText(getString(R.string.radius_covered, radiusSeekBar.getProgress()));

		radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			int progress = 0;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
				progress = progresValue;
				radiusNumbers.setText(getString(R.string.radius_covered, radiusSeekBar.getProgress()));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
		final Switch radiusSwitch = (Switch) findViewById(R.id.radius_switch);
		final TextView radiusTitle = (TextView) findViewById(R.id.radius_title);
		radiusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
				radiusSeekBar.setEnabled(isChecked);
				radiusNumbers.setEnabled(isChecked);
				radiusTitle.setEnabled(isChecked);
			}
		});
		setUpGridView();
	}

	private void setUpGridView() {
		final GridView topicsGrid = (GridView) findViewById(R.id.grid_topics);
		final MapFilterItemAdapter adapter = new MapFilterItemAdapter(this, topics, R.layout.filter_item);
		topicsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long l) {
				final ImageView img = (ImageView) view.findViewById(R.id.topic_img);
				topicService.loadImageResId(adapter.getItem(position), img);
			}
		});
		topicsGrid.setAdapter(adapter);
	}

	private void initializeAnimation() {
		setFinishOnTouchOutside(false);
		rootView = (FrameLayout) findViewById(R.id.root_layout);
		final ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
		if (viewTreeObserver.isAlive()) {
			viewTreeObserverListener = new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						enterReveal();
					}
				}
			};
			viewTreeObserver.addOnGlobalLayoutListener(viewTreeObserverListener);
		}
	}

	private void exitReveal() {
		// get the center for the clipping circle
		int cx = rootView.getMeasuredWidth() / 2;
		int cy = rootView.getMeasuredHeight() / 2;

		// get the final radius for the clipping circle
		int initialRadius = Math.max(rootView.getWidth(), rootView.getHeight()) / 2;
		revealAnimation(cx, cy, initialRadius, 0, true);
	}

	private void enterReveal() {
		// get the center for the clipping circle
		int cx = rootView.getMeasuredWidth() / 2;
		int cy = rootView.getMeasuredHeight() / 2;

		// get the final radius for the clipping circle
		int finalRadius = Math.max(rootView.getWidth(), rootView.getHeight()) / 2;
		revealAnimation(cx, cy, 0, finalRadius, false);
	}

	private void revealAnimation(final int cx, final int cy, final int initialRadius,
	                             final int finalRadius, final boolean isClosing) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

			// create the animator for this view (the start radius is zero)
			Animator anim = ViewAnimationUtils.createCircularReveal(rootView, cx, cy, initialRadius, finalRadius);
			anim.setDuration(CIRCULAR_REVEAL_TRANSITION);
			anim.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					if (isClosing) {
						rootView.setVisibility(View.INVISIBLE);
						finish();
					} else {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
							rootView.getViewTreeObserver().removeOnGlobalLayoutListener(viewTreeObserverListener);
						}
					}
				}

				@Override
				public void onAnimationCancel(Animator animation) {

				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}
			});

			// make the view visible and start the animation
			rootView.setVisibility(rootView.VISIBLE);
			anim.start();
		}
	}

	@Override
	public void onBackPressed() {
		exitReveal();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			exitReveal();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}