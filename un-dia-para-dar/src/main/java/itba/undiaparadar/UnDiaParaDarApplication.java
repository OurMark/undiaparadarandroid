package itba.undiaparadar;

import android.app.Application;
import android.content.Context;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.ArrayList;
import java.util.List;

import itba.undiaparadar.module.ContextModule;
import itba.undiaparadar.module.GsonModule;
import itba.undiaparadar.module.UnDiaParaDarModule;

public class UnDiaParaDarApplication extends Application {
	protected static Injector injector;
	private static UnDiaParaDarApplication mInstance;

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
	}

	public static void injectMembers(final Object object) {
		injector.injectMembers(object);
	}

	public static Context getAppContext() {
		return mInstance;
	}
}
