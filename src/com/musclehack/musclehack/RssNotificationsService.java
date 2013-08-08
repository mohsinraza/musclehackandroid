package com.musclehack.musclehack;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.musclehack.musclehack.rss.StackOverflowXmlParser.RssItem;

public class RssNotificationsService extends Service {
	
	private WakeLock mWakeLock;
	static String articles = "Articles";
	static String recipes = "Recipes";
	static String successes = "Successes";
	static int nTask = 0;
	
	/**
	 * Simply return null, since our Service will not be communicating with
	 * any other components. It just does its work silently.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	/**
	 * This is where we initialize. We call this when onStart/onStartCommand is
	 * called by the system. We won't do anything with the intent here, and you
	 * probably won't, either.
	 */
	private void handleIntent(Intent intent) {
		Log.d("RssNotificationsService","private void handleIntent(Intent intent) called");
		// obtain the wake lock
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		this.mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "tag");
		this.mWakeLock.acquire();
		
		// check the global background data setting
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		if (!cm.getBackgroundDataSetting()) {
			stopSelf();
			return;
		}
		
		// do the actual work, in a separate thread
		SharedPreferences settings
			= PreferenceManager.getDefaultSharedPreferences(RssNotificationsService.this);
		boolean notifArticles = settings.getBoolean("notifArticles", false);
		boolean notifRecipes = settings.getBoolean("notifRecipes", false);
		boolean notifSuccesses = settings.getBoolean("notifSuccesses", false);
		
		if(notifArticles){
			new PollRssTask(MainActivity.urlArticles, RssNotificationsService.articles, 1).execute();
		}
		if(notifRecipes){
			new PollRssTask(MainActivity.urlRecipes, RssNotificationsService.recipes, 2).execute();
		}
		if(notifSuccesses){
			new PollRssTask(MainActivity.urlSuccesses, RssNotificationsService.successes, 3).execute();
		}
		if(!notifArticles && !notifRecipes && !notifSuccesses){
			this.mWakeLock.release();
		}
		Log.d("RssNotificationsService", "private void handleIntent(Intent intent) end");
	}
	
	static public void updateLastDate(Context context, List<RssItem> items, String url){
		Log.d("RssNotificationsService","static public void updateLastDate(...) called");
		if(context != null){
			String keyPreference = null;
			if(url.equals(MainActivity.urlArticles)){
				keyPreference = RssNotificationsService.articles;
			}else if(url.equals(MainActivity.urlRecipes)){
				keyPreference = RssNotificationsService.recipes;
			}else if(url.equals(MainActivity.urlSuccesses)){
				keyPreference = RssNotificationsService.successes;
			}
			Log.d("RssNotificationsService","keyPreference: " + keyPreference);
			Log.d("RssNotificationsService","context: " + context);
			if(items != null && items.size() > 0){
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
				Log.d("RssNotificationsService","settings ok");
				RssItem maxItem = items.get(0);
				Date maxDate = maxItem.getDateTime();
				for(RssItem item:items){
					Date currentDate = item.getDateTime();
					maxDate = maxItem.getDateTime();
					if(maxDate.before(currentDate)){
						maxItem = item;
					}
				}
				SharedPreferences.Editor settingsEditor = settings.edit();
				settingsEditor.putString(keyPreference, maxItem.dateTimeString);
				settingsEditor.commit();
			}
		}
		Log.d("RssNotificationsService","static public void updateLastDate(...) end");
	}
	
	private class PollRssTask extends AsyncTask<Void, Void, Void> {
		protected String url;
		protected String preferenceKey;
		protected int notificationId;
		
		public PollRssTask(String url, String preferenceKey, int notificationId){
			this.url = url;
			this.preferenceKey = preferenceKey;
			this.notificationId = notificationId;
		}
		/**
		 * This is where YOU do YOUR work. There's nothing for me to write here
		 * you have to fill this in. Make your HTTP request(s) or whatever it is
		 * you have to do to get your updates in here, because this is run in a
		 * separate thread
		 */
		@Override
		protected Void doInBackground(Void... params) {
			Log.d("RssNotificationsService","protected Void doInBackground(Void... params) called");
			nTask++;
			List<RssItem> items = null;
			try {
				items = FragmentListFeed.loadXmlFromNetwork(this.url);
				Log.d("PollRssTask", "List<RssItem> items = FragmentListFeed.loadXmlFromNetwork(this.url);");
			} catch (XmlPullParserException e) {
				Log.d("PollRssTask", "} catch (XmlPullParserException e) {");
				e.printStackTrace();
			} catch (IOException e) {
				Log.d("PollRssTask", "} catch (IOException e) {");
				e.printStackTrace();
			}
			if(items != null && items.size() > 0){
				Log.d("RssNotificationsService","if(items != null && items.size() > 0) ok ");
				
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(RssNotificationsService.this);
				String lastDateString = settings.getString(this.preferenceKey, "");
				if(!lastDateString.equals("")){
					Log.d("RssNotificationsService","if(!lastDateString.equals('')) ok ");
					Date lastDate = RssItem.getDateTime(lastDateString);
					RssItem maxItem = items.get(0);
					Date maxDate = maxItem.getDateTime();
					for(RssItem item:items){
						Date currentDate = item.getDateTime();
						maxDate = maxItem.getDateTime();
						if(maxDate.before(currentDate)){
							maxItem = item;
						}
					}
					Log.d("RssNotificationsService","last date: " + lastDate);
					Log.d("RssNotificationsService","max date: " + maxDate);
					if(maxDate.after(lastDate)){
					//if(true){
						Log.d("RssNotificationsService","if(if(maxDate.after(lastDate)) ok ");
						SharedPreferences.Editor settingsEditor = settings.edit();
						settingsEditor.putString(this.preferenceKey, maxItem.dateTimeString);
						settingsEditor.commit();
						Notification notification = new NotificationCompat.Builder(RssNotificationsService.this)
						.setContentTitle(maxItem.title)
						.setContentText(maxItem.link)
						.setSmallIcon(R.drawable.ic_launcher).build();
						notification.flags |= Notification.FLAG_AUTO_CANCEL;
						Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(maxItem.link));
						PendingIntent pendingIntent
							= PendingIntent.getActivity(RssNotificationsService.this,
									0, myIntent,
									Intent.FLAG_ACTIVITY_NEW_TASK);
						notification.contentIntent = pendingIntent;
					
						NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
						notificationManager.notify(this.notificationId, notification);
					}
				}
				//SharedPreferences.Editor settingsEditor = settings.edit();
			}
			
			Log.d("RssNotificationsService","protected Void doInBackground(Void... params) end");
			return null;
		}
		
		/**
		 * In here you should interpret whatever you fetched in doInBackground
		 * and push any notifications you need to the status bar, using the
		 * NotificationManager. I will not cover this here, go check the docs on
		 * NotificationManager.
		 *
		 * What you HAVE to do is call stopSelf() after you've pushed your
		 * notification(s). This will:
		 * 1) Kill the service so it doesn't waste precious resources
		 * 2) Call onDestroy() which will release the wake lock, so the device
		 *	can go to sleep again and save precious battery.
		 */
		@Override
		protected void onPostExecute(Void result) {
			Log.d("NOTIFICATION:RssNotificationsService","protected void onPostExecute(Void result) called");
			nTask--;
			if(nTask == 0){
				stopSelf();
				try{
					RssNotificationsService.this.mWakeLock.release();
				}catch(RuntimeException e){
					Log.d("NOTIFICATION:RssNotificationsService", "WARNING: RssNotificationsService.this.mWakeLock.release failed");
				}
			}
			/*
			new AlertDialog.Builder(RssNotificationsService.this)
			.setTitle("Notification")
			.setMessage("We could have send a notification.")
			.setNeutralButton("Ok",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();
			//*/
			Log.d("NOTIFICATION:RssNotificationsService","protected void onPostExecute(Void result) end");
		}
	}
	
	/**
	 * This is deprecated, but you have to implement it if you're planning on
	 * supporting devices with an API level lower than 5 (Android 2.0).
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		Log.d("RssNotificationsService","public void onStart(Intent intent, int startId) called");
		handleIntent(intent);
		Log.d("RssNotificationsService","public void onStart(Intent intent, int startId) end");
	}
}
	
