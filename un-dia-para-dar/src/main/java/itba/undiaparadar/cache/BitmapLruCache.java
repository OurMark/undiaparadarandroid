package itba.undiaparadar.cache;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageCache {
	private static final int MAX_MEMORY_DIVISOR = 8;
	private static final int BYTES_PER_KB = 1024;

	public BitmapLruCache() {
		this(getDefaultLruCacheSize());
	}

	public BitmapLruCache(final int sizeInKiloBytes) {
		super(sizeInKiloBytes);
	}

	private static int getDefaultLruCacheSize() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / BYTES_PER_KB);
		return maxMemory / MAX_MEMORY_DIVISOR;
	}

	@Override
	protected int sizeOf(final String key, final Bitmap value) {
		return value.getRowBytes() * value.getHeight() / BYTES_PER_KB;
	}

	@Override
	public Bitmap getBitmap(final String url) {
		return get(url);
	}

	@Override
	public void putBitmap(final String url, final Bitmap bitmap) {
		put(url, bitmap);
	}
}