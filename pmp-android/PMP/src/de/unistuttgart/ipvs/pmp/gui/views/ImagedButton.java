package de.unistuttgart.ipvs.pmp.gui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
/**
 * ImagedButton represent an App or Ressource
 * in PMP-GUI
 * @author Alexander Wassiljew
 *
 */
public class ImagedButton extends LinearLayout {
	
	Paint paint2;
	ImageView image;
	String fullname;
	TextView appName;
	TextView appName2;
	public boolean isDown = false;

	public ImagedButton(Context context, String name) {
		super(context);
		this.setPadding(10, 10, 10, 10);
		this.fullname = name;
		this.setOrientation(LinearLayout.VERTICAL);
		this.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		setWillNotDraw(false);
		this.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == event.ACTION_DOWN){
					isDown = true;
					v.invalidate();
				}
				if(event.getAction() == event.ACTION_UP || event.getAction() == event.ACTION_CANCEL){
					isDown = false;
					v.invalidate();
				}
				return false;
			}
			
		});
		paint2 = new Paint();
		image = new ImageView(context);
		image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 1f));

		appName = new TextView(context);
		appName2 = new TextView(context);
		appName.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 1f));
		appName2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 1f));

		appName.setGravity(Gravity.CENTER_HORIZONTAL);
		image.setImageResource(R.drawable.icon);
		appName.setText(name);

		this.addView(image);
		this.addView(appName);
		this.addView(appName2);
		if (name.length() > 10) {
			String string = name.substring(0, 10);
			String string2 = name.substring(10);
			appName.setText(string);
			appName2.setText(string2);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(1);
		paint.setStyle(Style.STROKE);
		canvas.drawRect(0 + getPaddingLeft(), 0 + getPaddingTop(), getWidth()
				- getPaddingRight(), getHeight() - getPaddingBottom(), paint);

		if(isDown){
			paint2.setColor(Color.GREEN);	
		}else{
			paint2.setColor(Color.BLUE);
		}
		
		paint2.setAlpha(35);
		paint2.setStyle(Style.FILL);
		canvas.drawRect(0 + getPaddingLeft(), 0 + getPaddingTop(), getWidth()
				- getPaddingRight(), getHeight() - getPaddingBottom(), paint2);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		onDraw(canvas);
	}
	
	public String getAppName(){
	    return fullname;
	}
}