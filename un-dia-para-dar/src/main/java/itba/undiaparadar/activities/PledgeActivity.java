package itba.undiaparadar.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.inject.Inject;
import com.parse.SaveCallback;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.model.Pledge;
import itba.undiaparadar.model.PositiveAction;
import itba.undiaparadar.services.PledgeService;
import itba.undiaparadar.services.UserService;
import itba.undiaparadar.utils.GifDrawable;
import itba.undiaparadar.utils.UnDiaParaDarDialog;

public class PledgeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
		TimePickerDialog.OnTimeSetListener, SaveCallback {
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat time12Formatter = new SimpleDateFormat("hh:mm a");
	private static final SimpleDateFormat time24Formatter = new SimpleDateFormat("HH:mm");
	private static final String POSITIVE_ACTION = "POSITIVE_ACTION";
	@Inject
	private PledgeService pledgeService;
	@Inject
	private UserService userService;
	private PositiveAction positiveAction;
	private TextView dateButton;
	private TextView scheduleButton;
	private CoordinatorLayout coordinatorLayout;
	private Pledge pledge;
	private Dialog dialog;


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

		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
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
				final String dateString = dateButton.getText().toString();
				final String timeString = scheduleButton.getText().toString();
				if (TextUtils.isEmpty(dateString) || TextUtils.isEmpty(timeString)) {
					final Snackbar snackbar = Snackbar
							.make(coordinatorLayout, "Por favor complete la fecha y el horario", Snackbar.LENGTH_LONG);
					final View sbView = snackbar.getView();
					final TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
					textView.setTextColor(Color.YELLOW);
					snackbar.show();
				} else {
					try {
						final Date date = dateFormatter.parse(dateString);
						boolean is24Hour = DateFormat.is24HourFormat(PledgeActivity.this);
						final Date dateTime;
						time12Formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
						time24Formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
						if (is24Hour) {
							dateTime = time24Formatter.parse(timeString);
						} else {
							dateTime = time12Formatter.parse(timeString);
						}
						date.setHours(dateTime.getHours());
						date.setMinutes(dateTime.getMinutes());
						pledge = new Pledge();
						pledge.setCode(RandomStringUtils.random(6, true, true).toUpperCase());
						pledge.setUserId(userService.getUser().getUserId());
						pledge.setPositiveActionId(positiveAction.getId());
						pledge.setDone(false);
						pledge.setTargetDate(date);
						initDialog();
						pledgeService.savePledge(pledge, PledgeActivity.this);
						dialog.show();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
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

	private void initDialog() {
		final Drawable imageDrawable = new GifDrawable(R.raw.logo_loading, PledgeActivity.this);
		dialog = new UnDiaParaDarDialog(PledgeActivity.this, imageDrawable);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		final GregorianCalendar gregorianCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
		dateButton.setText(dateFormatter.format(gregorianCalendar.getTime()));
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		boolean is24Hour = DateFormat.is24HourFormat(this);
		StringBuilder builder = new StringBuilder();
		builder.append(hourOfDay).append(":");
		if (minute < 10) {
			builder.append("0");
		}
		builder.append(minute);

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

	@Override
	public void done(final com.parse.ParseException e) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		Log.e("Save parse object", "Error", e);
		final String snackbarText;
		final int duration;
		if (e == null) {
			snackbarText = "Compromiso guardado";
			duration = Snackbar.LENGTH_SHORT;
		} else {
			snackbarText = "Hubo un problema reintentar luego";
			duration = Snackbar.LENGTH_INDEFINITE;
		}
		final Snackbar snackbar = Snackbar
				.make(coordinatorLayout, snackbarText, duration);
		if (e != null) {
			snackbar.setAction("Reintentar", new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (pledge != null) {
						initDialog();
						pledgeService.savePledge(pledge, PledgeActivity.this);
					} else {
						snackbar.dismiss();
					}
				}
			});
			snackbar.setActionTextColor(Color.RED);
		}
		snackbar.show();
		snackbar.setCallback(new Snackbar.Callback() {
			@Override
			public void onDismissed(Snackbar snackbar, int event) {
				super.onDismissed(snackbar, event);
				if (e == null) {
					finish();
				}
			}
		});
	}
}
