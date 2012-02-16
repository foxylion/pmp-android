package de.unistuttgart.ipvs.pmp.gui.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.view.TwoRowProgressBar;
import de.unistuttgart.ipvs.pmp.model.element.missing.MissingPrivacySettingValue;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroup;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidPluginException;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidXMLException;
import de.unistuttgart.ipvs.pmp.model.server.IServerDownloadCallback;
import de.unistuttgart.ipvs.pmp.model.server.ServerProvider;

public class RGInstaller {
    
    public static String[] getMissingResourceGroups(IPreset preset) {
        List<String> missingRGs = new ArrayList<String>();
        for (MissingPrivacySettingValue ps : preset.getMissingPrivacySettings()) {
            if (!missingRGs.contains(ps.getResourceGroup())) {
                missingRGs.add(ps.getResourceGroup());
            }
        }
        return missingRGs.toArray(new String[missingRGs.size()]);
    }
    
    
    public static String[] getMissingResourceGroups(IServiceFeature serviceFeature) {
        List<String> missingRGs = new ArrayList<String>();
        for (MissingPrivacySettingValue ps : serviceFeature.getMissingPrivacySettings()) {
            if (!missingRGs.contains(ps.getResourceGroup())) {
                missingRGs.add(ps.getResourceGroup());
            }
        }
        return missingRGs.toArray(new String[missingRGs.size()]);
    }
    
    
    /**
     * Installs a new {@link ResourceGroup} with ui feedback.
     * 
     * @param context
     *            Context required to create the dialog.
     * @param resourceGroups
     *            Identifiers of the ResourceGroups which should be installed.
     * @param callback
     *            A callback which is invoked on completion. Can be null if not required.
     */
    public static void installResourceGroups(final Context context, final String[] resourceGroups,
            final ICallback callback) {
        
        final Handler guiHandler = new Handler(Looper.getMainLooper());
        
        guiHandler.post(new Runnable() {
            
            @Override
            public void run() {
                final TwoRowProgressBar pb = new TwoRowProgressBar(context);
                pb.setTitle(context.getString(R.string.rg_processing_installation));
                pb.setTaskProgress(0, resourceGroups.length);
                pb.setProgress(0, 1);
                pb.show();
                
                /* Set up the ServerProvider for updating the current download state */
                ServerProvider.getInstance().setCallback(new IServerDownloadCallback() {
                    
                    @Override
                    public void step(int position, int length) {
                        // Do nothing...
                    }
                    
                    
                    @Override
                    public void download(final int position, final int length) {
                        guiHandler.post(new Runnable() {
                            
                            @Override
                            public void run() {
                                pb.setProgress(position, length);
                            }
                        });
                    }
                });
                
                new Thread() {
                    
                    @Override
                    public void run() {
                        for (int i = 0; i < resourceGroups.length; i++) {
                            /* Install the ResourceGroup */
                            String error = null;
                            boolean success = false;
                            try {
                                success = ModelProxy.get().installResourceGroup(resourceGroups[i], false);
                            } catch (InvalidXMLException e) {
                                error = e.getMessage();
                            } catch (InvalidPluginException e) {
                                error = e.getMessage();
                            }
                            
                            final String message = (success ? "Installed the Resource successfully."
                                    : "Failed to install the Resource:\n" + error);
                            
                            /* Update the UI */
                            final int current = i;
                            guiHandler.post(new Runnable() {
                                
                                @Override
                                public void run() {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                    pb.setTaskProgress(current, resourceGroups.length);
                                }
                            });
                        }
                        
                        /* Finally close the UI */
                        guiHandler.post(new Runnable() {
                            
                            @Override
                            public void run() {
                                pb.dismiss();
                                if (callback != null) {
                                    callback.callback();
                                }
                            }
                        });
                    };
                }.start();
            }
        });
    }
}
