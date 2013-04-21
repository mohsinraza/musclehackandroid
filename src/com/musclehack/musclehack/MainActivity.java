package com.musclehack.musclehack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	/* Tab identifiers */
    static String TAB_A = "First Tab";
    static String TAB_B = "Second Tab";
    static String TAB_C = "Cart Tab";

    TabHost mTabHost;
    
    Fragment1rss fragment1rss;
    Fragment2worklog fragment2worklog;
    Fragment3cart fragment3cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.fragment1rss = new Fragment1rss();
        this.fragment2worklog = new Fragment2worklog();
        this.fragment3cart = new Fragment3cart();
        
        this.mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        this.mTabHost.setOnTabChangedListener(this.listener);
        this.mTabHost.setup();
        
        this.initializeTab();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	this.getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /*
     * Initialize the tabs and set views and identifiers for the tabs
     */
    public void initializeTab() {
        
        TabHost.TabSpec spec = mTabHost.newTabSpec(TAB_A);
        this.mTabHost.setCurrentTab(-3);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(android.R.id.tabcontent);
            }
        });
        spec.setIndicator(createTabView(getString(R.string.rss), R.drawable.tab1_rss));
        mTabHost.addTab(spec);


        spec = mTabHost.newTabSpec(TAB_B);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(android.R.id.tabcontent);
            }
        });
        spec.setIndicator(createTabView(getString(R.string.worklog), R.drawable.tab2_worklog));
        mTabHost.addTab(spec);
        
        spec = mTabHost.newTabSpec(TAB_C);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(android.R.id.tabcontent);
            }
        });
        spec.setIndicator(createTabView(getString(R.string.cart), R.drawable.tab3_cart));
        mTabHost.addTab(spec);
    }
    
    /*
     * TabChangeListener for changing the tab when one of the tabs is pressed
     */
    TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {
          public void onTabChanged(String tabId) {
              if(tabId.equals(TAB_A)){
                pushFragments(getString(R.string.rss), fragment1rss);
              }else if(tabId.equals(TAB_B)){
                  pushFragments(getString(R.string.worklog), fragment2worklog);
              }else if(tabId.equals(TAB_C)){
                  pushFragments(getString(R.string.cart), fragment3cart);
              }
          }
        };
        
    /*
     * adds the fragment to the FrameLayout
     */
    public void pushFragments(String tag, Fragment fragment){
        FragmentManager   manager         =   this.getSupportFragmentManager();
        FragmentTransaction ft            =   manager.beginTransaction();
        ft.replace(android.R.id.tabcontent, fragment);
        ft.commit();
    }
    
    /*
     * returns the tab view i.e. the tab icon and text
     */
    private View createTabView(final String text, final int id) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_icon, null);
        ImageView imageView =   (ImageView) view.findViewById(R.id.tab_icon);
        imageView.setImageDrawable(getResources().getDrawable(id));
        ((TextView) view.findViewById(R.id.tab_text)).setText(text);
        return view;
    }
}
