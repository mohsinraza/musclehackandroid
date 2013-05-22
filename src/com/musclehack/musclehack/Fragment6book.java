package com.musclehack.musclehack;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Fragment6book extends Fragment {
    static protected ProgressDialog progressDialog;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
            super.onCreateView(inflater, container, savedInstanceState);
            //setContentView(R.layout.activity_main);
            View view = inflater.inflate(R.layout.fragment4archives, container, false);
            WebView webView = (WebView) view.findViewById(R.id.webViewArchives);
            Fragment6book.progressDialog = ProgressDialog.show(this.getActivity(), "",
					getString(R.string.loading), true);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setPluginState(PluginState.ON);

            //---you need this to prevent the webview from
            // launching another browser when a url
            // redirection occurs---
            webView.setWebViewClient(new Callback());
           
            //String pdfURL = "http://ownwebsite.googlecode.com/files/resume_cedric_bettinger_v11en.pdf";
            String pdfURL = "http://www.musclehack.com/wp-content/uploads/2013/03/THT5point1.pdf";
            webView.setWebViewClient(new WebViewClient() {
	        	public void onPageFinished(WebView view, String url) {
	        		Fragment6book.progressDialog.dismiss();
	        	}
        	});
            webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfURL);


        /*
        View view = inflater.inflate(R.layout.fragment4archives, container, false);
        WebView webView = (WebView) view.findViewById(R.id.webViewArchives);
        Fragment4archives.progressDialog = ProgressDialog.show(this.getActivity(), "",
        											getString(R.string.loading), true);
        webView.setWebViewClient(new WebViewClient() {
	        	public void onPageFinished(WebView view, String url) {
	        		Fragment4archives.progressDialog.dismiss();
	        	}
        	});
        webView.loadUrl("http://www.musclehack.com/archives/");
        //*/
        return view;
    }

	private class Callback extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(
	            WebView view, String url) {
	        return(false);
	    }
	}
}
