package itba.undiaparadar.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Movie;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.RawRes;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Create a Drawable from a Gif.
 * The activity which uses this kind of drawable must set hardwareAccelerated to false.
 */
@SuppressFBWarnings(value = "MISSING_TO_STRING_OVERRIDE",
		justification = "We don't want implement toString() in a Drawable")
public class GifDrawable extends Drawable {

	private static final int LOOP_AD_ETERNUM = -1;

	private static final int BYTES_PER_COLOR = 3;
	private static final int SIZE_OF_COLOR_TABLE_MASK = 0x7;
	private static final int COLOR_TABLE_PRESENT_MASK = 0x80;
	private static final int SINGLE_BYTE_MASK = 0xFF;

	private static final int BITS_PER_BYTE = 8;
	private static final int IMAGE_DESCRIPTOR_SIZE_BYTES = 8;
	private static final int HEADER_SIZE = 3;
	private static final int VERSIONS_SIZE = 3;
	private static final int HEADER_IMAGE_SISZE = 4;
	private static final int UNSIGNED_BYTE_MAX_VALUE = 255;
	private static final String US_ASCII_CHARSET = "us-ascii";

	private static final byte ID_TEXT_EXTENSION = 0x01;
	private static final byte ID_COMMENT_EXTENSION = (byte) 0xfe;
	private static final byte ID_GRAPHICS_CONTROL_EXTENSION = (byte) 0xf9;
	private static final byte ID_APPLICATION_EXTENSION = (byte) 0xff;

	private static final byte BLOCK_EXTENSION = 0x21;
	private static final byte BLOCK_IMAGE_DESCRIPTOR = 0x2c;
	private static final byte BLOCK_TRAILER = 0x3b;

	private Movie movie;
	private final Context context;
	private int movieStart;
	private int loopCount = 1;

	/**
	 * Creates a drawable with a gif.
	 *
	 * @param gifLayout the raw containing the gif.
	 * @param context   the context.
	 */
	public GifDrawable(@RawRes final int gifLayout, final Context context) {
		this.context = context;
		setGif(gifLayout);
	}

	/**
	 * Set the gif for this drawable.
	 *
	 * @param gifLayout the raw containing the gif.
	 */
	public final void setGif(@RawRes final int gifLayout) {

		movie = Movie.decodeStream(context.getResources().openRawResource(gifLayout));
		new AsyncTask<Void, Void, Integer>() {

			@Override
			protected Integer doInBackground(final Void... params) {
				try {
					return getLoopCount(gifLayout);
				} catch (final IOException e) {
					Log.d(GifDrawable.class.getName(),
							"Setting an invalid gif resource for GifDrawable", e);
					return null;
				}
			}

			@Override
			protected void onPostExecute(final Integer result) {
				if (result != null) {
					loopCount = result;
				}
			}
		}.execute();
		movieStart = 0;
	}

	@SuppressFBWarnings(value = {"SR_NOT_CHECKED", "RR_NOT_CHECKED"},
			justification = "We trust the GIF is well formatted")
	@SuppressWarnings({"PMD.CheckSkipResult", "checkstyle:cyclomaticcomplexity",
			"checkstyle:methodlength"})
	private final int getLoopCount(@RawRes final int gifLayout) throws IOException {
		final InputStream is = context.getResources().openRawResource(gifLayout);
		final BufferedInputStream bis = new BufferedInputStream(is);

		final byte[] buffer = new byte[UNSIGNED_BYTE_MAX_VALUE];

		bis.read(buffer, 0, HEADER_SIZE + VERSIONS_SIZE);

		final String rawResource = new String(buffer, 0, HEADER_SIZE, US_ASCII_CHARSET);
		if (!"GIF".equals(rawResource)) {
			throw new IllegalArgumentException("Provided raw resource identifier is not a GIF. "
					+ "You provided " + rawResource);
		}

		final String gifVersion = new String(buffer, HEADER_SIZE, VERSIONS_SIZE, US_ASCII_CHARSET);
		if (!"89a".equals(gifVersion)) {
			throw new IllegalArgumentException("Illegal GIF version. Only 89a is "
					+ "currently supported. You provided " + gifVersion);
		}

		bis.skip(HEADER_IMAGE_SISZE); // Skip image size
		bis.read(buffer, 0, 1); // get packed field from header
		bis.skip(2); // Skip rest of header

		// Skip global color table if present
		skipColorTable(bis, buffer[0]);

		// Now come the data blocks!
		while (bis.read(buffer, 0, 1) != -1) { // read the bock identifier
			switch (buffer[0]) {
				case BLOCK_EXTENSION:
					// This is an extension block... may be interesting
					bis.read(buffer, 0, 1);
					switch (buffer[0]) {
						case ID_TEXT_EXTENSION: // Text extension, skip it!
						case ID_COMMENT_EXTENSION: // Comment extension, skip it!
						case ID_GRAPHICS_CONTROL_EXTENSION: // Graphics control extension, skip it!
							skipDataSubBlocks(bis, buffer);
							break;

						case ID_APPLICATION_EXTENSION: // This may be the Application extension we are looking for!
							bis.read(buffer, 0, 1); // Get block size (tipically 8 bytes)
							final int count = buffer[0];
							bis.read(buffer, 0, count); // Read block

							final String version = new String(buffer, 0, count, US_ASCII_CHARSET);
							if ("NETSCAPE2.0".equals(version) || "ANIMEXTS1.0".equals(version)) {
								bis.read(buffer, 0, 1); // Get block size
								final int blockSize = buffer[0];

								bis.read(buffer, 0, 1); // get the first byte and check if it's a looping definition
								if (buffer[0] == 0x01) {
									bis.read(buffer, 0, 2);

									// We've got it, we are good to go!
									final int loops = buffer[0] + buffer[1] << BITS_PER_BYTE;
									return loops == 0 ? LOOP_AD_ETERNUM : loops; // beware, 0 means forever
								} else {
									// not a looping extension, skip it
									bis.skip(blockSize - 1);
								}
							} else {
								skipDataSubBlocks(bis, buffer); // Not interesting, skip it
							}
							break;
						default:
							throw new IOException("We screwed up, and found an extension with identifier " + buffer[0]);
					}
					break;

				case BLOCK_IMAGE_DESCRIPTOR:
					// This is an image descriptor block, ust skip it
					bis.skip(IMAGE_DESCRIPTOR_SIZE_BYTES);
					bis.read(buffer, 0, 1);

					// Skip local color table if present
					skipColorTable(bis, buffer[0]);

					bis.skip(1); // Skip LZW block count
					// Skip image data blocks
					skipDataSubBlocks(bis, buffer);
					break;

				case BLOCK_TRAILER:
					// We hit the end of file, default to playing it once
					return 1;

				default:
					throw new IOException("We screwed up, and found a block with identifier " + buffer[0]);
			}
		}

		// We should never be here if we did things right, but it's a sane default
		return 1;
	}

	@SuppressFBWarnings(value = {"SR_NOT_CHECKED", "RR_NOT_CHECKED"},
			justification = "We trust the GIF is well formatted")
	@SuppressWarnings("PMD.CheckSkipResult")
	private final void skipDataSubBlocks(final BufferedInputStream bis, final byte[] buffer) throws IOException {
		do {
			bis.read(buffer, 0, 1);    // Get sub-block size..
			bis.skip(buffer[0] & SINGLE_BYTE_MASK); // .. and skip it
		} while (buffer[0] != 0);
	}

	@SuppressFBWarnings(value = {"SR_NOT_CHECKED", "RR_NOT_CHECKED"},
			justification = "We trust the GIF is well formatted")
	@SuppressWarnings("PMD.CheckSkipResult")
	private final void skipColorTable(final InputStream bis, final byte packedByte) throws IOException {
		// is a color table present?
		if ((packedByte & COLOR_TABLE_PRESENT_MASK) != 0) {
			final int numberOfColors = 2 << (packedByte & SIZE_OF_COLOR_TABLE_MASK);
			bis.skip(BYTES_PER_COLOR * numberOfColors); // Skip full color table
		}
	}

	@Override
	public int getIntrinsicWidth() {
		return movie.width();
	}

	@Override
	public int getIntrinsicHeight() {
		return movie.height();
	}

	@Override
	public void draw(final Canvas canvas) {
		final int now = (int) SystemClock.uptimeMillis();
		if (movieStart == 0) {
			movieStart = now;
		}

		if (loopCount != LOOP_AD_ETERNUM && (now - movieStart) / (double) movie.duration() > loopCount) {
			// stay at the last frame
			movie.setTime(movie.duration());
		} else {
			// render the appropriate frame
			movie.setTime((now - movieStart) % movie.duration());

			// and make sure we render again next frame
			invalidateSelf();
		}

		movie.draw(canvas, 0, 0);
	}

	@Override
	public void setAlpha(final int i) {
		// Is not needed to implement this.
	}

	@Override
	public void setColorFilter(final ColorFilter colorFilter) {
		// Is not needed to implement this.
	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}
}