package de.unistuttgart.ipvs.pmp.pluginmanager.tasks;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.gson.Gson;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.RGsAvailableAdapter;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.tab.RGsAvailableTab;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.pluginmanager.AvailablePlugins;
import de.unistuttgart.ipvs.pmp.pluginmanager.PluginManager;

public class GetAvailablePluginsTask extends AsyncTask<Void, Integer, Void> {
    
    private RGsAvailableTab context;
    private String json;
    
    
    public GetAvailablePluginsTask(RGsAvailableTab context) {
        this.context = context;
        this.json = "";
    }
    
    
    @Override
    protected void onPreExecute() {
        this.context.pd = new ProgressDialog(this.context);
        this.context.pd.setMessage(this.context.getString(R.string.rgs_task_getting));
        this.context.pd.setIndeterminate(false);
        this.context.pd.setMax(100);
        this.context.pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        if (PluginManager.getInstance().getAvailablePlugins() == null) {
            this.context.pd.show();
            
        }
    }
    
    
    @Override
    protected Void doInBackground(Void... arg0) {
        
        if (PluginManager.getInstance().getAvailablePlugins() == null) {
            AvailablePlugins availablePlugins = fetchAvailablePlugins();
            for (AvailablePlugins.Plugin plugin : availablePlugins.getPlugins()) {
                IResourceGroup rg = ModelProxy.get().getResourceGroup(plugin.getIdentifier());
                if (rg != null) {
                    plugin.setInstalledRevision(rg.getRevision());
                    Log.e("Plugin " + plugin.getIdentifier() + " installed, Version set to "
                            + plugin.getInstalledRevision());
                } else {
                    Log.e("Plugin " + plugin.getIdentifier() + " not installed, nothing to do.");
                }
            }
            PluginManager.getInstance().setAvailablePlugins(availablePlugins);
        }
        return null;
    }
    
    
    public void onProgressUpdate(int... args) {
        this.context.pd.setProgress(args[0]);
    }
    
    
    @Override
    public void onPostExecute(Void args) {
        this.context.pd.setProgress(100);
        this.context.pd.dismiss();
        AvailablePlugins availablePlugins = PluginManager.getInstance().getAvailablePlugins();
        RGsAvailableAdapter adapter = new RGsAvailableAdapter(this.context, availablePlugins);
        this.context.setListAdapter(adapter);
        
    }
    
    
    private AvailablePlugins fetchAvailablePlugins() {
        try {
            String lang = Locale.getDefault().getLanguage();
            String urlString = "http://www.friederschueler.de/pmp/?lang=" + lang;
            Log.e("Fetching new available Plugins from " + urlString);
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.connect();
            long lenghtOfFile = connection.getContentLength();
            BufferedInputStream input = new BufferedInputStream(url.openStream());
            byte data[] = new byte[1024];
            long totalBytesRead = 0;
            int bytesRead;
            
            while ((bytesRead = input.read(data)) != -1) {
                totalBytesRead += bytesRead;
                publishProgress((int) (totalBytesRead * 100 / lenghtOfFile));
                this.json += new String(data, 0, bytesRead);
            }
            
            input.close();
        } catch (Exception e) {
            Log.e(e.toString());
        }
        return new Gson().fromJson(this.json, AvailablePlugins.class);
    }
}
