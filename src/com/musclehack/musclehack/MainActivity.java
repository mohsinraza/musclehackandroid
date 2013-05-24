package com.musclehack.musclehack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class MainActivity extends FragmentActivity {
	private static final int RESULT_SETTINGS = 1;
	/* Tab identifiers */
	static String TAB_A = "Rss Tab";
	static String TAB_B = "Workout log Tab";
	static String TAB_C = "Testimonials Tab";
	static String TAB_D = "Recipes Tab";
	static String TAB_E = "Archives Tab";
	static String TAB_F = "Book Tab";

	TabHost mTabHost;
	
	FragmentListFeed fragment1rss;
	ListFragment fragment2worklog;
	FragmentListFeed fragment3testimonials;
	FragmentListFeed fragment4recipe;
	Fragment4archives fragment5archives;
	Fragment6book fragment6book;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("MainActivity","protected void onCreate(Bundle savedInstanceState) called");
		super.onCreate(savedInstanceState);
		Context context = this.getApplicationContext();
		WorkoutManagerSingleton.setContext(context);
		this.setContentView(R.layout.activity_main);
		this.fragment1rss = new FragmentListFeed();
		this.fragment1rss.setUrlFeed("http://feeds.feedburner.com/MuscleHack");
		this.fragment2worklog = null;
		int levelChoice = WorkoutManagerSingleton.getInstance().getLevelChoice();
		if(levelChoice == 0){
			this.fragment2worklog = new Fragment2worklog();
		}else if(levelChoice == 1){
			this.fragment2worklog = new Fragment2worklog_1subProg();
		}else if(levelChoice == 2){
			this.fragment2worklog = new Fragment2worklog_2week();
		}else if(levelChoice == 3){
			this.fragment2worklog = new Fragment2worklog_3day();
		}else if(levelChoice == 4){
			this.fragment2worklog = new Fragment2worklog_4exercices();
		}
		
		this.fragment3testimonials = new FragmentListFeed();
		this.fragment3testimonials.setUrlFeed("http://www.musclehack.com/category/testimonials-2/feed");
		this.fragment4recipe = new FragmentListFeed();
		this.fragment4recipe.setUrlFeed("http://www.musclehack.com/category/recipes/feed");
		this.fragment5archives = new Fragment4archives();
		this.fragment6book = new Fragment6book();

		this.mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		this.mTabHost.setOnTabChangedListener(this.listener);
		this.mTabHost.setup();
		
		
		this.initializeTab(savedInstanceState);
		Log.d("MainActivity","protected void onCreate(Bundle savedInstanceState) end");
	}

	
	@Override
	public void onStart() {
		super.onStart();
		/*
		int currentTab = this.mTabHost.getCurrentTab();
		if(currentTab == 1){
			this.fragment2worklog.onStart();
		}
		//*/
	}
	@Override
	public void onBackPressed() {
		Log.d("MainActivity","public void onBackPressed called");
		super.onBackPressed();
		/*
		int levelChoice = WorkoutManagerSingleton.getInstance().getLevelChoice();
		if(levelChoice > 0){
			WorkoutManagerSingleton.getInstance().setLevelChoice(0);
		}
		//*/
		/*
		int currentTab = this.mTabHost.getCurrentTab();
		if(currentTab == 1){
			this.fragment2worklog.onDestroy();
		}
		//*/
		Log.d("MainActivity","public void onBackPressed end");
	}
	
	@Override
	public void onDestroy() {
		Log.d("MainActivity","public void onDestroy() called");
		super.onDestroy();
		WorkoutManagerSingleton.closeDatabase();
		Log.d("MainActivity","public void onDestroy() end");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
 
        case R.id.action_settings:
            Intent i = new Intent(this, GeneralPreference.class);
            startActivity(i);
            //startActivityForResult(i, RESULT_SETTINGS);
            break;
 
        }
 
        return true;
    }

 
	/*
	 * Initialize the tabs and set views and identifiers for the tabs
	 */
	public void initializeTab(Bundle savedInstanceState) {
		Log.d("MainActivity","public void initializeTab(Bundle savedInstanceState)  called");
		TabHost.TabSpec spec = mTabHost.newTabSpec(TAB_A);
		//this.mTabHost.setCurrentTab(-3); //?


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
		spec.setIndicator(createTabView(getString(R.string.testimonials), R.drawable.tab3_testimonials));
		mTabHost.addTab(spec);
		
		
		spec = mTabHost.newTabSpec(TAB_D);
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(android.R.id.tabcontent);
			}
		});
		spec.setIndicator(createTabView(getString(R.string.recipes), R.drawable.tab3_recipes));
		mTabHost.addTab(spec);
		
		spec = mTabHost.newTabSpec(TAB_E);
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(android.R.id.tabcontent);
			}
		});
		spec.setIndicator(createTabView(getString(R.string.archives), R.drawable.tab4_archives));
		mTabHost.addTab(spec);
		
		spec = mTabHost.newTabSpec(TAB_F);
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(android.R.id.tabcontent);
			}
		});
		spec.setIndicator(createTabView(getString(R.string.book), R.drawable.tab6_book));
		mTabHost.addTab(spec);

		Log.d("MainActivity","Checking savedInstanceState...");
		if (savedInstanceState == null) {
			Log.d("MainActivity","savedInstanceState == null");
			this.mTabHost.setCurrentTab(-3);
			WorkoutManagerSingleton.getInstance().setLevelChoice(0);
		}else{
			Log.d("MainActivity","savedInstanceState != null");
			int tabPosition = savedInstanceState.getInt("TAB_POSITION");
			Log.d("MainActivity","tabPosition: " +tabPosition);
			this.mTabHost.setCurrentTab(tabPosition);
		}
		Log.d("MainActivity","public void initializeTab(Bundle savedInstanceState)  end");
	}

	/*
	 * TabChangeListener for changing the tab when one of the tabs is pressed
	 */
	TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {
		public void onTabChanged(String tabId) {
			Log.d("MainActivity","public void onTabChanged(String tabId) { called");
			if(tabId.equals(TAB_A)){
				pushFragments(getString(R.string.rss), fragment1rss);
			}else if(tabId.equals(TAB_B)){
				/*
				ListFragment newFragment = null;
				int levelChoice = WorkoutManagerSingleton.getInstance().getLevelChoice();
				if(levelChoice == 0){
					newFragment = fragment2worklog;
				}else if(levelChoice == 1){
					newFragment = new Fragment2worklog_1subProg();
				}else if(levelChoice == 2){
					newFragment = new Fragment2worklog_2week();
				}else if(levelChoice == 3){
					newFragment = new Fragment2worklog_3day();
				}else if(levelChoice == 4){
					newFragment = new Fragment2worklog_4exercices();
				}
				//*/
				pushFragments(getString(R.string.worklog), fragment2worklog);
			}else if(tabId.equals(TAB_C)){
				pushFragments(getString(R.string.testimonials), fragment3testimonials);
			}else if(tabId.equals(TAB_D)){
				pushFragments(getString(R.string.recipes), fragment4recipe);
			}else if(tabId.equals(TAB_E)){
				pushFragments(getString(R.string.archives), fragment5archives);
			}else if(tabId.equals(TAB_F)){
				pushFragments(getString(R.string.book), fragment6book);
			}
			Log.d("MainActivity","public void onTabChanged(String tabId) { end");
		}
	};
		
	/*
	 * adds the fragment to the FrameLayout
	 */
	public void pushFragments(String tag, Fragment fragment){
		FragmentManager manager = this.getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
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
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		int idTab = this.mTabHost.getCurrentTab();
		outState.putInt("TAB_POSITION", idTab);
		super.onSaveInstanceState(outState);
	}
}
