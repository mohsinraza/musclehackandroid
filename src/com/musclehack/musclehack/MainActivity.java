package com.musclehack.musclehack;

import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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
	static String TAB_A = "Rss Tab";
	static String TAB_B = "Workout log Tab";
	static String TAB_C = "Testimonials Tab";
	static String TAB_D = "Recipes Tab";
	static String TAB_E = "Archives Tab";
	static String TAB_F = "Book Tab";

	TabHost mTabHost;
	
	FragmentListFeed fragment1rss;
	Fragment2worklog fragment2worklog;
	FragmentListFeed fragment3testimonials;
	FragmentListFeed fragment4recipe;
	Fragment4archives fragment5archives;
	Fragment6book fragment6book;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		this.fragment1rss = new FragmentListFeed();
		this.fragment1rss.setUrlFeed("http://feeds.feedburner.com/MuscleHack");
		this.fragment2worklog = new Fragment2worklog();
		this.fragment3testimonials = new FragmentListFeed();
		this.fragment3testimonials.setUrlFeed("http://www.musclehack.com/category/testimonials-2/feed");
		this.fragment4recipe = new FragmentListFeed();
		this.fragment4recipe.setUrlFeed("http://www.musclehack.com/category/recipes/feed");
		this.fragment5archives = new Fragment4archives();
		this.fragment6book = new Fragment6book();
		/*
		this.fragment1rss.setRetainInstance(true);
		this.fragment2worklog.setRetainInstance(true);
		this.fragment3testimonials.setRetainInstance(true);
		this.fragment4recipe.setRetainInstance(true);
		this.fragment5archives.setRetainInstance(true);
		this.fragment6book.setRetainInstance(true);
		//*/
		
		this.mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		this.mTabHost.setOnTabChangedListener(this.listener);
		this.mTabHost.setup();
		
		
		this.initializeTab(savedInstanceState);
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
	public void initializeTab(Bundle savedInstanceState) {
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
		
		if (savedInstanceState == null) {
			this.mTabHost.setCurrentTab(-3);
		}else{
			int tabPosition = savedInstanceState.getInt("TAB_POSITION");
			this.mTabHost.setCurrentTab(tabPosition);
		}
	}
	
	@Override
	public void onBackPressed() {
		int levelChoice = WorkoutManagerSingleton.getInstance().getLevelChoice();
		if(levelChoice == 1){
			WorkoutManagerSingleton.getInstance().setLevelChoice(0);
		}
		super.onBackPressed();
	}
	/*
	 * TabChangeListener for changing the tab when one of the tabs is pressed
	 */
	TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {
		  public void onTabChanged(String tabId) {
			  if(tabId.equals(TAB_A)){
				pushFragments(getString(R.string.worklog), fragment1rss);
			  }else if(tabId.equals(TAB_B)){
				  pushFragments(getString(R.string.rss), fragment2worklog);
			  }else if(tabId.equals(TAB_C)){
				  pushFragments(getString(R.string.testimonials), fragment3testimonials);
			  }else if(tabId.equals(TAB_D)){
				  pushFragments(getString(R.string.recipes), fragment4recipe);
			  }else if(tabId.equals(TAB_E)){
				  pushFragments(getString(R.string.archives), fragment5archives);
			  }else if(tabId.equals(TAB_F)){
				  pushFragments(getString(R.string.book), fragment6book);
			  }
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
	
	/*
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen for landscape and portrait and set portrait mode always
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
	//*/
		
}
