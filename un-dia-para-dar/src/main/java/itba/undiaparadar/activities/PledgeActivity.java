package itba.undiaparadar.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.model.Topic;

/**
 * Created by mpurita on 10/31/15.
 */
public class PledgeActivity extends CircularRevealActivity {
	private static final String TOPIC = "Topic";

	public static Intent getIntent(final Context context, Topic topic) {
		final Intent intent = new Intent(context, PledgeActivity.class);
		intent.putExtra(TOPIC, topic);
		return intent;
	}



	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UnDiaParaDarApplication.injectMembers(this);
	}

	@Override
	int getContentLayoutId() {
		return 0;
	}
}
