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

    Paint paint2;
    ImageView image;
    String fullname;
    TextView appName;
    TextView appName2;
    int ID;
    public boolean isDown = false;
    private int imageSource;

    public ImagedButton(Context context, String name, int ID, int ImageSource) {
	super(context);
	this.imageSource = ImageSource;
	this.setPadding(10, 10, 10, 10);
	this.ID = ID;
	this.fullname = name;
	this.setOrientation(LinearLayout.VERTICAL);
	this.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
	setWillNotDraw(false);
	this.setOnTouchListener(new OnTouchListener() {

	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == event.ACTION_DOWN) {
		    isDown = true;
		    v.invalidate();
		}
		if (event.getAction() == event.ACTION_UP
			|| event.getAction() == event.ACTION_CANCEL) {
		    isDown = false;
		    v.invalidate();
		}
		return false;
	    }

	});
	paint2 = new Paint();
	image = new ImageView(context);
	image.setLayoutParams(LayoutParamsCreator.createWCWC(1f));
	image.setPadding(0, 10, 0, 0);

	appName = new TextView(context);
	appName2 = new TextView(context);
	appName.setLayoutParams(LayoutParamsCreator.createWCWC(1f));
	appName2.setLayoutParams(LayoutParamsCreator.createWCWC(1f));

	appName.setGravity(Gravity.CENTER_HORIZONTAL);
	image.setImageResource(imageSource);
	appName.setText(name);

	this.addView(image);
	this.addView(appName);
	this.addView(appName2);
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
	Paint paint = new Paint();
	paint.setColor(Color.BLACK);
	paint.setStrokeWidth(1);
	paint.setStyle(Style.STROKE);
	RectF rect = new RectF();
	rect.set(0 + getPaddingLeft(), 0 + getPaddingTop(), getWidth()
		- getPaddingRight(), getHeight() - getPaddingBottom());
	canvas.drawRoundRect(rect, 10, 10, paint);
	if (isDown) {
	    paint2.setColor(Color.GREEN);
	} else {
	    paint2.setColor(Color.BLUE);
	}

	paint2.setAlpha(35);
	paint2.setStyle(Style.FILL);

	RectF rect2 = new RectF();
	rect2.set(0 + getPaddingLeft(), 0 + getPaddingTop(), getWidth()
		- getPaddingRight(), getHeight() - getPaddingBottom());
	canvas.drawRoundRect(rect2, 10, 10, paint2);
    }

    /**
     * Can be deleted. Overlay the icons with the background
     */
    // @Override
    // protected void dispatchDraw(Canvas canvas) {
    // super.dispatchDraw(canvas);
    // onDraw(canvas);
    // }
    
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
    public int getIndex() {
	return ID;
    }
}