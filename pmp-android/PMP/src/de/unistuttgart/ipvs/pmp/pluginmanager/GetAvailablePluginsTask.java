package de.unistuttgart.ipvs.pmp.pluginmanager;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import com.google.gson.Gson;

import android.os.AsyncTask;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.gui.adapter.RGsAvailableAdapter;
import de.unistuttgart.ipvs.pmp.gui.tab.RGsAvailableTab;

public class GetAvailablePluginsTask extends AsyncTask<String, Integer, String> {
    
    private RGsAvailableTab context;
    private String json;
    
    
    public GetAvailablePluginsTask(RGsAvailableTab context) {
        this.context = context;
        json = "";
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String lang = Locale.getDefault().getLanguage();
            String urlString = "http://www.friederschueler.de/pmp/?lang=" + lang;
            Log.e("Requesting " + urlString);
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
                json += new String(data, 0, bytesRead);
            }
            input.close();
        } catch (Exception e) {
            Log.e(e.toString());
        }
        
        return json;
    }
    
    
    public void onProgressUpdate(int... args) {
        context.pd.setProgress(args[0]);
    }
    
    
    @Override
    public void onPostExecute(String json) {
        context.pd.setProgress(100);
        context.pd.dismiss();
        AvailablePlugins availablePlugins = new Gson().fromJson(json, AvailablePlugins.class);
        RGsAvailableAdapter adapter = new RGsAvailableAdapter(context, availablePlugins);
        context.setListAdapter(adapter);  
        
    }
}
