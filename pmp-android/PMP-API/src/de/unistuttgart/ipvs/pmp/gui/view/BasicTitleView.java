package de.unistuttgart.ipvs.pmp.gui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.AttributeSetUtil;

/**
 * Basic Title for layouts. Very similar to the Android 4.0 titles (compare to settings-menu).
 * 
 * @author Jakob Jarosch
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
     * The back action gives the user a possibility to return
     */
    private boolean backActionAvailable;
    
    
    /**
     * @see LinearLayout#LinearLayout(Context)
     */
    public BasicTitleView(Context context) {
        super(context);
        
        this.context = context;
        
        this.title = "";
        this.icon = R.drawable.pmp_api_icon_undefined;
    }
    
    
    /**
     * @see LinearLayout#LinearLayout(Context, AttributeSet)
     */
    public BasicTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        Log.d("BasicTitleView was created with any AttributeSet");
        
        this.context = context;
        
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            System.out.println(attrs.getAttributeName(i) + " :: " + attrs.getAttributeNameResource(i) + " :: "
                    + attrs.getAttributeValue(i));
        }
        
        /* Load the styles from the xml assigned values */
        AttributeSetUtil asu = new AttributeSetUtil(getContext(), attrs);
        
        this.title = asu.getString(AttributeSetUtil.ViewBasicTitle_name);
        
        this.iconDrawable = asu.getDrawable(AttributeSetUtil.ViewBasicTitle_icon, R.drawable.pmp_api_icon_undefined);
        
        this.backActionAvailable = asu.getBoolean(AttributeSetUtil.ViewBasicTitle_backButton, false);
        
        this.borderColor = asu.getColor(AttributeSetUtil.ViewBasicTitle_borderColor, Color.parseColor("#ff8c00"));
        this.textColor = asu.getColor(AttributeSetUtil.ViewBasicTitle_textColor, Color.WHITE);
        
        System.out.println(this.title + " :: " + this.icon + " :: " + this.backActionAvailable + " :: "
                + this.borderColor + " :: " + this.textColor);
    }
    
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        if (!isInEditMode()) {
            createLayout();
            addListener();
            refresh();
        } else {
            /* In edit mode, just load a very basic representation of the real contents. */
            setOrientation(LinearLayout.VERTICAL);
            
            TextView tv = new TextView(this.context);
            tv.setText("[EditViewMode] BasicTitleView");
            tv.setPadding(5, 10, 5, 10);
            addView(tv);
            
            View view = new View(this.context);
            view.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, 2));
            view.setBackgroundColor(Color.parseColor("#ff8c00"));
            addView(view);
        }
    }
    
    
    protected void createLayout() {
        /* load the xml-layout. */
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(layoutInflater.inflate(R.layout.view_basictitle, null));
    }
    
    
    protected void addListener() {
        ImageView iv = (ImageView) findViewById(R.id.ImageView_Icon);
        iv.setOnClickListener(new OnClickListenerImpl());
        
        ImageView backAction = (ImageView) findViewById(R.id.ImageView_BackIcon);
        backAction.setOnClickListener(new OnClickListenerImpl());
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
    
    
    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        refresh();
    }
    
    
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        refresh();
    }
    
    
    public void setBackAction(boolean available) {
        this.backActionAvailable = available;
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
        
        View border = findViewById(R.id.View_Divider_Strong);
        if (border != null) {
            border.setBackgroundColor(this.borderColor);
        }
        
        ImageView backAction = (ImageView) findViewById(R.id.ImageView_BackIcon);
        if (this.backActionAvailable) {
            backAction.setVisibility(View.VISIBLE);
            iv.setClickable(true);
        } else {
            backAction.setVisibility(View.GONE);
            iv.setClickable(false);
        }
    }
}

class OnClickListenerImpl implements View.OnClickListener {
    
    @Override
    public void onClick(View v) {
        if (v.getContext() instanceof Activity) {
            ((Activity) v.getContext()).finish();
        }
    }
}
