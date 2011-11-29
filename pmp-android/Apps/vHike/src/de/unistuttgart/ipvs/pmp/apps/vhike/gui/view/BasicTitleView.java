package de.unistuttgart.ipvs.pmp.apps.vhike.gui.view;

import de.unistuttgart.ipvs.pmp.R;
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
     * @see LinearLayout#LinearLayout(Context)
     */
    public BasicTitleView(Context context) {
        super(context);
        
        this.context = context;
        
        title = "";
        icon = R.drawable.logo;
    }
    
    /**
     * @see LinearLayout#LinearLayout(Context, AttributeSet)
     */
    public BasicTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        this.context = context;
        
        /* Load the styles from the xml assigned values */
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BasicTitleView);
        title = a.getString(R.styleable.BasicTitleView_name);
        icon = a.getResourceId(R.styleable.BasicTitleView_icon, R.drawable.logo);
    }
    
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        if (!isInEditMode()) {
            createLayout();
            refresh();
        } else {
            /* In edit mode, just load a very basic representation of the real contents. */
            setOrientation(LinearLayout.VERTICAL);
            
            TextView tv = new TextView(context);
            tv.setText("[EditViewMode] BasicTitleView");
            tv.setPadding(5, 10, 5, 10);
            addView(tv);
            
            View view = new View(context);
            view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 2));
            view.setBackgroundColor(Color.parseColor("#ff8c00"));
            addView(view);
        }
    }
    
    protected void createLayout() {
        /* load the xml-layout. */
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
     * Assign a new icon to the title. This method call overrides
     * the values set by {@link BasicTitleView#setIcon(int)}.
     * 
     * @param icon
     *            new icon as {@link Bitmap} to be set
     */
    public void setIcon(Drawable icon) {
        this.iconDrawable = icon;
        refresh();
    }
    
    
    /**
     * Refreshes the icon and title after a change.
     */
    protected void refresh() {
        TextView tv = (TextView) findViewById(R.id.TextView_Title);
        if (tv != null) {
            tv.setText(title);
        }
        
        ImageView iv = (ImageView) findViewById(R.id.ImageView_Icon);
        if (iv != null) {
            if (iconDrawable == null) {
                iv.setImageResource(icon);
            } else {
                iv.setImageDrawable(iconDrawable);
            }
        }
    }
}
