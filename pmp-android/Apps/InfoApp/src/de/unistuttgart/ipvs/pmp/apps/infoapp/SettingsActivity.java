package de.unistuttgart.ipvs.pmp.apps.infoapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SettingsActivity extends ListActivity {
    
    final Intent mResultIntent = new Intent();
    int mCurrentPanel;
    SharedPreferences mSettings;
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String[] panels = getIntent().getStringArrayExtra("panels");
        final CharSequence[] asd = panels;
        
        String[] options = new String[] { "Set starting panel" };
        mSettings = getSharedPreferences("settings", 0);
        
        setListAdapter(new ArrayAdapter<String>(this, R.layout.settings, options));
        
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
            
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createDialog(asd);
            }
        });
        
    }
    
    
    private void createDialog(final CharSequence[] items) {
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a panel");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            
            public void onClick(DialogInterface dialog, int item) {
                mCurrentPanel = item;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    
    private void setSettings() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("panel", mCurrentPanel);
        editor.commit();
    }
    
    
    public void onStop() {
        super.onStop();
        setSettings();
        setResult(1, mResultIntent.putExtra("panelNumber", mCurrentPanel));
    }
}
