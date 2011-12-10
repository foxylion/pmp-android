package de.unistuttgart.ipvs.pmp.gui.util;

import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * A handler for tasks with long duration, displays a progress dialog in the meantime.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class LongTaskProgressDialog<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    
    public abstract Result run(Params... params);
    
    
    public void processResult(Result result) {
    }
    
    private ProgressDialog progressDialog;
    
    
    public LongTaskProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }
    
    
    @Override
    protected void onPreExecute() {
        this.progressDialog.show();
    }
    
    
    @Override
    protected Result doInBackground(Params... params) {
        return run(params);
    };
    
    
    @Override
    protected void onPostExecute(Result result) {
        this.progressDialog.dismiss();
        processResult(result);
    }
    
}
