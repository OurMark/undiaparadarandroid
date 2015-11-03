package itba.undiaparadar.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

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
		setCancelable(false);
		final ImageView imageView = new ImageView(context);
		imageView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
		imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		imageView.setImageDrawable(drawable);
		setContentView(imageView);
		setCanceledOnTouchOutside(false);
	}

}
