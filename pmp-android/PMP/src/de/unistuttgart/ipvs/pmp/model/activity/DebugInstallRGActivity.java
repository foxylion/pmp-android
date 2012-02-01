package de.unistuttgart.ipvs.pmp.model.activity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.gui.util.LongTaskProgressDialog;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidPluginException;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidXMLException;
import de.unistuttgart.ipvs.pmp.model.plugin.PluginProvider;

public class DebugInstallRGActivity extends Activity {
    
    private Handler handler;
    private Dialog dialog;
    private ProgressDialog pd;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        handler = new Handler();
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        final String pkg = getIntent().getStringExtra("pkg");
        
        pd = new ProgressDialog(this);
        pd.setTitle("Install Resource Group");
        pd.setMessage("Injecting '" + pkg + "'...");
        pd.setCancelable(false);
        LongTaskProgressDialog<Void, Void, Void> ltpd = new LongTaskProgressDialog<Void, Void, Void>(pd) {
            
            @Override
            public Void run(Void... params) {
                if (ModelProxy.get().getResourceGroup(pkg) != null) {
                    final boolean result = ModelProxy.get().uninstallResourceGroup(pkg);
                    handler.post(new Runnable() {
                        
                        @Override
                        public void run() {
                            Toast.makeText(DebugInstallRGActivity.this,
                                    "First removing RG... (rm-result: " + result + ")", Toast.LENGTH_SHORT).show();
                        }
                    });
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
                        ModelProxy.get().installResourceGroup(pkg, true);
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
                if (dialog == null) {
                    pd.dismiss();
                    DebugInstallRGActivity.this.finish();
                    Toast.makeText(DebugInstallRGActivity.this, "Installed RG successfully.", Toast.LENGTH_SHORT)
                            .show();
                }
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
    protected void complain(final String title, final String msg) {
        handler.post(new Runnable() {
            
            @Override
            public void run() {
                Builder dialog = new AlertDialog.Builder(DebugInstallRGActivity.this);
                dialog.setTitle(title);
                dialog.setMessage(msg);
                dialog.setPositiveButton("Ok, I will fix it", new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pd.dismiss();
                        dialog.dismiss();
                        DebugInstallRGActivity.this.finish();
                    }
                });
                DebugInstallRGActivity.this.dialog = dialog.show();
            }
        });
    }
}
