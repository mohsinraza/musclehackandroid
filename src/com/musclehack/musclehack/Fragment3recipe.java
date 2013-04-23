package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;


public class Fragment3recipe extends ListFragment {
	
	public static String TAG_TITLE = "title";
	public static String TAG_TEXT = "text";
	protected ArrayList<HashMap<String, String>> rssFeedList;
	protected ListAdapter adapter;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//*
		rssFeedList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		 
		// adding each child node to HashMap key => value
		map.put(TAG_TITLE, "Recipe 1");
		map.put(TAG_TEXT, "<a href=\"http://www.musclehack.com/stoopid-emails-april-2013/\"><img align=\"left\" hspace=\"5\" width=\"150\" height=\"150\" src=\"http://static.spoonful.com/sites/default/files/styles/square_128x128/public/recipes/edible-eyeball-treats-halloween-recipe-photo-420-FF1005TRICKA07.jpg\" class=\"alignleft wp-post-image tfe\" alt=\"Stupid Emails\" title=\"\" /></a>A recipe with an image.");
		rssFeedList.add(map);
		for(int i=0; i< 8; i++){
			map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put(TAG_TITLE, "Recipe " + (i+2));
			map.put(TAG_TEXT, "<p>This is the description of the recipe...</p>");
			rssFeedList.add(map);
		}
		rssFeedList.add(map);

		this.adapter = new SimpleHtmlAdapter(this.getActivity(),
												rssFeedList,
												R.layout.fragment3recipe_row,
												new String[] { TAG_TITLE, TAG_TEXT },
												new int[] { R.id.titleRecipe, R.id.webViewRecipe});
		setListAdapter(this.adapter);
		/**/
 
		
		
		return super.onCreateView(inflater, container, savedInstanceState);
	
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		//RssItem item = this.entries.get((int)id);
		//Uri uri = Uri.parse(item.link);
		//Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		//startActivity(intent);
	}
}
