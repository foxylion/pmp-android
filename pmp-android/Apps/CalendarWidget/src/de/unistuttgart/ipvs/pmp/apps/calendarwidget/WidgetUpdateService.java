package de.unistuttgart.ipvs.pmp.apps.calendarwidget;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.resourcegroups.database.IDatabaseConnection;
import android.app.Application;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.RemoteViews;

public class WidgetUpdateService extends Service {
    
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        
        // Build the widget update for today
        buildUpdate(getApplicationContext(), null, false);
        
        new UIUpdateThread(getApplication(), getApplicationContext()).start();
    }
    
    
    public static void buildUpdate(Context context, String[][] entries, boolean pmpFailed) {
        // Push update for this widget to the home screen
        ComponentName thisWidget = new ComponentName(context, CalendarWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        
        // Create an Intent to launch calendar app
        Intent intent = new Intent();
        intent.setClassName("de.unistuttgart.ipvs.pmp.apps.calendarapp",
                "de.unistuttgart.ipvs.pmp.api.gui.registration.RegistrationActivity");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setOnClickPendingIntent(R.id.header, pendingIntent);
        
        if (entries == null) {
            if (pmpFailed) {
                views.setViewVisibility(R.id.pmp_view, View.VISIBLE);
                views.setViewVisibility(R.id.empty_view, View.GONE);
                views.setViewVisibility(R.id.loading_view, View.GONE);
            } else {
                views.setViewVisibility(R.id.pmp_view, View.GONE);
                views.setViewVisibility(R.id.empty_view, View.GONE);
                views.setViewVisibility(R.id.loading_view, View.VISIBLE);
            }
        } else {
            views.removeAllViews(R.id.widget_items);
            
            views.setViewVisibility(R.id.loading_view, View.GONE);
            views.setViewVisibility(R.id.pmp_view, View.GONE);
            
            if (entries.length == 0) {
                views.setViewVisibility(R.id.empty_view, View.VISIBLE);
            } else {
                views.setViewVisibility(R.id.empty_view, View.GONE);
                
                for (int i = 0; i < entries.length; i++) {
                    String[] entry = entries[i];
                    RemoteViews view = new RemoteViews(context.getPackageName(),
                            (i % 2 == 0 ? R.layout.dark_widget_item : R.layout.light_widget_item));
                    view.setTextViewText(R.id.widget_item_date, entry[0]);
                    view.setTextViewText(R.id.widget_item_description, entry[1]);
                    views.addView(R.id.widget_items, view);
                }
            }
        }
        
        manager.updateAppWidget(thisWidget, views);
    }
}

class UIUpdateThread extends Thread {
    
    private Context context;
    private Application app;
    
    /**
     * Identifier of the needed resource group
     */
    private static final String RG_IDENTIFIER = "de.unistuttgart.ipvs.pmp.resourcegroups.database";
    
    /**
     * Resource identifier
     */
    private static final String R_IDENTIFIER = "databaseResource";
    /**
     * Identifier to get the resource
     */
    private static final PMPResourceIdentifier PMP_IDENTIFIER = PMPResourceIdentifier.make(RG_IDENTIFIER, R_IDENTIFIER);
    
    private static final String DB_TABLE_NAME = "appointments";
    private static final String DATE = "Date";
    
    
    public UIUpdateThread(Application app, Context context) {
        this.context = context;
        this.app = app;
    }
    
    
    @Override
    public void run() {
        
        PMP.get(app).getResource(PMP_IDENTIFIER, new PMPRequestResourceHandler() {
            
            @Override
            public void onBindingFailed() {
                WidgetUpdateService.buildUpdate(context, null, true);
                Log.d(WidgetUpdateService.class, "Failed to connect to PMP, onBindingFailed() was called.");
            }
            
            
            @Override
            public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder) {
                if (binder == null) {
                    WidgetUpdateService.buildUpdate(context, null, true);
                    Log.d(WidgetUpdateService.class, "Failed to use resource, pmp returned a NULL-Binder.");
                } else {
                    IDatabaseConnection database = IDatabaseConnection.Stub.asInterface(binder);
                    try {
                        database.open("appointments");
                        
                        if (database.isTableExisted("appointments")) {
                            long rowCount = database.queryWithLimit(
                                    DB_TABLE_NAME,
                                    null,
                                    "date >= "
                                            + new Date(new Date().getYear(), new Date().getMonth(), new Date().getDay())
                                                    .getTime(), null, null, null, DATE, "4");
                            
                            String[][] entries = new String[(int) rowCount][2];
                            
                            // Getting the rows
                            for (int itr = 0; itr < rowCount; itr++) {
                                String[] columns = database.getRowAt(itr);
                                
                                // Storing everything from this
                                // appointment
                                String name = columns[1];
                                Date date = new Date(Long.valueOf(columns[4]));
                                
                                entries[itr][0] = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(date);
                                entries[itr][1] = name;
                            }
                            WidgetUpdateService.buildUpdate(context, entries, false);
                        } else {
                            WidgetUpdateService.buildUpdate(context, new String[][] {}, false);
                        }
                        
                        database.close();
                    } catch (SecurityException e) {
                        WidgetUpdateService.buildUpdate(context, null, true);
                        Log.d(WidgetUpdateService.class, "Failed to use resource, got a security exception.", e);
                    } catch (RemoteException e) {
                        WidgetUpdateService.buildUpdate(context, null, true);
                        Log.d(WidgetUpdateService.class, "Failed to use resource, got a remote exception.", e);
                    }
                    
                }
            }
        });
    }
}
