package itba.undiaparadar.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.model.PositiveAction;

public class PledgeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
	private static final String POSITIVE_ACTION = "POSITIVE_ACTION";
	private PositiveAction positiveAction;
	private TextView dateButton;
	private TextView scheduleButton;

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
		positiveAction = (PositiveAction) getIntent().getSerializableExtra(POSITIVE_ACTION);
		getSupportActionBar().setTitle(R.string.pledge_action);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		dateButton = (TextView) findViewById(R.id.date_button);
		scheduleButton = (TextView) findViewById(R.id.schedule_button);
		final ImageButton keepSearchingButton = (ImageButton) findViewById(R.id.keep_searching_button);
		final ImageButton pledgeButton = (ImageButton) findViewById(R.id.pledge_button);

		keepSearchingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		pledgeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO: connect with parse
			}
		});

		final Calendar calendar = Calendar.getInstance();

		dateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				DatePickerDialog datePickerDialog = new DatePickerDialog(PledgeActivity.this,
						PledgeActivity.this, calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.show();
			}
		});

		scheduleButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				TimePickerDialog timePickerDialog = new TimePickerDialog(PledgeActivity.this,
						PledgeActivity.this, calendar.get(Calendar.HOUR_OF_DAY),
						calendar.get(Calendar.MINUTE),
						DateFormat.is24HourFormat(PledgeActivity.this));
				timePickerDialog.show();
			}
		});
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		dateButton.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		boolean is24Hour = DateFormat.is24HourFormat(this);
		StringBuilder builder = new StringBuilder();
		builder.append(hourOfDay).append(":").append(minute);

		if (!is24Hour) {
			Calendar calendar = Calendar.getInstance();
			if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
				builder.append(" am");
			} else {
				builder.append(" pm");
			}
		}
		scheduleButton.setText(builder.toString());
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
