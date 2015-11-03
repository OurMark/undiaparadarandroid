package itba.undiaparadar.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import itba.undiaparadar.R;

/**
 * View that adds a rounded corner rectangle mask on top of a given view.
 */
@SuppressFBWarnings(value = "MISSING_TO_STRING_OVERRIDE", justification = "Excluded to avoid StackOverflow")
public class RoundedRectMaskView extends FrameLayout {

	private final int cornerRadius;

	/**
	 * Creates a {@code RoundedRectMaskView}
	 *
	 * @param context the context
	 * @param attrs   the attribute set. It must contain the {@code cornerRadius} and {@code layout} attributes.
	 * @see {@link android.view.View#View(Context, AttributeSet)}
	 */
	public RoundedRectMaskView(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * Creates a {@code RoundedRectMaskView}
	 *
	 * @param context      the context
	 * @param attrs        the attribute set. It must contain the {@code cornerRadius} and {@code layout} attributes.
	 * @param defStyleAttr the defStyleAttr
	 * @see {@link android.view.View#View(Context, AttributeSet, int)}
	 */
	public RoundedRectMaskView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		final TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.RoundedRectMaskView);
		final int layoutId = styledAttrs.getResourceId(R.styleable.RoundedRectMaskView_modalLayout, -1);
		final int cornerRadius = styledAttrs.getDimensionPixelSize(R.styleable.RoundedRectMaskView_cornerRadius, -1);
		styledAttrs.recycle();

		if (layoutId != -1 && cornerRadius != -1) {
			LayoutInflater.from(context).inflate(layoutId, this);
			this.cornerRadius = cornerRadius;
			return;
		}

		if (layoutId == -1 && cornerRadius == -1) {
			throw new IllegalStateException("Attributes layout and cornerRadius must be defined in ModalView.");
		}

		if (cornerRadius == -1) {
			throw new IllegalStateException("Attribute cornerRadius is not defined in ModalView.");
		}

		throw new IllegalStateException("Attribute layout is not defined in ModalView.");

	}

	@Override
	protected void dispatchDraw(final Canvas canvas) {
		final Path path = new Path();
		path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), cornerRadius, cornerRadius, Path.Direction.CW);
		canvas.clipPath(path);
		super.dispatchDraw(canvas);
	}
}