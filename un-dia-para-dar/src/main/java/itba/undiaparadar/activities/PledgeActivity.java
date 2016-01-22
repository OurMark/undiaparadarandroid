package itba.undiaparadar.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.model.PositiveAction;

public class PledgeActivity extends AppCompatActivity {
	private static final String POSITIVE_ACTION = "POSITIVE_ACTION";

	public static Intent getIntent(final Context context, PositiveAction positiveAction) {
		final Intent intent = new Intent(context, PledgeActivity.class);
		intent.putExtra(POSITIVE_ACTION, positiveAction);
		return intent;
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UnDiaParaDarApplication.injectMembers(this);
		setContentView(R.layout.pledge_actiivty);
	}
}
