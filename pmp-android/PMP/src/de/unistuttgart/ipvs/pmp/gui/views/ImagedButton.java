package de.unistuttgart.ipvs.pmp.gui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ImagedButton represent an App or Ressource in PMP-GUI
 * 
 * @author Alexander Wassiljew
 * 
 */
public class ImagedButton extends LinearLayout {
    /**
     * Painter for the View
     */
    Paint paint;
    Paint paint2;
    /**
     * Image for the View
     */
    ImageView image;
    /**
     * Fullname of the App
     */
    String fullname;
    /**
     * Appname lines
     */
    TextView appName;
    TextView appName2;
    /**
     * ID of the Button
     */
    String identifier;
    /**
     * State of the ImageButton
     */
    private boolean isDown = false;
    /**
     * Sourceimage for image
     */
    private int imageSource;

    /**
     * Constructor
     * 
     * @param context
     * @param name
     * @param ID
     * @param ImageSource
     */
    public ImagedButton(Context context, String name,String identifier, int ImageSource) {
	super(context);
	
	/*Setting the Padding of the ImageButton*/
	this.setPadding(10, 10, 10, 10);
	
	/*Initialize the fields*/
	this.imageSource = ImageSource;
	this.identifier = identifier;
	this.fullname = name;
	
	/*Setting up the ImagedButton*/
	this.setOrientation(LinearLayout.VERTICAL);
	this.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
	
	// False for using the custom onDraw method
	this.setWillNotDraw(false);
	
	/*TouchListener for the ImagedButton, which handles the 
	 * behaviour of the ImagedButton*/
	this.setOnTouchListener(new OnTouchListener() {

	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    isDown = true;
		    v.invalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_UP
			|| event.getAction() == MotionEvent.ACTION_CANCEL) {
		    isDown = false;
		    v.invalidate();
		}
		return false;
	    }

	});
	
	/*Creating the painter*/
	paint = new Paint();
	paint2 = new Paint();
	
	/*Setting up the Icon of the ImagedButton*/
	
	image = new ImageView(context);
	image.setPadding(0, 10, 0, 0);
	image.setLayoutParams(LayoutParamsCreator.createWCWC(1f));
	
	// set the SourceImage to the View
	image.setImageResource(imageSource);
	
	/*Setting up the AppName lines*/
	appName = new TextView(context);
	appName2 = new TextView(context);
	appName.setLayoutParams(LayoutParamsCreator.createWCWC(1f));
	appName2.setLayoutParams(LayoutParamsCreator.createWCWC(1f));

	appName.setGravity(Gravity.CENTER_HORIZONTAL);
	appName.setText(name);
	
	/*Sets the Appname max. to 10 Chars each line*/
	if (name.length() > 10) {
	    String string = name.substring(0, 10);
	    String string2 = name.substring(10);
	    if (string2.length() > 10)
		string2 = string2.substring(0, 10);

	    appName.setText(string);
	    appName2.setText(string2);
	}
	appName.setTextColor(Color.BLACK);
	appName2.setTextColor(Color.BLACK);
	
	/*Adding the Views*/
	this.addView(image);
	this.addView(appName);
	this.addView(appName2);
    }

    /**
     * Custom onDraw method
     */
    @Override
    protected void onDraw(Canvas canvas) {
	
	/*Setting up the painter*/
	paint.setColor(Color.BLACK);
	paint.setStrokeWidth(1);
	paint.setStyle(Style.STROKE);
	paint2.setStyle(Style.FILL);
	
	
	/*Create and draw the black border*/
	RectF rect = new RectF();
	rect.set(0 + getPaddingLeft(), 0 + getPaddingTop(), getWidth()
		- getPaddingRight(), getHeight() - getPaddingBottom());
	canvas.drawRoundRect(rect, 10, 10, paint);
	
	
	/*Create and draw the State of the ImagedButton
	 * isDown --> GREEN
	 * !isDown--> BLUE*/
	if (isDown) {
	    paint2.setColor(Color.GREEN);
	} else {
	    paint2.setColor(Color.BLUE);
	}
	paint2.setAlpha(35);
	RectF rect2 = new RectF();
	rect2.set(0 + getPaddingLeft(), 0 + getPaddingTop(), getWidth()
		- getPaddingRight(), getHeight() - getPaddingBottom());
	canvas.drawRoundRect(rect2, 10, 10, paint2);
    }

    /**
     * @return returns the fullname of the App
     */
    public String getName() {
	return fullname;
    }

    /**
     * 
     * @return return the ID of the ImagedButton
     */
    public String getIdentifier() {
	return identifier;
    }
}