package de.unistuttgart.ipvs.pmp.api.gui.registration;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.api.IPMP;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRegistrationHandler;

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
    
    private Intent mainActivityIntent = null;
    
    private Elements elements;
    
    private IPMP pmp;
    
    private Handler handler;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.activity_registration);
        
        this.handler = new Handler();
        this.elements = new Elements(this);
        this.pmp = PMP.get(getApplication());
        
        loadMetaData();
        
        if (this.mainActivityIntent == null) {
            invokeEvent(EventTypes.NO_ACITIVTY_DEFINED);
        } else {
            startRegistration();
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
        handler.post(new Runnable() {
            
            @Override
            public void run() {
                switch (eventType) {
                    case NO_ACITIVTY_DEFINED:
                        elements.pbPendingRegistration.setVisibility(View.GONE);
                        elements.tvDescription.setVisibility(View.GONE);
                        elements.tvFailureMissingActivity.setVisibility(View.VISIBLE);
                        elements.buttonClose.setVisibility(View.VISIBLE);
                        break;
                    
                    case PMP_NOT_INSTALLED:
                        elements.pbPendingRegistration.setVisibility(View.GONE);
                        elements.tvDescription.setVisibility(View.GONE);
                        elements.tvFailureMissingPMP.setVisibility(View.VISIBLE);
                        elements.buttonClose.setVisibility(View.VISIBLE);
                        break;
                    
                    case REGISTRATION_SUCCEED:
                        Toast.makeText(RegistrationActivity.this, "The registration succeed, you can now use the App",
                                Toast.LENGTH_LONG);
                        break;
                    
                    case REGISTRATION_FAILED:
                        elements.pbPendingRegistration.setVisibility(View.GONE);
                        elements.tvDescription.setVisibility(View.GONE);
                        
                        elements.tvFailureError.setVisibility(View.VISIBLE);
                        elements.tvFailureErrorMessage.setText((String) parameters[0]);
                        elements.tvFailureErrorMessage.setVisibility(View.VISIBLE);
                        elements.buttonClose.setVisibility(View.VISIBLE);
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
    
    
    public void switchToDMainActivity() {
        //startActivityForResult(this.mainActivityIntent, RegistrationActivity.CLOSE_ON_RESULT);
    }
}

/**
 * The Elements class provides all elements defined in the xml.
 * 
 * @author Jakob Jarosch
 */
class Elements {
    
    private Activity parentActivity;
    
    public ProgressBar pbPendingRegistration;
    
    public TextView tvDescription;
    public TextView tvFailureMissingActivity;
    public TextView tvFailureMissingPMP;
    public TextView tvFailureError;
    public TextView tvFailureErrorMessage;
    
    public Button buttonClose;
    
    
    public Elements(Activity activity) {
        this.parentActivity = activity;
        
        this.pbPendingRegistration = (ProgressBar) activity.findViewById(R.id.ProgressBar_PendingRegistration);
        
        this.tvDescription = (TextView) activity.findViewById(R.id.TextView_Description);
        this.tvFailureMissingActivity = (TextView) activity.findViewById(R.id.TextView_Failure_MissingActivity);
        this.tvFailureMissingPMP = (TextView) activity.findViewById(R.id.TextView_Failure_MissingPMP);
        this.tvFailureError = (TextView) activity.findViewById(R.id.TextView_Failure_Error);
        this.tvFailureErrorMessage = (TextView) activity.findViewById(R.id.TextView_Failure_ErrorMessage);
        
        this.buttonClose = (Button) activity.findViewById(R.id.Button_Close);
        
        addListener();
    }
    
    
    private void addListener() {
        this.buttonClose.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Elements.this.parentActivity.finish();
            }
        });
    }
}

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
        this.activity.switchToDMainActivity();
    }
    
    
    @Override
    public void onSuccess() {
        this.activity.invokeEvent(EventTypes.REGISTRATION_SUCCEED);
        this.activity.switchToDMainActivity();
    }
    
    
    @Override
    public void onFailure(String message) {
        this.activity.invokeEvent(EventTypes.REGISTRATION_FAILED, message);
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
    REGISTRATION_SUCCEED,
    REGISTRATION_FAILED,
}
