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
package de.unistuttgart.ipvs.pmp.gui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.views.LayoutParamsCreator;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

/**
 * PrivacyLvlActivity shows the list with available Service Levels
 * 
 * 
 * @author Alexander Wassiljew
 * 
 */
public class ServiceLvlActivity extends Activity {
    
    /**
     * App of which the levels have to be load
     */
    private String appName;
    
    /**
     * Index of the current app
     */
    private String identifier;
    
    /**
     * Scrollable Service levels
     */
    private ScrollView scroll;
    
    /**
     * Main Layout of the activity, which will be drawn to the Canvas
     */
    private LinearLayout parentLayout;
    
    
    /** Called when the activity is created for the first time */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadIntentsExtras();
        this.appName = ModelSingleton.getInstance().getModel().getApp(this.identifier).getName();
        this.setTitle(this.getString(R.string.servive_level_for) + " " + this.appName);
        createParentLayout();
        loadServiceLevels();
        
        this.scroll = new ScrollView(this);
        this.scroll.addView(this.parentLayout);
        //this.scroll.setBackgroundColor(Color.rgb(211, 211, 211));
        
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(LayoutParamsCreator.createFPFP());
        
        TextView tv = new TextView(this);
        tv.setText(R.string.set_service_level_hint);
        tv.setBackgroundColor(Color.GRAY);
        tv.setTextColor(Color.BLACK);
        tv.setPadding(5, 5, 5, 10);
        
        ll.addView(tv);
        ll.addView(this.scroll);
        
        setContentView(ll);
    }
    
    
    /**
     * Creating and setting the parent layout
     */
    private void createParentLayout() {
        this.parentLayout = new LinearLayout(this);
        this.parentLayout.setScrollBarStyle(0);
        this.parentLayout.setOrientation(LinearLayout.VERTICAL);
        this.parentLayout.setLayoutParams(LayoutParamsCreator.createFPFP());
        this.parentLayout.setBackgroundColor(Color.BLACK);
    }
    
    
    /**
     * Loading the Privacy Levels for the GUI
     */
    private void loadServiceLevels() {
        IApp app = ModelSingleton.getInstance().getModel().getApp(this.identifier);
        
        IServiceLevel levelArray[] = app.getServiceLevels();
        Log.v("ServiceLeveltActivity: Number of Service Levels" + String.valueOf(levelArray.length) + "for the app: "
                + app.getName());
        RadioGroup group = new RadioGroup(this);
        
        /* Iterate over Service Levels */
        for (int i = 0; i < levelArray.length; i++) {
            RadioButton button = new RadioButton(this);
            //button.setBackgroundColor(Color.rgb(211, 211, 211));
            button.setLayoutParams(LayoutParamsCreator.createFPWC());
            button.setGravity(Gravity.LEFT);
            button.setTextColor(Color.WHITE);
            
            Spanned sp = Html.fromHtml("<b>Service Level " + levelArray[i].getLevel() + "</b><br>"
                    + levelArray[i].getName());
            button.setText(sp);
            
            if (i % 2 == 0) {
                button.setBackgroundColor(Color.parseColor("#222222"));
            } else {
                button.setBackgroundColor(Color.BLACK);
            }
            
            /* Check if Service Level is set */
            if (app.getActiveServiceLevel().getLevel() == i) {
                button.setChecked(true);
            }
            
            /* Check if Service Level is available */
            if (levelArray[i].isAvailable()) {
                button.setOnTouchListener(new OnLevelTouchListener(button.getContext(), levelArray[i].getDescription(),
                        button, this, this.identifier, i));
                
            } else {
                button.setEnabled(false);
                button.setTextColor(Color.GRAY);
            }
            group.addView(button);
        }
        group.setGravity(Gravity.CENTER_HORIZONTAL);
        this.parentLayout.addView(group);
    }
    
    
    /**
     * Loading the intent
     */
    private void loadIntentsExtras() {
        this.identifier = getIntent().getExtras().getString(de.unistuttgart.ipvs.pmp.Constants.INTENT_IDENTIFIER);
    }
    
    
    /**
     * Reloading the activity
     */
    public void reloadActivity() {
        this.parentLayout.removeAllViews();
        loadServiceLevels();
    }
}

/**
 * OnLevelTouchListener
 * 
 * @author Alexander Wassiljew
 * 
 */
class OnLevelTouchListener implements OnTouchListener {
    
    private Context context;
    // private RadioButton parent;
    private ServiceLvlActivity activity;
    private int levelID;
    private String identifier;
    
    
    public OnLevelTouchListener(Context context, String lvlDescr, RadioButton button, ServiceLvlActivity activity,
            String identifier, int levelID) {
        this.context = context;
        // this.parent = button; // TODO check if that is really required, seems
        // not to be used
        this.activity = activity;
        this.levelID = levelID;
        this.identifier = identifier;
    }
    
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Dialog dialog = createDialog();
            dialog.show();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
        }
        return false;
    }
    
    
    /**
     * Creating the dialog for setting up the Service Level
     * 
     * @return Dialog
     */
    private Dialog createDialog() {
        IServiceLevel sl = ModelSingleton.getInstance().getModel().getApp(this.identifier)
                .getServiceLevel(this.levelID);
        
        /* Dialog */
        final Dialog dialog = new Dialog(this.context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setTitle(this.context.getString(R.string.service_level) + " " + sl.getLevel());
        
        /**
         * Reloading the ServiceLvlActivity if no changes occur
         */
        dialog.setOnCancelListener(new OnCancelListener() {
            
            @Override
            public void onCancel(DialogInterface dialog) {
                // Reloads the Service Level Activity if new Service Level
                // wasn't set
                dialog.dismiss();
                OnLevelTouchListener.this.activity.reloadActivity();
                
            }
        });
        
        /* Description */
        TextView description = new TextView(this.context);
        
        String req_res = "";
        String old_res = "";
        int PLlength = sl.getPrivacyLevels().length;
        IPrivacyLevel[] plevels = sl.getPrivacyLevels();
        for (int j = 0; j < PLlength; j++) {
            if (old_res.contentEquals(plevels[j].getResourceGroup().getName())) {
                ;
            } else {
                old_res = plevels[j].getResourceGroup().getName();
                req_res = req_res + "- " + plevels[j].getResourceGroup().getName() + "<br>";
            }
        }
        
        description.setText(Html.fromHtml("<b>" + sl.getName() + "</b><br>" + sl.getDescription() + "<br><br><b>"
                + this.context.getString(R.string.required_resource) + "</b><br>" + req_res));
        
        description.setPadding(10, 0, 10, 0);
        
        /* Apply */
        Button apply = new Button(this.context);
        apply.setLayoutParams(LayoutParamsCreator.createFPFP(1f));
        apply.setText(R.string.apply);
        
        /**
         * Setting the Service Level
         */
        apply.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                final IApp app = ModelSingleton.getInstance().getModel().getApp(OnLevelTouchListener.this.identifier);
                IServiceLevel levelArray[] = app.getServiceLevels();
                final IServiceLevel level = levelArray[OnLevelTouchListener.this.levelID];
                
                /* Set the Service Level here */
                
                final Dialog waitingDialog = ProgressDialog.show(OnLevelTouchListener.this.context,
                        OnLevelTouchListener.this.context.getString(R.string.please_wait),
                        OnLevelTouchListener.this.context.getString(R.string.set_service_level), true);
                
                /*
                 * Create an AsynchTask and wait till
                 * setActiveServiceLevelAsPreset is done
                 */
                new AsyncTask<Void, Void, Void>() {
                    
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            app.setActiveServiceLevelAsPreset(level.getLevel());
                        } catch (Exception exc) {
                            Log.e("ServiceLevelActivity: setActiveServiceLevelAsPreset() --> Exception", exc);
                        }
                        return null;
                    }
                    
                    
                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        waitingDialog.dismiss();
                        dialog.cancel();
                        OnLevelTouchListener.this.activity.finish();
                    }
                }.execute();
            }
        });
        
        /* Cancel */
        Button cancel = new Button(this.context);
        cancel.setLayoutParams(LayoutParamsCreator.createFPFP(1f));
        cancel.setText(this.context.getString(R.string.cancel));
        
        /**
         * Canceling the dialog
         */
        cancel.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        
        /* The hint for applying */
        TextView applyHint = new TextView(this.context);
        applyHint.setText(R.string.service_level_apply_hint);
        applyHint.setPadding(10, 0, 10, 0);
        
        /* DialogLayout which holds the description and the buttonLayout */
        LinearLayout dialogLayout = new LinearLayout(this.context);
        dialogLayout.setLayoutParams(LayoutParamsCreator.createFPFP());
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        
        ScrollView dialogScroll = new ScrollView(this.context);
        dialogScroll.setLayoutParams(LayoutParamsCreator.createFPFP(0.5f));
        dialogScroll.addView(description);
        
        /* buttonLayout which holds the buttons: "Apply", "Cancel" */
        LinearLayout buttonLayout = new LinearLayout(this.context);
        
        // buttonLayout.setLayoutParams(LayoutParamsCreator.createFPFP());
        buttonLayout.addView(apply);
        buttonLayout.addView(cancel);
        
        dialogLayout.addView(dialogScroll);
        dialogLayout.addView(applyHint);
        dialogLayout.addView(buttonLayout);
        dialog.setContentView(dialogLayout);
        
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = LayoutParams.FILL_PARENT;
        lp.height = LayoutParams.FILL_PARENT;
        dialog.getWindow().setAttributes(lp);
        
        return dialog;
    }
    
}
