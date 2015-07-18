package itba.undiaparadar.module;

import android.content.Context;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import itba.undiaparadar.UnDiaParaDarApplication;

public class ContextModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(Context.class).toProvider(new Provider<Context>() {

			@Override
			public Context get() {
				return UnDiaParaDarApplication.getAppContext();
			}
		}).in(Singleton.class);
	}
}
