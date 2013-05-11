package com.musclehack.musclehack;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Fragment4archives extends Fragment {
    static protected ProgressDialog progressDialog;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        
        View view = inflater.inflate(R.layout.fragment4archives, container, false);
        WebView webView = (WebView) view.findViewById(R.id.webViewArchives);
        Fragment4archives.progressDialog = ProgressDialog.show(this.getActivity(), "",
        											"Loading. Please wait...", true);
        webView.setWebViewClient(new WebViewClient() {
	        	public void onPageFinished(WebView view, String url) {
	        		Fragment4archives.progressDialog.dismiss();
	        	}
        	});
        webView.loadUrl("http://www.musclehack.com/archives/");
        return view;
    }
}
