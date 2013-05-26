package com.musclehack.musclehack;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;


public class RssReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent arg1){
		Log.d("RssReceiver","public void onReceive(Context context, Intent arg1) called");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String minutesString = prefs.getString("notificationIntervalInMinutes", "20");
		int minutes = Integer.parseInt(minutesString);AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, RssNotificationsService.class);
		PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
		am.cancel(pi);
		// by my own convention, minutes <= 0 means notifications are disabled
		if (minutes > 0) {
			am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + minutes*10*1000,
				minutes*60*1000, pi);
		}
		Log.d("RssReceiver","public void onReceive(Context context, Intent arg1) end");
	}

}