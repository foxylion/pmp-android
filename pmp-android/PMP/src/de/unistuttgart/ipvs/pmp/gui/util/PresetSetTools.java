package de.unistuttgart.ipvs.pmp.gui.util;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.gui.preset.DialogPresetsImportExport;
import de.unistuttgart.ipvs.pmp.gui.preset.DialogPresetsImportStart;
import de.unistuttgart.ipvs.pmp.gui.util.dialog.DialogLongRunningTask;
import de.unistuttgart.ipvs.pmp.model.server.ServerProvider;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;

public class PresetSetTools {
    
    public interface ICallback {
        
        public void ended(boolean succcess);
    }
    
    public interface ICallbackDownload {
        
        public void ended(IPresetSet presetSet);
    }
    
    public interface ICallbackUpload {
        
        public void ended(String id);
    }
    
    
    public static void importPresets(Context context, ICallback callback) {
        new DialogPresetsImportStart(context, callback).show();
    }
    
    
    public static void exportPresets(Context context) {
        new DialogPresetsImportExport(context).show();
    }
    
    
    public static void downloadPresetSet(Context context, final String id, final ICallbackDownload callback) {
        new DialogLongRunningTask(context, new Runnable() {
            
            @Override
            public void run() {
                IPresetSet presetSet = ServerProvider.getInstance().loadPresetSet(id);
                
                callback.ended(presetSet);
            }
        });
        
    }
    
    
    public static void uploadPresetSet(Context context, final IPresetSet presetSet, final ICallbackUpload callback) {
        new DialogLongRunningTask(context, new Runnable() {
            
            @Override
            public void run() {
                String id = ServerProvider.getInstance().storePresetSet(presetSet);
                
                callback.ended(id);
            }
        });
        
    }
}
