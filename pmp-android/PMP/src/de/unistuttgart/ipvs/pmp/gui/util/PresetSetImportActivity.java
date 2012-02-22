package de.unistuttgart.ipvs.pmp.gui.util;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.gui.preset.ActivityPresets;

/**
 * Activity which handles a link click matched to the hostname defined in the AndroidManifest.xml.
 * 
 * @author Jakob Jarosch
 */
public class PresetSetImportActivity extends Activity {
    
    private static final int INTENT_RESULT_ID = 12345;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // TODO get the id from intent...
        Uri data = getIntent().getData();
        List<String> params = data.getPathSegments();
        String id = params.get(0); // "presetSetId"
        
        Intent intent = new Intent(this, ActivityPresets.class);
        intent.putExtra(GUIConstants.ACTIVITY_ACTION, GUIConstants.DOWNLOAD_PRESET_SET);
        intent.putExtra(GUIConstants.PRESET_SET_ID, id);
        startActivityForResult(intent, INTENT_RESULT_ID);
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == INTENT_RESULT_ID) {
            finish();
        }
    }
}
