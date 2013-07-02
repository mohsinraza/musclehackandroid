package com.musclehack.musclehack;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
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
	

	static public final String urlArticles = "http://feeds.feedburner.com/MuscleHack/";
	static public final String urlRecipes = "http://www.musclehack.com/category/recipes/feed";
	static public final String urlSuccesses = "http://www.musclehack.com/category/testimonials-2/feed";

	TabHost mTabHost;
	
	FragmentListFeed fragment1rss;
	ListFragment fragment2worklog;
	FragmentListFeed fragment3testimonials;
	FragmentListFeed fragment4recipe;
	Fragment4archives fragment5archives;
	Fragment6book fragment6book;
	
	public MainActivity(){
		this.mTabHost = null;
		this.fragment1rss = null;
		this.fragment2worklog = null;
		this.fragment3testimonials = null;
		this.fragment4recipe = null;
		this.fragment5archives = null;
		this.fragment6book = null;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("MainActivity","protected void onCreate(Bundle savedInstanceState) called");
		super.onCreate(savedInstanceState);
		Context context = this.getApplicationContext();
		WorkoutManagerSingleton.setContext(context);
		WorkoutManagerSingleton.setMainActivity(this);
		this.setContentView(R.layout.activity_main);
		this.fragment1rss = new FragmentListFeed();
		this.fragment1rss.setUrlFeed(MainActivity.urlArticles);
		this.fragment2worklog = null;
		if(savedInstanceState != null){
			int tabPosition = savedInstanceState.getInt("TAB_POSITION");
			if(tabPosition == 1){
				FragmentManager manager = this.getSupportFragmentManager();
				for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {	
					manager.popBackStack();
				}
			}
		}
		this.fragment2worklog = new Fragment2worklog();
		
		this.fragment3testimonials = new FragmentListFeed();
		this.fragment3testimonials.setUrlFeed(MainActivity.urlSuccesses);
		this.fragment4recipe = new FragmentListFeed();
		this.fragment4recipe.setUrlFeed(MainActivity.urlRecipes);
		this.fragment5archives = new Fragment4archives();
		this.fragment6book = new Fragment6book();

		this.mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		this.mTabHost.setOnTabChangedListener(this.listener);
		this.mTabHost.setup();
		
		
		this.initializeTab(savedInstanceState);
		Log.d("MainActivity","protected void onCreate(Bundle savedInstanceState) end");
	}
	
	public void onResume() {
		Log.d("MainActivity","public void onResume() called");
		
		super.onResume();
		//*
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String minutesString = prefs.getString("notificationIntervalInMinutes", "20");
		int minutes = Integer.parseInt(minutesString);
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent i = new Intent(this, RssNotificationsService.class);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		am.cancel(pi);
		// by my own convention, minutes <= 0 means notifications are disabled
		if (minutes > 0) {
			am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + minutes*10*1000,
				minutes*60*1000, pi);
		}
		//*/
		Log.d("MainActivity","public void onResume() end");
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onBackPressed() {
		Log.d("MainActivity","public void onBackPressed called");


		super.onBackPressed();
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

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
		/*
		WorkoutManagerSingleton workoutManager = WorkoutManagerSingleton.getInstance();
		if(workoutManager.isDatabaseDeleted()){
			workoutManager.setDatabaseNotDeleted();
			startActivity(new Intent(this, MainActivity.class));
			this.finish();
		}
		//*/
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
			if(!tabId.equals(TAB_B)){
				int tabPosition = MainActivity.this.mTabHost.getCurrentTab();
				if(tabPosition != 1){
					FragmentManager manager = MainActivity.this.getSupportFragmentManager();
					for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {	
						manager.popBackStack();
					}
				}
			}
			if(tabId.equals(TAB_A)){
				pushFragments(getString(R.string.rss), fragment1rss);
			}else if(tabId.equals(TAB_B)){
				pushFragments(getString(R.string.worklog), fragment2worklog);
				int levelChoice = WorkoutManagerSingleton.getInstance().getLevelChoice();
				ListFragment nextFragment = null;
				if(levelChoice < 10){ // means we are in filling workout branch
					if(levelChoice > 0){
						nextFragment = new Fragment2worklog_2week();
						pushFragmentsRegisterInStack(nextFragment);
					}
					if(levelChoice > 1){
						nextFragment = new Fragment2worklog_3day();
						pushFragmentsRegisterInStack(nextFragment);
					}
					if(levelChoice > 2){
						nextFragment = new Fragment2worklog_4exercices();
						pushFragmentsRegisterInStack(nextFragment);
					}
				}else if(levelChoice < 20){ // means we are in the workout edition of a new or existing program branch
					
				}
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
	
	public void pushFragmentsRegisterInStack(Fragment fragment){
		FragmentManager manager = this.getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.replace(android.R.id.tabcontent, fragment);
		ft.addToBackStack(null);
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
		//* //todo test that

		//*/
		super.onSaveInstanceState(outState);
	}
}
