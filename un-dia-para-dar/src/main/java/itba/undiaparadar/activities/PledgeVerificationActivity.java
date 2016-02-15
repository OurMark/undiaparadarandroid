package itba.undiaparadar.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.inject.Inject;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.model.Pledge;
import itba.undiaparadar.model.PledgeStatus;
import itba.undiaparadar.services.PledgeService;
import itba.undiaparadar.utils.GifDrawable;
import itba.undiaparadar.utils.UnDiaParaDarDialog;

public class PledgeVerificationActivity extends AppCompatActivity implements GetCallback<Pledge> {
	private static final String PLEDGE = "PLEDGE";
	@Inject
	private PledgeService pledgeService;
	private Pledge pledge;
	private Dialog dialog;

	public static Intent getIntent(final Context context, final String objectId) {
		final Intent intent = new Intent(context, PledgeVerificationActivity.class);
		intent.putExtra(PLEDGE, objectId);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pledge_verification);
		UnDiaParaDarApplication.injectMembers(this);
		getSupportActionBar().setTitle(getString(R.string.pledge_verification));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		final String pledgeId = getIntent().getStringExtra(PLEDGE);
		initDialog();
		dialog.show();
		pledgeService.retrievePledge(pledgeId, this);
		final Button sendButton = (Button) findViewById(R.id.btn_verify_otp);
		final EditText inputCode = (EditText) findViewById(R.id.input_code);
		final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final String code = inputCode.getText().toString().toUpperCase();
				if (pledge.getCode().equals(code)) {
					pledge.setDone(PledgeStatus.DONE.ordinal());
					initDialog();
					dialog.show();
					pledgeService.savePledge(pledge, new SaveCallback() {
						@Override
						public void done(final ParseException e) {
							if (dialog != null && dialog.isShowing()) {
								dialog.dismiss();
							}
							finish();
						}
					});
				} else {
					final Snackbar snackbar = Snackbar
							.make(coordinatorLayout, "Código invalido", Snackbar.LENGTH_SHORT);
					final View sbView = snackbar.getView();
					final TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
					textView.setTextColor(Color.YELLOW);
					snackbar.show();
				}
			}
		});
	}

	@NonNull
	private void initDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		final Drawable imageDrawable = new GifDrawable(R.raw.logo_loading, PledgeVerificationActivity.this);
		dialog = new UnDiaParaDarDialog(PledgeVerificationActivity.this, imageDrawable);
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

	@Override
	public void done(Pledge pledge, ParseException e) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		this.pledge = pledge;
	}
}
