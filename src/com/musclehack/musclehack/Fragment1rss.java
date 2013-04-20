package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class Fragment1rss extends ListFragment {
	
	String[] countries = new String[] {
	        "India",
	        "Pakistan",
	        "Sri Lanka",
	        "China",
	        "Bangladesh",
	        "Nepal",
	        "Afghanistan",
	        "North Korea",
	        "South Korea",
	        "Japan"
	    };
	
    public static String TAG_TITLE = "title";
    public static String TAG_IMAGE = "image";
    public static String TAG_TEXT = "text";
	ArrayList<HashMap<String, String>> rssFeedList;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	/** Creating an array adapter to store the list of countries **/
    	rssFeedList = new ArrayList<HashMap<String, String>>();
    	HashMap<String, String> map = new HashMap<String, String>();
    	 
        // adding each child node to HashMap key => value
        
        map.put(TAG_TITLE, "Article title");
        map.put(TAG_IMAGE, "@drawable/tmp_calculette");
        map.put(TAG_TEXT, "This is the text of the article. It can be on more than one row and it doesn't matter...");
        rssFeedList.add(map);

        // adding HashList to ArrayList
        for(int i=0; i< 8; i++){
            map = new HashMap<String, String>();
            // adding each child node to HashMap key => value
            map.put(TAG_TITLE, "Article title 2");
            map.put(TAG_IMAGE, "tmp_calculette");
            map.put(TAG_TEXT, "This is the text of the article...");
            rssFeedList.add(map);
        }


        // adding HashList to ArrayList
        rssFeedList.add(map);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1,countries);
 
        /** Setting the list adapter for the ListFragment */
        ListAdapter adapter = new SimpleAdapter(this.getActivity(),
        										rssFeedList,
        										R.layout.fragment1rss_row,
        										new String[] { TAG_TITLE, TAG_IMAGE, TAG_TEXT },
        			                            new int[] { R.id.title, R.id.image_rss_article, R.id.text });
        setListAdapter(adapter);
 
        return super.onCreateView(inflater, container, savedInstanceState);
    
        
        //View view = inflater.inflate(R.layout.fragment1rss, container, false);
        
        //return view;
    }
}
