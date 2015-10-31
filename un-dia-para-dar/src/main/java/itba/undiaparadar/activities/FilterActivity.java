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
import android.widget.Button;
import android.widget.FrameLayout;

import itba.undiaparadar.R;

/**
 * Created by mpurita on 10/31/15.
 */
public class FilterActivity extends Activity {
	private static final int CIRCULAR_REVEAL_TRANSITION = 500;
	private FrameLayout rootView;

	public static Intent getIntent(final Context context) {
		return new Intent(context, FilterActivity.class);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
		setContentView(R.layout.filter_activity);
		setFinishOnTouchOutside(false);
		rootView = (FrameLayout) findViewById(R.id.root_layout);
		final Button accept = (Button) findViewById(R.id.acept);
		accept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				exitReveal();
			}
		});
		ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
		if (viewTreeObserver.isAlive()) {
			viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						enterReveal();
					}
				}
			});
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
			if (isClosing) {
				anim.addListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {

					}

					@Override
					public void onAnimationEnd(Animator animation) {
						rootView.setVisibility(View.INVISIBLE);
						finish();
					}

					@Override
					public void onAnimationCancel(Animator animation) {

					}

					@Override
					public void onAnimationRepeat(Animator animation) {

					}
				});
			}

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
