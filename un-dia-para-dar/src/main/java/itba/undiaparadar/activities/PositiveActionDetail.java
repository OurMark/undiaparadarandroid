package itba.undiaparadar.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import itba.undiaparadar.R;
import itba.undiaparadar.model.PositiveAction;
import itba.undiaparadar.utils.CircularImageView;
import itba.undiaparadar.utils.GifDrawable;
import itba.undiaparadar.utils.UnDiaParaDarDialog;

/**
 * Created by mpurita on 11/6/15.
 */
public class PositiveActionDetail extends AppCompatActivity {
	private static final int NO_DRAWABLE = -1;
	private static final String POSITIVE_ACTION = "POSITIVE_ACTION";
	private static final String TOPIC_IMG_RES = "TOPIC_IMG_RES";
	private PositiveAction positiveAction;
	private CallbackManager callbackManager;
	private int topicImgRes;

	public static Intent getIntent(final Context context, @DrawableRes int topicImgRes,
		final PositiveAction positiveAction) {
		final Intent intent = new Intent(context, PositiveActionDetail.class);
		intent.putExtra(POSITIVE_ACTION, positiveAction);
		intent.putExtra(TOPIC_IMG_RES, topicImgRes);
		return intent;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.positive_action_activity);
		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
		positiveAction = (PositiveAction) getIntent().getSerializableExtra(POSITIVE_ACTION);
		topicImgRes = getIntent().getIntExtra(TOPIC_IMG_RES, NO_DRAWABLE);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		setUpView();
		setUpShareButton();
		setUpPledgeButton();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitle(getString(R.string.detail));
	}

	private void setUpView() {
		final CircularImageView topicImg = (CircularImageView) findViewById(R.id.topic_img);
		if (topicImgRes != NO_DRAWABLE) {
			topicImg.setImageResource(topicImgRes);
		}
		final TextView positiveActionTitle = (TextView) findViewById(R.id.positive_action_title);
		positiveActionTitle.setText(positiveAction.getTitle());
		final TextView positiveActionSubtitle = (TextView) findViewById(R.id.positive_action_subtitle);
		if (positiveAction.getSubtitle() != null && !positiveAction.getSubtitle().isEmpty()) {
			positiveActionSubtitle.setVisibility(View.VISIBLE);
			positiveActionSubtitle.setText(positiveAction.getSubtitle());
		} else {
			positiveActionSubtitle.setVisibility(View.GONE);
		}
		final TextView positiveActionDescription = (TextView) findViewById(R.id.positive_action_description);
		positiveActionDescription.setText(positiveAction.getDescription());
		final TextView positiveActionLocation = (TextView) findViewById(R.id.location);
		final String location = positiveAction.getCity() + ", " + positiveAction.getCountry();
		positiveActionLocation.setText(location);
		final TextView positiveActionWebUrl = (TextView) findViewById(R.id.web_url);
		if (positiveAction.getExternalUrl() != null && !positiveAction.getExternalUrl().isEmpty()) {
			positiveActionWebUrl.setVisibility(View.VISIBLE);
			positiveActionWebUrl.setText(positiveAction.getExternalUrl());
		} else {
			positiveActionWebUrl.setVisibility(View.GONE);
		}
	}

	private void setUpShareButton() {
		final ImageButton shareButton = (ImageButton) findViewById(R.id.share);
		final boolean shareStatus = positiveAction.getExternalUrl() != null && !positiveAction.getExternalUrl().isEmpty();
		shareButton.setEnabled(shareStatus);
		shareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (positiveAction.getExternalUrl() != null && !positiveAction.getExternalUrl().isEmpty()) {
					final Drawable imageDrawable = new GifDrawable(R.raw.logo_loading, PositiveActionDetail.this);
					final Dialog dialog = new UnDiaParaDarDialog(PositiveActionDetail.this, imageDrawable);
					dialog.show();
					share(dialog);
				} else {
					Toast.makeText(PositiveActionDetail.this, "No puedes compartir esta acción", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}

	private void setUpPledgeButton() {
		final ImageButton pledgeButton = (ImageButton) findViewById(R.id.pledge);
		pledgeButton.setEnabled(false); //TODO: Modificar cuando se implemente el pledge
		pledgeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				pledge();
			}
		});
	}

	private void share(final Dialog dialog) {
		final ShareDialog shareDialog = new ShareDialog(this);
		shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
			@Override
			public void onSuccess(final Sharer.Result result) {
				dialog.dismiss();
			}

			@Override
			public void onCancel() {
				dialog.dismiss();
			}

			@Override
			public void onError(final FacebookException error) {
				dialog.dismiss();
			}
		});
		if (ShareDialog.canShow(ShareLinkContent.class)) {
			ShareLinkContent linkContent = new ShareLinkContent.Builder()
					.setContentTitle(positiveAction.getTitle())
					.setContentDescription(
							positiveAction.getDescription())
					.setContentUrl(Uri.parse(positiveAction.getExternalUrl()))
					.build();
			shareDialog.show(linkContent);
		}

	}

	private void pledge() {
		//TODO: Implementar mas adelante
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
