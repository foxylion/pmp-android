package de.unistuttgart.ipvs.pmp.api.gui.registration;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.api.IPMP;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.gui.registration.RegistrationStateListItem.State;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRegistrationHandler;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestServiceFeaturesHandler;

/**
 * The {@link RegistrationActivity} provides an activity based registration at the privacy management platform.
 * It automatically registers the compatible app at PMP and opens after success the main activity of the app.
 * 
 * The app needs to be defined in the AndroidManifest.xml as the main launcher activity, also a meta tag with the normal
 * main activity (like the following example) has to be added to the activity definition.
 * 
 * <pre>
 * &lt;activity android:name="de.unistuttgart.ipvs.pmp.api.activity.RegistrationActivity" android:label="@string/app_name"&gt;
 *    &lt;intent-filter&gt;
 *       &lt;action android:name="android.intent.action.MAIN" /&gt;
 *       &lt;category android:name="android.intent.category.LAUNCHER" /&gt;
 *    &lt;/intent-filter&gt;
 *    &lt;meta-data android:name="mainActivity" android:value=".gui.MainActivity" /&gt;
 * &lt;/activity&gt;
 * </pre>
 * 
 * @author Jakob Jarosch
 */
public class RegistrationActivity extends Activity {
    
    private static final int CLOSE_ON_RESULT = 111;
    private static final String REGISTERED_KEY = "registered";
    
    private Intent mainActivityIntent = null;
    
    private Elements elements;
    
    private IPMP pmp;
    
    private Handler handler;
    
    private EventTypes lastEvent;
    
    private SharedPreferences appPreferences;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        this.handler = new Handler();
        this.appPreferences = getSharedPreferences("appPreferences", MODE_PRIVATE);
        this.pmp = PMP.get(getApplication());
        
        checkRegistration();
    }
    
    
    private void checkRegistration() {
        if (!appPreferences.getBoolean(REGISTERED_KEY, false)) {
            initiateRegistration();
        } else {
            new Thread() {
                
                public void run() {
                    // TODO add the !pmp.isRegistered() here
                    if (false) {
                        handler.post(new Runnable() {
                            
                            @Override
                            public void run() {
                                initiateRegistration();
                            }
                        });
                    } else {
                        loadMetaData();
                        switchToMainActivity(false);
                    }
                };
            }.start();
            
        }
    }
    
    
    private void initiateRegistration() {
        setContentView(R.layout.pmp_api_activity_registration);
        
        this.elements = new Elements(this);
        
        /* Initiating, processing step 1 */
        this.elements.setState(1, State.PROCESSING);
        
        loadMetaData();
        
        if (this.mainActivityIntent == null) {
            invokeEvent(EventTypes.NO_ACITIVTY_DEFINED);
        } else {
            invokeEvent(EventTypes.START_REGISTRATION);
            startRegistration();
        }
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        if (this.lastEvent == EventTypes.SF_SCREEN_OPENED) {
            invokeEvent(EventTypes.SF_SCREEN_CLOSED);
        }
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RegistrationActivity.CLOSE_ON_RESULT) {
            finish();
        }
    }
    
    
    private void startRegistration() {
        RegistrationHandler regHandler = new RegistrationHandler(this);
        pmp.register(regHandler);
    }
    
    
    public void invokeEvent(final EventTypes eventType, final Object... parameters) {
        this.lastEvent = eventType;
        
        handler.post(new Runnable() {
            
            @Override
            public void run() {
                switch (eventType) {
                    case NO_ACITIVTY_DEFINED:
                        elements.tvFailureMissingActivity.setVisibility(View.VISIBLE);
                        elements.buttonClose.setVisibility(View.VISIBLE);
                        
                        elements.setState(1, State.FAIL);
                        break;
                    
                    case PMP_NOT_INSTALLED:
                        elements.tvFailureMissingPMP.setVisibility(View.VISIBLE);
                        elements.buttonClose.setVisibility(View.VISIBLE);
                        
                        elements.setState(1, State.FAIL);
                        elements.setState(2, State.NONE);
                        elements.setState(3, State.NONE);
                        break;
                    
                    case START_REGISTRATION:
                        elements.setState(1, State.SUCCESS);
                        elements.setState(2, State.PROCESSING);
                        elements.setState(3, State.PROCESSING);
                        break;
                    
                    case REGISTRATION_SUCCEED:
                        elements.tvSelectInitialSF.setVisibility(View.VISIBLE);
                        elements.buttonSelectInitialSF.setVisibility(View.VISIBLE);
                        
                        elements.setState(2, State.SUCCESS);
                        elements.setState(3, State.SUCCESS);
                        elements.setState(4, State.NEW);
                        
                        SharedPreferences.Editor editor = appPreferences.edit();
                        editor.putBoolean(REGISTERED_KEY, true);
                        editor.commit();
                        break;
                    
                    case ALREADY_REGISTERED:
                        elements.setState(2, State.SUCCESS);
                        elements.setState(3, State.SKIPPED);
                        elements.setState(4, State.SKIPPED);
                        elements.setState(5, State.PROCESSING);
                        switchToMainActivity(false);
                        break;
                    
                    case REGISTRATION_FAILED:
                        elements.tvFailureError.setVisibility(View.VISIBLE);
                        elements.tvFailureErrorMessage.setText((String) parameters[0]);
                        elements.tvFailureErrorMessage.setVisibility(View.VISIBLE);
                        elements.buttonClose.setVisibility(View.VISIBLE);
                        
                        elements.setState(2, State.FAIL);
                        elements.setState(3, State.FAIL);
                        break;
                    
                    case SF_SCREEN_OPENED:
                        elements.setState(4, State.PROCESSING);
                        pmp.requestServiceFeatures(new ArrayList<String>(), new ServiceFeaturesHandler());
                        break;
                    
                    case SF_SCREEN_CLOSED:
                        elements.tvSelectInitialSF.setVisibility(View.GONE);
                        elements.buttonSelectInitialSF.setVisibility(View.GONE);
                        elements.tvOpenApp.setVisibility(View.VISIBLE);
                        elements.buttonOpenApp.setVisibility(View.VISIBLE);
                        
                        elements.setState(4, State.SUCCESS);
                        elements.setState(5, State.NEW);
                        break;
                    
                    case OPEN_APP:
                        elements.setState(5, State.PROCESSING);
                        switchToMainActivity(true);
                        break;
                }
            }
        });
    }
    
    
    /**
     * Load the meta data from the AndroidManifest.xml
     */
    private void loadMetaData() {
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String activityName = bundle.getString("mainActivity");
            
            this.mainActivityIntent = createMainActivityIntent(activityName);
        } catch (NameNotFoundException e) {
            Log.e("Failed to load package details from android package manager.", e);
        }
    }
    
    
    /**
     * Create a new Intent for opening the main activity of the app.
     * 
     * @param activityName
     *            The Name of the activity class which should be opened.
     * @return The created intent.
     */
    private Intent createMainActivityIntent(String activityName) {
        Intent intent = new Intent();
        intent.setClassName(getApplicationContext(), activityName);
        
        return intent;
    }
    
    
    public void switchToMainActivity(boolean transitionAnimation) {
        if (!transitionAnimation) {
            this.mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        startActivityForResult(this.mainActivityIntent, RegistrationActivity.CLOSE_ON_RESULT);
    }
}

/**
 * The Elements class provides all elements defined in the xml.
 * 
 * @author Jakob Jarosch
 */
class Elements {
    
    private RegistrationActivity parentActivity;
    
    public TextView tvFailureMissingActivity;
    public TextView tvFailureMissingPMP;
    public TextView tvFailureError;
    public TextView tvFailureErrorMessage;
    public TextView tvOpenApp;
    public TextView tvSelectInitialSF;
    
    public Button buttonClose;
    public Button buttonOpenApp;
    public Button buttonSelectInitialSF;
    
    private List<RegistrationStateListItem> registrationStateList = new ArrayList<RegistrationStateListItem>();
    
    
    public Elements(RegistrationActivity activity) {
        this.parentActivity = activity;
        
        this.tvFailureMissingActivity = (TextView) activity.findViewById(R.id.TextView_Failure_MissingActivity);
        this.tvFailureMissingPMP = (TextView) activity.findViewById(R.id.TextView_Failure_MissingPMP);
        this.tvFailureError = (TextView) activity.findViewById(R.id.TextView_Failure_Error);
        this.tvFailureErrorMessage = (TextView) activity.findViewById(R.id.TextView_Failure_ErrorMessage);
        this.tvOpenApp = (TextView) activity.findViewById(R.id.TextView_OpenApp);
        this.tvSelectInitialSF = (TextView) activity.findViewById(R.id.TextView_OpenSFList);
        
        this.buttonClose = (Button) activity.findViewById(R.id.Button_Close);
        this.buttonOpenApp = (Button) activity.findViewById(R.id.Button_OpenApp);
        this.buttonSelectInitialSF = (Button) activity.findViewById(R.id.Button_OpenSFList);
        
        fillRegistrationStateList();
        
        addListener();
    }
    
    
    /**
     * Updates the state of an item from the list.
     * 
     * @param item
     *            Number of the item, begins with 1.
     * @param state
     *            New state of the item.
     */
    public void setState(int item, RegistrationStateListItem.State state) {
        registrationStateList.get(item - 1).setState(state);
    }
    
    
    private void fillRegistrationStateList() {
        registrationStateList.add(new RegistrationStateListItem(parentActivity, 1, "Preparing & detecting PMP"));
        registrationStateList.add(new RegistrationStateListItem(parentActivity, 2, "Checking registration"));
        registrationStateList.add(new RegistrationStateListItem(parentActivity, 3, "Registration at PMP"));
        registrationStateList.add(new RegistrationStateListItem(parentActivity, 4, "Set initial Service Features"));
        registrationStateList.add(new RegistrationStateListItem(parentActivity, 5, "Open the App"));
        
        ((LinearLayout) parentActivity.findViewById(R.id.LinearLayout_States)).removeAllViews();
        
        for (RegistrationStateListItem item : registrationStateList) {
            ((LinearLayout) parentActivity.findViewById(R.id.LinearLayout_States)).addView(item);
        }
    }
    
    
    private void addListener() {
        this.buttonClose.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Elements.this.parentActivity.finish();
            }
        });
        
        this.buttonSelectInitialSF.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                parentActivity.invokeEvent(EventTypes.SF_SCREEN_OPENED);
            }
        });
        
        this.buttonOpenApp.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                parentActivity.invokeEvent(EventTypes.OPEN_APP);
            }
        });
    }
}

/**
 * The {@link RegistrationHandler} reacts on events generated by the registration at PMP.
 * 
 * @author Jakob Jarosch
 */
class RegistrationHandler extends PMPRegistrationHandler {
    
    private RegistrationActivity activity;
    
    
    public RegistrationHandler(RegistrationActivity activity) {
        this.activity = activity;
    }
    
    
    @Override
    public void onBindingFailed() {
        this.activity.invokeEvent(EventTypes.PMP_NOT_INSTALLED);
    }
    
    
    @Override
    public void onAlreadyRegistered() {
        this.activity.invokeEvent(EventTypes.ALREADY_REGISTERED);
    }
    
    
    @Override
    public void onSuccess() {
        this.activity.invokeEvent(EventTypes.REGISTRATION_SUCCEED);
    }
    
    
    @Override
    public void onFailure(String message) {
        this.activity.invokeEvent(EventTypes.REGISTRATION_FAILED, message);
    }
}

class ServiceFeaturesHandler extends PMPRequestServiceFeaturesHandler {
    
    @Override
    public void onRequestFailed() {
        super.onRequestFailed();
    }
}

/**
 * The defined event types do have influences on the gui.
 * 
 * @author Jakob Jarosch
 * 
 */
enum EventTypes {
    NO_ACITIVTY_DEFINED,
    PMP_NOT_INSTALLED,
    START_REGISTRATION,
    REGISTRATION_SUCCEED,
    REGISTRATION_FAILED,
    ALREADY_REGISTERED,
    SF_SCREEN_OPENED,
    SF_SCREEN_CLOSED,
    OPEN_APP,
}
