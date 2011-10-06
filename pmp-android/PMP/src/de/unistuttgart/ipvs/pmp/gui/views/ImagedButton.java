/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    public ImagedButton(Context context, String name, String identifier, int ImageSource) {
        super(context);
        
        /*Setting the Padding of the ImageButton*/
        setPadding(10, 10, 10, 10);
        
        /*Initialize the fields*/
        this.imageSource = ImageSource;
        this.identifier = identifier;
        this.fullname = name;
        
        /*Setting up the ImagedButton*/
        setOrientation(LinearLayout.VERTICAL);
        setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        
        // False for using the custom onDraw method
        setWillNotDraw(false);
        
        /*TouchListener for the ImagedButton, which handles the 
         * behaviour of the ImagedButton*/
        setOnTouchListener(new OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ImagedButton.this.isDown = true;
                    v.invalidate();
                }
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    ImagedButton.this.isDown = false;
                    v.invalidate();
                }
                return false;
            }
            
        });
        
        /*Creating the painter*/
        this.paint = new Paint();
        this.paint2 = new Paint();
        
        /*Setting up the Icon of the ImagedButton*/
        
        this.image = new ImageView(context);
        this.image.setPadding(0, 10, 0, 0);
        this.image.setLayoutParams(LayoutParamsCreator.createWCWC(1f));
        
        // set the SourceImage to the View
        this.image.setImageResource(this.imageSource);
        
        /*Setting up the AppName lines*/
        this.appName = new TextView(context);
        this.appName2 = new TextView(context);
        this.appName.setLayoutParams(LayoutParamsCreator.createWCWC(1f));
        this.appName2.setLayoutParams(LayoutParamsCreator.createWCWC(1f));
        this.appName.setPadding(5, 5, 5, 5);
        
        this.appName.setGravity(Gravity.CENTER_HORIZONTAL);
        this.appName.setText(name);
        
        /*Sets the Appname max. to 15 Chars each line*/
        if (name.length() > 15) {
            String string = name.substring(0, 15);
            String string2 = name.substring(15);
            if (string2.length() > 15) {
                string2 = string2.substring(0, 15);
            }
            
            this.appName.setText(string);
            this.appName2.setText(string2);
        }
        this.appName.setTextColor(Color.BLACK);
        this.appName2.setTextColor(Color.BLACK);
        
        /*Adding the Views*/
        this.addView(this.image);
        this.addView(this.appName);
        this.addView(this.appName2);
    }
    
    
    /**
     * Custom onDraw method
     */
    @Override
    protected void onDraw(Canvas canvas) {
        
        /*Setting up the painter*/
        this.paint.setColor(Color.BLACK);
        this.paint.setStrokeWidth(1);
        this.paint.setStyle(Style.STROKE);
        this.paint2.setStyle(Style.FILL);
        
        /*Create and draw the black border*/
        RectF rect = new RectF();
        rect.set(0 + getPaddingLeft(), 0 + getPaddingTop(), getWidth() - getPaddingRight(), getHeight()
                - getPaddingBottom());
        canvas.drawRoundRect(rect, 10, 10, this.paint);
        
        /*Create and draw the State of the ImagedButton
         * isDown --> GREEN
         * !isDown--> BLUE*/
        if (this.isDown) {
            this.paint2.setColor(Color.parseColor("#aaaaaa"));
        } else {
            this.paint2.setColor(Color.WHITE);
        }
        //this.paint2.setAlpha(35);
        RectF rect2 = new RectF();
        rect2.set(0 + getPaddingLeft(), 0 + getPaddingTop(), getWidth() - getPaddingRight(), getHeight()
                - getPaddingBottom());
        canvas.drawRoundRect(rect2, 10, 10, this.paint2);
    }
    
    
    /**
     * @return returns the fullname of the App
     */
    public String getName() {
        return this.fullname;
    }
    
    
    /**
     * 
     * @return return the ID of the ImagedButton
     */
    public String getIdentifier() {
        return this.identifier;
    }
}
