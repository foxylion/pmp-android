package de.unistuttgart.ipvs.pmp.pluginmanager;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;


public class GetPluginTask extends AsyncTask<Void, Integer, String> {

    @Override
    protected String doInBackground(Void... params) {
        // TODO Auto-generated method stub
        return null;
    }
/*
    protected String doInBackground(Void... args) {
        int count;
        try {
            url = new URL(url[0]);
            URLConnection connection = this.url.openConnection();
            connection.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream("/sdcard/somewhere/nameofthefile.ext");

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                publishProgress((int)(total*100/lenghtOfFile));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {}
        return null;
    }
    */
}
