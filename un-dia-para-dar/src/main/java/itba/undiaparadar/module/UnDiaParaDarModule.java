package itba.undiaparadar.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.monits.volleyrequests.network.NullSafeImageLoader;
import com.monits.volleyrequests.restsupport.Rest;

import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.cache.BitmapLruCache;
import itba.undiaparadar.services.SettingsService;
import itba.undiaparadar.services.SettingsServiceImpl;
import itba.undiaparadar.services.TopicService;
import itba.undiaparadar.services.TopicServiceImpl;
import itba.undiaparadar.services.UserService;
import itba.undiaparadar.services.UserServiceImpl;

public class UnDiaParaDarModule extends AbstractModule {

	@Override
	protected final void configure() {
		//Network
		bind(RequestQueue.class).toProvider(RequestQueueProvider.class).in(Singleton.class);
		bind(ImageLoader.class).toProvider(ImageLoaderProvider.class).in(Singleton.class);
		bind(BitmapLruCache.class).in(Singleton.class);

		//Services

		bind(TopicService.class).toProvider(TopicServiceProvider.class).in(Singleton.class);
		bind(SettingsService.class).to(SettingsServiceImpl.class).in(Singleton.class);
		bind(UserService.class).to(UserServiceImpl.class).in(Singleton.class);

		bind(SharedPreferences.class).toProvider(new Provider<SharedPreferences>() {

			@Override
			public SharedPreferences get() {
				return UnDiaParaDarApplication.getSharedPreferences();
			}
		}).in(Singleton.class);
	}

	private static class RequestQueueProvider implements Provider<RequestQueue> {

		private final Context context;

		@Inject
		public RequestQueueProvider(final Context context) {
			this.context = context;
		}

		@Override
		public RequestQueue get() {
			return Volley.newRequestQueue(context);
		}
	}

	private static class TopicServiceProvider implements Provider<TopicService> {
		private final RequestQueue requestQueue;

		@Inject
		public TopicServiceProvider(final Gson gson, final RequestQueue requestQueue) {
			this.requestQueue = requestQueue;
			Rest.setGson(gson);
		}

		@Override
		public TopicService get() {
			return new TopicServiceImpl(requestQueue);
		}
	}

	private static class ImageLoaderProvider implements Provider<ImageLoader> {

		private final RequestQueue requestQueue;
		private final BitmapLruCache bitmapCache;

		@Inject
		public ImageLoaderProvider(final RequestQueue requestQueue, final BitmapLruCache bitmapCache) {
			this.requestQueue = requestQueue;
			this.bitmapCache = bitmapCache;
		}

		@Override
		public ImageLoader get() {
			return new NullSafeImageLoader(requestQueue, bitmapCache);
		}
	}
}
