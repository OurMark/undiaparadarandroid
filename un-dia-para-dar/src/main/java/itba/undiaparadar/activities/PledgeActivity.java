package itba.undiaparadar.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
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
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.helper.ApptimizeHelper;
import itba.undiaparadar.model.NotificationPublisher;
import itba.undiaparadar.model.Pledge;
import itba.undiaparadar.model.PledgeStatus;
import itba.undiaparadar.model.PositiveAction;
import itba.undiaparadar.services.PledgeService;
import itba.undiaparadar.services.SettingsService;
import itba.undiaparadar.services.UserService;
import itba.undiaparadar.utils.DateUtils;
import itba.undiaparadar.utils.GifDrawable;
import itba.undiaparadar.utils.UnDiaParaDarDialog;

public class PledgeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
		TimePickerDialog.OnTimeSetListener, SaveCallback {
	private static final int NO_DRAWABLE = -1;
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat time12Formatter = new SimpleDateFormat("hh:mm a");
	private static final SimpleDateFormat time24Formatter = new SimpleDateFormat("HH:mm");
	private final java.text.DateFormat date12Format = new SimpleDateFormat( "dd/MM/yyy hh:mm:ss aa");
	private final java.text.DateFormat date24Format = new SimpleDateFormat( "dd/MM/yyyy hh:mm:ss");
	private static final String POSITIVE_ACTION = "POSITIVE_ACTION";
	private static final String TOPIC_IMG_RES = "TOPIC_IMG_RES";
	private static final String POSITIVE_ACTION_ID = "POSITIVE_ACTION_ID";
	private static final String OBJECT_ID = "OBJECT_ID";
	@Inject
	private PledgeService pledgeService;
	@Inject
	private UserService userService;
	@Inject
	private SettingsService settingsService;
	private PositiveAction positiveAction;
	private TextView dateButton;
	private TextView scheduleButton;
	private CoordinatorLayout coordinatorLayout;
	private Pledge pledge;
	private Dialog dialog;
	private Date pledgeDate;
	private int topicImgRes;



	public static Intent getIntent(final Context context, PositiveAction positiveAction, @DrawableRes int topicImgRes) {
		final Intent intent = new Intent(context, PledgeActivity.class);
		intent.putExtra(POSITIVE_ACTION, positiveAction);
		intent.putExtra(TOPIC_IMG_RES, topicImgRes);
		return intent;
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UnDiaParaDarApplication.injectMembers(this);
		setContentView(R.layout.pledge_actiivty);
		positiveAction = (PositiveAction) getIntent().getSerializableExtra(POSITIVE_ACTION);
		topicImgRes = getIntent().getIntExtra(TOPIC_IMG_RES, NO_DRAWABLE);
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
						pledgeDate = dateFormatter.parse(dateString);
						boolean is24Hour = DateFormat.is24HourFormat(PledgeActivity.this);
						final Date dateTime;
						if (is24Hour) {
							dateTime = time24Formatter.parse(timeString);
						} else {
							dateTime = time12Formatter.parse(timeString);
						}
						pledgeDate.setHours(dateTime.getHours());
						pledgeDate.setMinutes(dateTime.getMinutes());
						pledge = new Pledge();
						pledge.setCode(RandomStringUtils.random(6, true, true).toUpperCase());
						if (userService.getUser().getUserId() == null) {
							ParseUser parseUser = ParseUser.getCurrentUser();
							userService.getUser().setUserId(parseUser.getObjectId());
						}
						pledge.setUserId(userService.getUser().getUserId());
						pledge.setPositiveActionId(positiveAction.getId());
						if (is24Hour) {
							pledge.setTargetDate(date24Format.format(pledgeDate));
						} else {
							pledge.setTargetDate(date12Format.format(pledgeDate));
						}
						pledge.setDone(PledgeStatus.NEUTRAL.ordinal());
						pledge.setPositiveActionTitle(positiveAction.getTitle());
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
					setupReminder();
					finish();
				}
			}
		});
	}

	private void setupReminder() {
		if (ApptimizeHelper.isForDemo()) {
			scheduleNotification(getNotification(), SystemClock.elapsedRealtime() + ApptimizeHelper.getNotificationTime());
		} else if (DateUtils.daysBetween(pledgeDate, new Date()) > 1) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(pledgeDate);
			calendar.set(Calendar.HOUR_OF_DAY, 8);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			scheduleNotification(getNotification(), calendar.getTimeInMillis());
		}
	}

	private void scheduleNotification(Notification notification, long delay) {

		Intent notificationIntent = new Intent(this, NotificationPublisher.class);
		notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, settingsService.getNotificationId());
		settingsService.incrementNotificationId();
		notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, delay, pendingIntent);
	}

	private Notification getNotification() {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setContentTitle("Tenes un compromiso");
		builder.setContentText(pledge.getPositiveActionTitle());
		Bundle bundle = new Bundle();
		bundle.putInt(TOPIC_IMG_RES, topicImgRes);
		bundle.putLong(POSITIVE_ACTION_ID, positiveAction.getId());
		bundle.putString(OBJECT_ID, pledge.getObjectId());
		builder.addExtras(bundle);
		builder.setSmallIcon(R.drawable.logo);
		return builder.build();
	}
}
