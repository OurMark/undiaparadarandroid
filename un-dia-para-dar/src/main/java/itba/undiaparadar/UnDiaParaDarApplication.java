package itba.undiaparadar;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.ArrayList;
import java.util.List;

import itba.undiaparadar.module.ContextModule;
import itba.undiaparadar.module.GsonModule;
import itba.undiaparadar.module.UnDiaParaDarModule;

public class UnDiaParaDarApplication extends MultiDexApplication {
	private static final String SHARED_PREFERENCES = "itba.undiaparadar.Preferences";
	protected static Injector injector;
	private static UnDiaParaDarApplication mInstance;
	private static SharedPreferences sharedPreferences;


	private void setUpInjector() {
		final List<Module> modules = new ArrayList<>();
		modules.add(new ContextModule());
		modules.add(new GsonModule());
		modules.add(new UnDiaParaDarModule());

		injector = Guice.createInjector(modules);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		setUpInjector();
		sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
	}

	public static void injectMembers(final Object object) {
		injector.injectMembers(object);
	}

	public static Context getAppContext() {
		return mInstance;
	}

	public static SharedPreferences getSharedPreferences() {
		return UnDiaParaDarApplication.sharedPreferences;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}
