package de.unistuttgart.ipvs.pmp.apps.vhike.gui.view;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.view.BasicTitleView;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A BasicTitleView powered by delegate Jakob Jarosch
 * 
 * @author Jakob Jarosch
 * 
 */
public class BasicTitleView extends LinearLayout {

	/**
	 * Context referenced in some methods.
	 */
	protected Context context;

	/**
	 * The icon displayed on the left side of the title.
	 */
	private int icon;
	private Drawable iconDrawable = null;

	/**
	 * The title which should be displayed.
	 */
	private String title;

	/**
	 * The color of the border below the title.
	 */
	private int borderColor;

	/**
	 * The color of the title.
	 */
	private int textColor;

	/**
	 * @see LinearLayout#LinearLayout(Context)
	 */
	public BasicTitleView(Context context) {
		super(context);

		this.context = context;

		this.title = "";
		this.icon = R.drawable.logo;
	}

	/**
	 * @see LinearLayout#LinearLayout(Context, AttributeSet)
	 */
	public BasicTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;

		/* Load the styles from the xml assigned values */
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.BasicTitleView);
		this.title = a.getString(R.styleable.BasicTitleView_name);
		this.icon = a.getResourceId(R.styleable.BasicTitleView_icon,
				R.drawable.logo);

		this.borderColor = a.getColor(R.styleable.BasicTitleView_borderColor,
				Color.parseColor("#ff8c00"));
		this.textColor = a.getColor(R.styleable.BasicTitleView_textColor,
				Color.WHITE);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		if (!isInEditMode()) {
			createLayout();
			refresh();
		} else {
			/*
			 * In edit mode, just load a very basic representation of the real
			 * contents.
			 */
			setOrientation(LinearLayout.VERTICAL);

			TextView tv = new TextView(this.context);
			tv.setText("[EditViewMode] BasicTitleView");
			tv.setPadding(5, 10, 5, 10);
			addView(tv);

			View view = new View(this.context);
			view.setLayoutParams(new LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT, 2));
			view.setBackgroundColor(Color.parseColor("#ff8c00"));
			addView(view);
		}
	}

	protected void createLayout() {
		/* load the xml-layout. */
		LayoutInflater layoutInflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addView(layoutInflater.inflate(R.layout.view_basictitle, null));
	}

	/**
	 * Assign an new title to the view.
	 * 
	 * @param title
	 *            new title to be set
	 */
	public void setTitle(String title) {
		this.title = title;
		refresh();
	}

	/**
	 * Assign a new icon to the title.
	 * 
	 * @param icon
	 *            new icon to be set
	 */
	public void setIcon(int icon) {
		this.icon = icon;
		refresh();
	}

	/**
	 * Assign a new icon to the title. This method call overrides the values set
	 * by {@link BasicTitleView#setIcon(int)}.
	 * 
	 * @param icon
	 *            new icon as {@link Bitmap} to be set
	 */
	public void setIcon(Drawable icon) {
		this.iconDrawable = icon;
		refresh();
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		refresh();
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
		refresh();
	}

	/**
	 * Refreshes the icon and title after a change.
	 */
	protected void refresh() {
		TextView tv = (TextView) findViewById(R.id.TextView_Title);
		if (tv != null) {
			tv.setText(this.title);
			tv.setTextColor(this.textColor);
		}

		ImageView iv = (ImageView) findViewById(R.id.ImageView_Icon);
		if (iv != null) {
			if (this.iconDrawable == null) {
				iv.setImageResource(this.icon);
			} else {
				iv.setImageDrawable(this.iconDrawable);
			}
		}

		View border = (View) findViewById(R.id.View_Divider_Strong);
		if (border != null) {
			border.setBackgroundColor(this.borderColor);
		}
	}
}
