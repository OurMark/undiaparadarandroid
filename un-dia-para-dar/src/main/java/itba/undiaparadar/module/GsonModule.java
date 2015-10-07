package itba.undiaparadar.module;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;

import java.lang.reflect.Type;
import java.util.List;

import itba.undiaparadar.model.PositiveAction;
import itba.undiaparadar.serializer.PositiveActionSerializer;

public class GsonModule extends AbstractModule {

	@Override
	protected final void configure() {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		final Type postivieActionsType = new TypeToken<List<PositiveAction>>() { }.getType();
		gsonBuilder.registerTypeAdapter(postivieActionsType, new PositiveActionSerializer());
		bind(Gson.class).toInstance(gsonBuilder.create());
	}
}