/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;
import java.util.Formatter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.OnConfirmationDialogFinished;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;

public class MessageActivity extends Activity implements OnClickListener, OnConfirmationDialogFinished {
    
    private long userId;
    private long tripId;
    private String userName;
    private ListView listMessages;
    private ArrayAdapter<String> adapter;
    private EditText txtMessage;
    private Runnable checkMessages;
    private final int refreshInterval = 5000;
    private final int idConfirmAccept = 246; // Random number
    private final int idConfirmDecline = 142;
    private CharSequence offerType;
    private boolean isOffer = false;
    private boolean isInvitation = false;
    private View offerNotification;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        
        Intent i = getIntent();
        this.userId = i.getLongExtra("userId", -1);
        this.tripId = i.getLongExtra("tripId", -1);
        this.userName = i.getStringExtra("userName");
        
        if (i.hasExtra("isOffer")) {
            isOffer = true;
            ((TextView) findViewById(R.id.message_offer)).setText("Hi, may I please join your trip?");
        }
        if (i.hasExtra("isInvitation"))
            isInvitation = true;
        
        this.listMessages = (ListView) findViewById(R.id.message_list_messages);
        this.txtMessage = (EditText) findViewById(R.id.txtMessage);
        
        findViewById(R.id.btnSend).setOnClickListener(this);
        findViewById(R.id.message_username).setOnClickListener(this);
        findViewById(R.id.btnAccept).setOnClickListener(this);
        findViewById(R.id.btnDecline).setOnClickListener(this);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        checkForNewMessage();
        
        TextView t = (TextView) findViewById(R.id.message_username);
        t.setText(this.userName);
    }
    
    
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        this.listMessages.removeCallbacks(this.checkMessages);
    }
    
    int i = 0;
    
    private String[] msg = "Sed cursus turpis vitae tortor. Donec posuere vulputate arcu. Phasellus accumsan cursus velit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Sed aliquam, nisi quis porttitor congue, elit erat euismod orci, ac placerat dolor lectus quis orci. Phasellus consectetuer vestibulum elit. Aenean tellus metus, bibendum sed, posuere ac, mattis non, nunc. Vestibulum fringilla pede sit amet augue. In turpis. Pellentesque posuere. Praesent turpis. Aenean posuere, tortor sed cursus feugiat, nunc augue blandit nunc, eu sollicitudin urna dolor sagittis lacus. Donec elit libero, sodales nec, volutpat a, suscipit non, turpis. Nullam sagittis. Suspendisse pulvinar, augue ac venenatis condimentum, sem libero volutpat nibh, nec pellentesque velit pede quis nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Fusce id purus. Ut varius tincidunt libero. Phasellus dolor. Maecenas vestibulum mollis diam. Pellentesque ut neque. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. In dui magna, posuere eget, vestibulum et, tempor auctor, justo. In ac felis quis tortor malesuada pretium. Pellentesque auctor neque nec urna. Proin sapien ipsum, porta a, auctor quis, euismod ut, mi. Aenean viverra rhoncus pede. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut non enim eleifend felis pretium feugiat. Vivamus quis mi. Phasellus a est. Phasellus magna. In hac habitasse platea dictumst. Curabitur at lacus ac velit ornare lobortis. Curabitur a felis in nunc fringilla tristique. Morbi mattis ullamcorper velit. Phasellus gravida semper nisi. Nullam vel sem. Pellentesque libero tortor, tincidunt et, tincidunt eget, semper nec, quam. Sed hendrerit. Morbi ac felis."
            .split("\\.");
    
    
    private void checkForNewMessage() {
        if (this.adapter == null) {
            ArrayList<String> a = new ArrayList<String>();
            a.add("Hallo!");
            a.add("just type something");
            this.adapter = new ArrayAdapter<String>(this, R.layout.list_item_chat_message, R.id.chat, a);
            this.listMessages.setAdapter(this.adapter);
            
            this.offerNotification = findViewById(R.id.message_offer_notification);
            // TODO AsyncTask
            this.checkMessages = new Runnable() {
                
                @Override
                public void run() {
                    checkForNewMessage();
                }
            };
        } else {
            this.adapter.add(msg[this.i++]);
            this.adapter.notifyDataSetChanged();
            this.listMessages.setSelection(this.adapter.getCount() - 1);
        }
        this.listMessages.postDelayed(this.checkMessages, this.refreshInterval);
        
        if (this.isOffer) {
            this.offerType = this.isInvitation ? getText(R.string.invitation) : getText(R.string.request);
            this.offerNotification.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.txtNewOffer)).setText((new Formatter()).format(
                    (String) getText(R.string.message_new_offer), this.offerType).toString());
        } else {
            findViewById(R.id.message_offer_notification).setVisibility(View.GONE);
        }
    }
    
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                this.adapter.add(this.txtMessage.getText().toString());
                this.adapter.notifyDataSetChanged();
                this.listMessages.setSelection(this.adapter.getCount() - 1);
                this.txtMessage.getText().clear();
                break;
            case R.id.message_username:
                // TODO Open profile
                break;
            case R.id.btnAccept:
                CharSequence title = (new Formatter()).format(
                        (String) getText(R.string.message_accept_title),
                        this.offerType).toString();
                CharSequence msg = (new Formatter()).format(
                        (String) getText(R.string.message_accept_confirm),
                        this.offerType).toString();
                vhikeDialogs.getConfirmationDialog(this, title, msg, getText(android.R.string.ok),
                        getText(android.R.string.cancel), this.idConfirmAccept).show();
                break;
            case R.id.btnDecline:
                title = (new Formatter()).format((String) getText(R.string.message_decline_title),
                        this.offerType)
                        .toString();
                msg = (new Formatter()).format((String) getText(R.string.message_decline_confirm),
                        this.offerType)
                        .toString();
                vhikeDialogs.getConfirmationDialog(this, title, msg, getText(android.R.string.ok),
                        getText(android.R.string.cancel), this.idConfirmDecline).show();
                break;
        }
        
    }
    
    
    @Override
    public void confirmDialogPositive(int callbackFunctionID) {
        switch (callbackFunctionID) {
            case idConfirmAccept:
                this.isOffer = false;
                this.offerNotification.setVisibility(View.GONE);
                break;
            case idConfirmDecline:
                this.isOffer = false;
                this.offerNotification.setVisibility(View.GONE);
                break;
        }
    }
    
    
    @Override
    public void confirmDialogNegative(int callbackFunctionID) {
    }
}
