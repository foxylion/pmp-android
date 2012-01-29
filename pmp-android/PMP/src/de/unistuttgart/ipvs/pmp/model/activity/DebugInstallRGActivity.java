package de.unistuttgart.ipvs.pmp.model.activity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.gui.util.LongTaskProgressDialog;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidPluginException;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidXMLException;
import de.unistuttgart.ipvs.pmp.model.plugin.PluginProvider;

public class DebugInstallRGActivity extends Activity {
    
    @Override
    protected void onResume() {
        super.onResume();
        
        final String pkg = getIntent().getStringExtra("pkg");
        
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Install Resource Group");
        pd.setMessage("Injecting '" + pkg + "'...");
        pd.setCancelable(false);
        LongTaskProgressDialog<Void, Void, Void> ltpd = new LongTaskProgressDialog<Void, Void, Void>(pd) {
            
            @Override
            public Void run(Void... params) {
                if (ModelProxy.get().getResourceGroup(pkg) != null) {
                    ModelProxy.get().uninstallResourceGroup(pkg);
                }
                
                try {
                    String path = getPackageManager().getApplicationInfo(pkg, 0).sourceDir;
                    InputStream rgStream = new FileInputStream(path);
                    try {
                        PluginProvider.getInstance().injectFile(pkg, rgStream);
                    } finally {
                        rgStream.close();
                    }
                    try {
                        ModelProxy.get().installResourceGroup(pkg);
                    } catch (InvalidXMLException ixmle) {
                        Log.e("Invalid XML", ixmle);
                        complain("Invalid XML", ixmle);
                    } catch (InvalidPluginException ipe) {
                        Log.e("Invalid Plugin", ipe);
                        complain("Invalid Plugin", ipe);
                    }
                } catch (IOException ioe) {
                    Log.e("Cannot install RG", ioe);
                    complain("Cannot install RG", ioe);
                } catch (NameNotFoundException e) {
                    Log.e("DebugInstallRGActivity not found the " + pkg + " details: ", e);
                }
                
                return null;
            }
            
            
            @Override
            protected void onPostExecute(Void result) {
                DebugInstallRGActivity.this.finish();
            }
        };
        ltpd.execute();
    }
    
    
    /**
     * Complains about an exception
     * 
     * @param title
     * @param t
     */
    protected void complain(String title, Throwable t) {
        complain(title, t.getClass().getCanonicalName() + ": " + t.getMessage() + " (see LogCat)");
    }
    
    
    /**
     * Complains to the user (mostly about the user being dumb as ****.
     * 
     * @param title
     * @param msg
     */
    protected void complain(String title, String msg) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(msg)
                .setPositiveButton("Ok, I will fix it", new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).show();
    }
    
}
