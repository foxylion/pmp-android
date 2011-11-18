package de.unistuttgart.ipvs.pmp.gui2.views;

import de.unistuttgart.ipvs.pmp.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Basic Title for layouts. Very similar to the Android 4.0 titles (compare to settings-menu).
 * 
 * @author Jakob Jarosch
 */
public class BasicTitleView extends LinearLayout {
    
    /**
     * Context referenced in some methods.
     */
    private Context context;
    
    /**
     * The icon displayed on the left side of the title.
     */
    private int icon;
    private Bitmap iconBitmap = null;
    
    /**
     * The title which should be displayed.
     */
    private String name;
    
    
    /**
     * @see LinearLayout#LinearLayout(Context, AttributeSet)
     */
    public BasicTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        this.context = context;
        
        /* Load the styles from the xml assigned values */
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BasicTitleView);
        name = a.getString(R.styleable.BasicTitleView_name);
        icon = a.getResourceId(R.styleable.BasicTitleView_icon, R.drawable.app);
    }
    
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        if (!isInEditMode()) {
            /* Not in edit mode, load the xml-layout. */
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            addView(layoutInflater.inflate(R.layout.view_basictitle, null));
            
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
    
    /**
     * Assign an new name to the title.
     * 
     * @param name
     *            new name to be set
     */
    public void setName(String name) {
        this.name = name;
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
    }
    
    
    /**
     * Assign a new icon to the title. This method call overrides
     * the values set by {@link BasicTitleView#setIcon(int)}.
     * 
     * @param icon
     *            new icon as {@link Bitmap} to be set
     */
    public void setIcon(Bitmap icon) {
        this.iconBitmap = icon;
    }
    
    /**
     * Refreshes the icon and name after a change.
     */
    private void refresh() {
        TextView tv = (TextView) findViewById(R.id.textView1);
        if (tv != null) {
            tv.setText(name);
        }
        
        ImageView iv = (ImageView) findViewById(R.id.imageView1);
        if (iv != null) {
            if (iconBitmap == null) {
                iv.setImageResource(icon);
            } else {
                iv.setImageBitmap(iconBitmap);
            }
        }
    }
}
