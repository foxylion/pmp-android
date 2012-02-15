package de.unistuttgart.ipvs.pmp.apps.calendarwidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
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
		RemoteViews updateViews = buildUpdate();

		// Push update for this widget to the home screen
		ComponentName thisWidget = new ComponentName(this,
				CalendarWidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		manager.updateAppWidget(thisWidget, updateViews);
	}

	private RemoteViews buildUpdate() {
		// Create an Intent to launch calendar app
		Intent intent = new Intent();
		intent.setClassName("de.unistuttgart.ipvs.pmp.apps.calendarapp",
				"de.unistuttgart.ipvs.pmp.api.gui.registration.RegistrationActivity");
		PendingIntent pendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, intent, 0);

		RemoteViews views = new RemoteViews(getApplicationContext()
				.getPackageName(), R.layout.widget_layout);
		views.setOnClickPendingIntent(R.id.header, pendingIntent);

		return views;
	}
}