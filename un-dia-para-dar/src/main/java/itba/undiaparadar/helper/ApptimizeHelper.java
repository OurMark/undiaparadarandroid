package itba.undiaparadar.helper;

import android.content.Context;

import com.apptimize.Apptimize;
import com.apptimize.ApptimizeVar;

public final class ApptimizeHelper {
	private static final String NOTIFICATION_TIME = "Notification Time";
	private static final String DEMO = "Demo";
	private static ApptimizeVar<Integer> notificationTime =
			ApptimizeVar.createInteger(NOTIFICATION_TIME, 5000);
	private static ApptimizeVar<Boolean> demo =
			ApptimizeVar.createBoolean(DEMO, false);

	public static void initialize(final Context context) {
		Apptimize.setup(context, "DEkm6aPsuZAPhvBVVhbbgd6kRVP4TJa");
	}

	public static boolean isForDemo() {
		return demo.value();
	}

	public static int getNotificationTime() {
		return notificationTime.value();
	}
}
