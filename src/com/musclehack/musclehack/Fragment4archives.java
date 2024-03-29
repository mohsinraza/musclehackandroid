package com.musclehack.musclehack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Fragment4archives extends Fragment {
	static protected ProgressDialog progressDialog;
	static public final String url = "http://www.musclehack.com/archives/";
	protected View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			this.view = inflater.inflate(R.layout.fragment4archives, container, false);
			//*
			Fragment4archives.progressDialog = ProgressDialog.show(this.getActivity(), "",
														getString(R.string.loading), true);

			WebView webView = (WebView) view.findViewById(R.id.webViewArchives);
			webView.setWebViewClient(new WebViewClient() {
					public void onPageFinished(WebView view, String url) {
						Fragment4archives.progressDialog.dismiss();
					}
				});
			//webView.loadUrl("http://www.musclehack.com/archives/");
			//String archivesFeedUrl = "http://feeds.feedburner.com/MuscleHack?max-results=10";
			//new DownloadXmlTask().execute(archivesFeedUrl);
			new RetrieveWebContentTask().execute(Fragment4archives.url);
		return view;
	}
	
	public void setContentRemovingHeader(String content){
		Log.d("Fragment4archives", "public void setContentRemovingHeader(String content) called");
		View view = this.getView();
		if(view != null){
			WebView webView = (WebView) view.findViewById(R.id.webViewArchives);
			String contentWithoutHeader = getContentWithoutHeader(content);
			if(contentWithoutHeader.equals("")){
				Log.d("Fragment4archives", "web content null");
				this.displayNoDataDialog();
			}else{
				webView.loadData(URLEncoder.encode(contentWithoutHeader).replaceAll("\\+"," "), "text/html", "utf-8" );
			}
			Log.d("Fragment4archives", "public void setContentRemovingHeader(String content) end");
		}
	}
	
	public void displayNoDataDialog(){
		Activity activity = this.getActivity();
		Resources res = activity.getResources();
		new AlertDialog.Builder(activity)
		.setTitle(res.getString(R.string.warningNoData))
		.setMessage(res.getString(R.string.warningNoDataMessage))
		.setNeutralButton("Ok",
		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();
	}
	
	public boolean onBackPressed() {
		Log.d("Fragment4archives", "FILTER public void onBackPressed() called");
		WebView webView = (WebView) view.findViewById(R.id.webViewArchives);
		Log.d("Fragment4archives", "FILTER can go back:" + webView.canGoBack());
		Log.d("Fragment4archives", "FILTER isFocused:" + webView.isFocused());
		boolean wentBack = false;
		if (webView.canGoBack()) {
			Log.d("Fragment4archives", "FILTER webView go back");
			webView.goBack();
			wentBack = true;
		}
		Log.d("Fragment4archives", "FILTER public void onBackPressed() end");
		return wentBack;
	}

	protected String getContentWithoutHeader(String content){
		String contentWithoutHeader = "";
		if(content != null){
			String[] elements = content.split("<div class=\"car-container\">");
			if(elements.length > 1){
				contentWithoutHeader = elements[1];
				elements = contentWithoutHeader
						.split("</span></li></ul></li></ul>");
				contentWithoutHeader = elements[0];
				contentWithoutHeader += "</span></li></ul>";
				/*
				elements = contentWithoutHeader.split("</ul>");
				elements[elements.length-1] = "";
				contentWithoutHeader = elements[0];
				for(int i=1; i<elements.length; i++){
					String element = elements[i];
					contentWithoutHeader += "</ul>" + element;
				}
				//*/

				if(!contentWithoutHeader.equals("")){
					String css =
							"<style>" +
									"body{" +
									"background-color:#EEEEEE;" +
									"} "+
									".car-yearmonth{" +
									"font-weight:bold;" +
									"}" +
									"ul li a:link{" +
									"color:black;" +
									"}" +
							"</style>";
					contentWithoutHeader = css + contentWithoutHeader;
				}
			}
		}
		return contentWithoutHeader;
	}
	
	private class RetrieveWebContentTask extends AsyncTask<String, Void, String> {
		protected boolean success;
		@Override
		protected String doInBackground(String... urls) {
			try {
				String webContent = loadWebContentFromUrl(urls[0]);
				Fragment4archives.this.setContentRemovingHeader(webContent);
				this.success = true;
				return webContent;
			} catch (IOException e) {
				Log.d("RetrieveWebContentTask", " failure catch");
				this.success = false;
				return null;
			}
		}

		@Override
		protected void onPostExecute(String content) {  
			//setContentView(R.layout.activity_main);
			// Displays the HTML string in the UI via a WebView
			//WebView myWebView = (WebView) findViewById(R.id.webview);
			//myWebView.loadData(result, "text/html", null);
			//setEntries(this.entries);
			Log.d("RetrieveWebContentTask", "protected void onPostExecute(String content) called");
			Fragment4archives.progressDialog.dismiss();
			if(!this.success){
				Fragment4archives.this.displayNoDataDialog();
			}
			Log.d("RetrieveWebContentTask", "protected void onPostExecute(String content) end");
			
		}
	}
	
	public static String loadWebContentFromUrl(String url) throws ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);

		String html = "";
		InputStream in = response.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder str = new StringBuilder();
		String line = null;
		while((line = reader.readLine()) != null)
		{
		    str.append(line);
		}
		in.close();
		html = str.toString();
		return html;
	}
	/*
	public void setEntries(List<RssItem> entries){
		if(entries == null ||entries.size() <= 0){
			Fragment4archives.progressDialog.dismiss();
			new AlertDialog.Builder(this.getActivity())
			.setTitle("No data")
			.setMessage("No data could be retrieve. Please check your internet connection.")
			.setNeutralButton("Ok",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();
		}else{
			View view = this.getView();
			WebView webView = (WebView) view.findViewById(R.id.webViewArchives);
			String webContent = "<p>work</p>";
			for (RssItem entry : entries) {
				webContent += "<p>" + entry.title + "</p>";
			}
			webView.loadData(webContent, "text/html", null);
		}
	}
	

	private class DownloadXmlTask extends AsyncTask<String, Void, List<RssItem>> {
		//protected List<RssItem> entries = null;
		@Override
		protected List<RssItem> doInBackground(String... urls) {
			try {
				return FragmentListFeed.loadXmlFromNetwork(urls[0]);
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
	//*/
	
	private class Callback extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(
				WebView view, String url) {
			return(false);
		}
	}
}
