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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.musclehack.musclehack.rss.StackOverflowXmlParser;
import com.musclehack.musclehack.rss.StackOverflowXmlParser.RssItem;

public class FragmentListFeed extends ListFragment {
	//static protected ProgressDialog progressDialog = null;
	public static String TAG_IMAGE_URL = "image_url";
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
			Bundle savedInstanceState){
		Log.d("FragmentListFeed", "protected void onCreateView(...) called");
		if(this.urlFeed != null){ //TODO find out why this is called two times and can be null
			//FragmentListFeed.progressDialog = ProgressDialog.show(this.getActivity(), "",
					//getString(R.string.loading), true);
			new DownloadXmlTask().execute(this.urlFeed);
		}
		View view = super.onCreateView(inflater, container, savedInstanceState);
		Log.d("FragmentListFeed", "protected void onCreateView(...) end");
		return view;
	
	}
	
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void setEntries(List<RssItem> entries){
		Log.d("FragmentListFeed", "public void setEntries(List<RssItem> entries) called");
		//if(FragmentListFeed.progressDialog != null){
			//FragmentListFeed.progressDialog.dismiss();
		//}
		this.entries = entries;
		rssFeedList = new ArrayList<HashMap<String, String>>();
		if(this.entries == null || this.entries.size() <= 0){
			Activity activity = this.getActivity();
			if(activity != null){
				new AlertDialog.Builder(activity)
				.setTitle("No data")
				.setMessage("No data could be retrieve. Please check your internet connection.")
				.setNeutralButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
			}
		}else{

			// adding each child node to HashMap key => value
			for (RssItem entry : entries) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(TAG_TITLE, entry.title);
				map.put(TAG_TEXT, entry.getShortDescription());
				map.put(TAG_IMAGE_URL, entry.getImageUrl());
				rssFeedList.add(map);
			}

			//((SimpleHtmlAdapter)adapter).notifyDataSetChanged();
		}

		Activity activity = this.getActivity();
		if(activity != null){
			ListAdapter adapter = new SimpleRssAdapter(activity,
					rssFeedList,
					R.layout.fragment1rss_row,
					new String[] { TAG_TITLE, TAG_TEXT, TAG_IMAGE_URL },
					new int[] { R.id.title, R.id.text, R.id.image_rss_article2});
			setListAdapter(adapter);
		}
		Log.d("FragmentListFeed", "public void setEntries(List<RssItem> entries) end");
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		Log.d("FragmentListFeed", "public void onListItemClick(...) called");
		super.onListItemClick(l, v, position, id);
		RssItem item = this.entries.get((int)id);
		Uri uri = Uri.parse(item.link);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		Log.d("FragmentListFeed", "public void onListItemClick(...) end");
	}
	
////////////////////////////////////////////////
////////////////////////////////////////////////
////////////////////////////////////////////////
////////////////////////////////////////////////
	private class DownloadXmlTask extends AsyncTask<String, Void, List<RssItem>> {
		//protected List<RssItem> entries = null;
		@Override
		protected List<RssItem> doInBackground(String... urls) {
			Log.d("DownloadXmlTask", "protected List<RssItem> doInBackground(...) called");
			try {
				List<RssItem> items = loadXmlFromNetwork(urls[0]);
				Log.d("DownloadXmlTask", "protected List<RssItem> doInBackground(...) end items");
				return items;
			} catch (IOException e) {
				Log.d("DownloadXmlTask", "protected List<RssItem> doInBackground(...) end catch 1");
				return null;
			} catch (XmlPullParserException e) {
				Log.d("DownloadXmlTask", "protected List<RssItem> doInBackground(...) end catch 2");
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<RssItem> items) { 
			Log.d("DownloadXmlTask", "protected void onPostExecute(...) called");
			//setContentView(R.layout.activity_main);
			// Displays the HTML string in the UI via a WebView
			//WebView myWebView = (WebView) findViewById(R.id.webview);
			//myWebView.loadData(result, "text/html", null);
			//setEntries(this.entries);
			setEntries(items);
			if(items == null){
				this.cancel(true);
			}
			Log.d("DownloadXmlTask", "protected void onPostExecute(...) end");
		}
	}
	

	
	static public List<RssItem> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
		Log.d("DownloadXmlTask", "static public List<RssItem> loadXmlFromNetwork(...) called");
		InputStream stream = null;
		StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
		List<RssItem> entries = null;
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
		Log.d("DownloadXmlTask", "static public List<RssItem> loadXmlFromNetwork(...) end");
		return entries;
	}

	static public InputStream downloadUrl(String urlString) throws IOException {
		Log.d("DownloadXmlTask", "static public InputStream downloadUrl loadXmlFromNetwork(...) called");
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		// Starts the query
		conn.connect();
		InputStream inputStream = conn.getInputStream();
		Log.d("DownloadXmlTask", "static public InputStream downloadUrl loadXmlFromNetwork(...) end");
		return inputStream;
	}
}

