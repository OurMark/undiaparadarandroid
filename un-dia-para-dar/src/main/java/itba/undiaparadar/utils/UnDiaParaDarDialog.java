package itba.undiaparadar.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import itba.undiaparadar.R;

/**
 * Created by mpurita on 11/3/15.
 */
public class UnDiaParaDarDialog extends Dialog {

	public UnDiaParaDarDialog(final Context context, final Drawable drawable) {
		super(context);
		init(drawable, context);
	}

	public UnDiaParaDarDialog(final Context context, final int theme, final Drawable drawable) {
		super(context, theme);
		init(drawable, context);
	}

	protected UnDiaParaDarDialog(final Context context, final boolean cancelable,
		final OnCancelListener cancelListener, final Drawable drawable) {
		super(context, cancelable, cancelListener);
		init(drawable, context);
	}

	private void init(final Drawable drawable, final Context context) {
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setCancelable(false);
		setCanceledOnTouchOutside(false);
		final View view = getLayoutInflater().inflate(R.layout.dialog_gif_drawable, null, false);
		final ImageView imageView = (ImageView) view.findViewById(R.id.dialog_drawable);
		imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		imageView.setImageDrawable(drawable);
		setContentView(view);
	}

}
