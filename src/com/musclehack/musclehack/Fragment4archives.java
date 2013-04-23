package com.musclehack.musclehack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class Fragment4archives extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        
        View view = inflater.inflate(R.layout.fragment4archives, container, false);
        WebView webView = (WebView) view.findViewById(R.id.webViewArchives);
        webView.loadUrl("http://www.musclehack.com/archives/");
        return view;
    }
}
