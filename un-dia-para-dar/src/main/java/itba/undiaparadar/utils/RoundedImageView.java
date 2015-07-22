package itba.undiaparadar.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

public class RoundedImageView extends NetworkImageView {

	public RoundedImageView(final Context context) {
		super(context);
	}

	public RoundedImageView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public RoundedImageView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(final Canvas canvas) {

		final Drawable drawable = getDrawable();

		if (drawable == null || getWidth() == 0 || getHeight() == 0) {
			return;
		}
		final Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		if (b == null) {
			return;
		}
		final Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

		int w = getWidth();

		final Bitmap roundBitmap = getCroppedBitmap(bitmap, w);
		canvas.drawBitmap(roundBitmap, 0, 0, null);

	}

	public static Bitmap getCroppedBitmap(final Bitmap bmp, final int radius) {
		final Bitmap sbmp;
		if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
			sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		} else {
			sbmp = bmp;
		}
		final Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
				sbmp.getHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f, sbmp.getHeight() / 2 + 0.7f,
				sbmp.getWidth() / 2 + 0.1f, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);


		return output;
	}

}