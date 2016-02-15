package itba.undiaparadar.activities;

import android.app.Dialog;
import android.app.NotificationManager;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.inject.Inject;

import java.util.List;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.model.PositiveAction;
import itba.undiaparadar.services.TopicService;
import itba.undiaparadar.utils.CircularImageView;
import itba.undiaparadar.utils.GifDrawable;
import itba.undiaparadar.utils.UnDiaParaDarDialog;

/**
 * Created by mpurita on 11/6/15.
 */
public class PositiveActionDetail extends AppCompatActivity {
	private static final int NO_DRAWABLE = -1;
	private static final String POSITIVE_ACTION = "POSITIVE_ACTION";
	private static final String POSITIVE_ACTION_ID = "POSITIVE_ACTION_ID";
	private static final String TOPIC_IMG_RES = "TOPIC_IMG_RES";
	private PositiveAction positiveAction;
	private CallbackManager callbackManager;
	private int topicImgRes;
	private static final String NOTIFICATION_ID = "NOTIFICATION_ID";
	private static final String OBJECT_ID = "OBJECT_ID";
	@Inject
	private TopicService topicService;

	public static Intent getIntent(final Context context, @DrawableRes int topicImgRes,
		final PositiveAction positiveAction) {
		final Intent intent = new Intent(context, PositiveActionDetail.class);
		intent.putExtra(POSITIVE_ACTION, positiveAction);
		intent.putExtra(TOPIC_IMG_RES, topicImgRes);
		return intent;
	}

	public static Intent getIntent(final Context context, @DrawableRes int topicImgRes,
		final long positiveActionId, final int notificationId, String objectId) {
		final Intent intent = new Intent(context, PositiveActionDetail.class);
		intent.putExtra(POSITIVE_ACTION_ID, positiveActionId);
		intent.putExtra(TOPIC_IMG_RES, topicImgRes);
		intent.putExtra(NOTIFICATION_ID, notificationId);
		intent.putExtra(OBJECT_ID, objectId);
		return intent;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UnDiaParaDarApplication.injectMembers(this);
		setContentView(R.layout.positive_action_activity);
		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
		positiveAction = (PositiveAction) getIntent().getSerializableExtra(POSITIVE_ACTION);
		topicImgRes = getIntent().getIntExtra(TOPIC_IMG_RES, NO_DRAWABLE);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		int notificationId = getIntent().getIntExtra(NOTIFICATION_ID, -1);
		if (notificationId != -1) {
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(notificationId);
		}
		if (positiveAction == null) {
			long positiveActionId = getIntent().getLongExtra(POSITIVE_ACTION_ID, 0);
			final String objectId = getIntent().getStringExtra(OBJECT_ID);
			final Drawable imageDrawable = new GifDrawable(R.raw.logo_loading, this);
			final Dialog dialog = new UnDiaParaDarDialog(this, imageDrawable);
			dialog.show();
			topicService.getPositiveActionById(positiveActionId, new Response.Listener<List<PositiveAction>>() {
				@Override
				public void onResponse(List<PositiveAction> positiveActions) {
					if (!positiveActions.isEmpty()) {
						positiveAction = positiveActions.get(0);
						dialog.dismiss();
						setUpView();
						setUpShareButton();
						setUpPledgeButton(false, objectId);
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError volleyError) {
					dialog.dismiss();
				}
			});

		} else {
			setUpView();
			setUpShareButton();
			setUpPledgeButton(true, null);
		}

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

		final TextView positiveActionOrganizationName = (TextView) findViewById(R.id.positive_action_organization_name);
		positiveActionOrganizationName.setText(positiveAction.getOrganizationName());

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
		final StringBuilder locationBuilder = new StringBuilder();
		if (positiveAction.getCity() != null && !positiveAction.getCity().isEmpty()) {
			locationBuilder.append(positiveAction.getCity());
			if (positiveAction.getCountry() != null && !positiveAction.getCountry().isEmpty()) {
				locationBuilder.append(", ").append(positiveAction.getCountry());
			}
		} else {
			if (positiveAction.getCountry() != null && !positiveAction.getCountry().isEmpty()) {
				locationBuilder.append(positiveAction.getCountry());
			}
		}
		if (locationBuilder.toString().isEmpty()) {
			positiveActionLocation.setVisibility(View.GONE);
		} else {
			positiveActionLocation.setVisibility(View.VISIBLE);
			positiveActionLocation.setText(locationBuilder.toString());
		}
		final TextView positiveActionWebUrl = (TextView) findViewById(R.id.web_url);
		if (positiveAction.getExternalUrl() != null && !positiveAction.getExternalUrl().isEmpty()) {
			positiveActionWebUrl.setVisibility(View.VISIBLE);
			positiveActionWebUrl.setText(positiveAction.getExternalUrl());
			positiveActionWebUrl.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					String url = positiveAction.getExternalUrl();
					if (!url.startsWith("http://") && !url.startsWith("https://")) {
						url = "http://" + url;
					}
					final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(browserIntent);
				}
			});
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

	private void setUpPledgeButton(boolean flag, final String objectId) {
		final ImageButton pledgeButton = (ImageButton) findViewById(R.id.pledge);
		if (flag) {
			pledgeButton.setImageResource(R.drawable.pledge_toggle);
			pledgeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					pledge();
				}
			});
		} else {
			pledgeButton.setImageResource(R.drawable.pledge);
			pledgeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					verifyPledge(objectId);
				}
			});
		}
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
		startActivity(PledgeActivity.getIntent(this, positiveAction, topicImgRes));
	}

	private void verifyPledge(String objectId) {
		startActivity(PledgeVerificationActivity.getIntent(this, objectId));
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
