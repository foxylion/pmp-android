package de.unistuttgart.ipvs.pmp.pluginmanager.tasks;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.activity.RGDetailsActivity;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;

public class InstallPluginTask extends AsyncTask<String, Integer, Void> {
    
    private RGDetailsActivity context;
    
    
    public InstallPluginTask(RGDetailsActivity context) {
        this.context = context;
    }
    
    
    @Override
    protected void onPreExecute() {
        context.pd = new ProgressDialog(context);
        context.pd.setMessage(context.getString(R.string.rgs_task_installing));
        context.pd.setIndeterminate(false);
        context.pd.setMax(100);
        context.pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        context.pd.show();
    }
    
    
    @Override
    protected Void doInBackground(String... identifier) {
        if (fetchPlugin(identifier[0])) {
            if (deployPlugin(identifier[0])) {
                ModelProxy.get().installResourceGroup(identifier[0]);
            }
        }
        return null;
    }
    
    
    public void onProgressUpdate(int... args) {
        context.pd.setProgress(args[0]);
    }
    
    
    @Override
    public void onPostExecute(Void args) {
        context.pd.setProgress(100);
        context.pd.dismiss();
        context.finish();
        //AvailablePlugins availablePlugins = PluginManager.getInstance().getAvailablePlugins();
        //RGsAvailableAdapter adapter = new RGsAvailableAdapter(context, availablePlugins);
        //context.setListAdapter(adapter);
        
    }
    
    
    private boolean deployPlugin(String identifier) {
        try {
            File path = PMPApplication.getContext().getDir("plugins", 0);
            File file = new File(path, identifier + ".jar");
            InputStream input = new FileInputStream(file);
            ZipInputStream zinput = new ZipInputStream(input);
            ZipEntry entry;
            while ((entry = zinput.getNextEntry()) != null) {
                saveEntry(zinput, entry);
            }
            return true;
        } catch (Exception e) {
            Log.e(e.toString());
            return false;
        }
    }
    
    
    private void saveEntry(ZipInputStream zinput, ZipEntry target) throws ZipException, IOException {
        int count;
        byte data[] = new byte[8192];
        Log.e(target.getName());
        FileOutputStream fos = new FileOutputStream(target.getName());
        BufferedOutputStream output = new BufferedOutputStream(fos);
        while ((count = zinput.read(data, 0, 8192)) != -1) {
            output.write(data, 0, count);
        }
        output.flush();
        output.close();
    }
    
    
    private boolean fetchPlugin(String identifier) {
        try {
            String urlString = "http://www.friederschueler.de/pmp/" + identifier + ".jar";
            
            Log.e("Fetching Plugin " + identifier + " from " + urlString);
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.connect();
            
            File outputPath = PMPApplication.getContext().getDir("plugins", 0);
            File outputFile = new File(outputPath, identifier + ".jar");
            Log.e("Write Plugin to " + outputFile.toString());
            
            long lenght = connection.getContentLength();
            
            InputStream input = new BufferedInputStream(url.openStream());
            FileOutputStream output = new FileOutputStream(outputFile);
            
            byte data[] = new byte[8192];
            long totalBytesRead = 0;
            int bytesRead;
            
            while ((bytesRead = input.read(data)) != -1) {
                totalBytesRead += bytesRead;
                publishProgress((int) (totalBytesRead * 100 / lenght));
                output.write(data, 0, bytesRead);
            }
            
            output.flush();
            output.close();
            input.close();
            return true;
        } catch (Exception e) {
            Log.e(e.toString());
            return false;
        }
    }
}
