package com.musclehack.musclehack;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.musclehack.musclehack.rss.StackOverflowXmlParser;
import com.musclehack.musclehack.rss.StackOverflowXmlParser.RssItem;

public class FragmentListFeed extends ListFragment {
	public static String TAG_TITLE = "title";
	public static String TAG_TEXT = "text";
	protected List<RssItem> entries = null;
	protected ArrayList<HashMap<String, String>> rssFeedList;
	protected ListAdapter adapter;
	protected String urlFeed;

	
	public void setUrlFeed(String urlFeed){
		this.urlFeed = urlFeed;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//new DownloadXmlTask().execute("http://feeds.feedburner.com/MuscleHack");
		new DownloadXmlTask().execute(this.urlFeed);
		return super.onCreateView(inflater, container, savedInstanceState);
	
	}
	
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void setEntries(List<RssItem> entries){
		this.entries = entries;
		rssFeedList = new ArrayList<HashMap<String, String>>();
		if(this.entries == null || this.entries.size() <= 0){
			new AlertDialog.Builder(this.getActivity())
			.setTitle("No data")
			.setMessage("No data could be retrieve. Please check your internet connection.")
			.setNeutralButton("Ok",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();
			
		}else{

			// adding each child node to HashMap key => value
			for (RssItem entry : entries) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(TAG_TITLE, entry.title);
				map.put(TAG_TEXT, entry.description);
				rssFeedList.add(map);
			}


			//((SimpleHtmlAdapter)adapter).notifyDataSetChanged();
		}
		
		Activity activity = this.getActivity();
		if(activity != null){
			ListAdapter adapter = new SimpleRssAdapter(activity,
					rssFeedList,
					R.layout.fragment1rss_row,
					new String[] { TAG_TITLE, TAG_TEXT },
					new int[] { R.id.title, R.id.text });
			setListAdapter(adapter);
		}

	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		RssItem item = this.entries.get((int)id);
		Uri uri = Uri.parse(item.link);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
////////////////////////////////////////////////
////////////////////////////////////////////////
////////////////////////////////////////////////
////////////////////////////////////////////////
	private class DownloadXmlTask extends AsyncTask<String, Void, List<RssItem>> {
		//protected List<RssItem> entries = null;
		@Override
		protected List<RssItem> doInBackground(String... urls) {
			try {
				return loadXmlFromNetwork(urls[0]);
			} catch (IOException e) {
				return null;
			} catch (XmlPullParserException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<RssItem> items) {  
			//setContentView(R.layout.activity_main);
			// Displays the HTML string in the UI via a WebView
			//WebView myWebView = (WebView) findViewById(R.id.webview);
			//myWebView.loadData(result, "text/html", null);
			//setEntries(this.entries);
			setEntries(items);
			if(items == null){
				this.cancel(true);
			}
		}
	}
	

	
	private List<RssItem> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
		InputStream stream = null;
		StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();

		try {
			stream = downloadUrl(urlString);		
			entries = stackOverflowXmlParser.parse(stream);
			
		// Makes sure that the InputStream is closed after the app is
		// finished using it.
		} finally {
			if (stream != null) {
				stream.close();
			} 
		 }
		return entries;
	}

	private InputStream downloadUrl(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		// Starts the query
		conn.connect();
		return conn.getInputStream();
	}
}

