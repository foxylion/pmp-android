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
    private boolean isOffer = true;
    private boolean isInvitation = true;
    private View offerNotification;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        
        System.out.println("Oncreate");
        
        Intent i = getIntent();
        this.userId = i.getLongExtra("userId", -1);
        this.tripId = i.getLongExtra("tripId", -1);
        this.userName = i.getStringExtra("userName");
        System.out.println(this.userId + "/" + this.tripId);
        
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
        System.out.println("on resume");
        
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
            this.adapter.add("Halahlahla: " + this.i++);
            this.adapter.notifyDataSetChanged();
            this.listMessages.setSelection(this.adapter.getCount() - 1);
            this.isInvitation = !this.isInvitation;
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
