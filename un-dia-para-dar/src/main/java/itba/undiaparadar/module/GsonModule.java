package itba.undiaparadar.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;

public class GsonModule extends AbstractModule {

	@Override
	protected final void configure() {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		bind(Gson.class).toInstance(gsonBuilder.create());
	}
}