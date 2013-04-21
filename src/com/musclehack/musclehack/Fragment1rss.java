package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
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
        map.put(TAG_TEXT, "<a href=\"http://www.musclehack.com/stoopid-emails-april-2013/\"><img align=\"left\" hspace=\"5\" width=\"150\" height=\"150\" src=\"http://www.musclehack.com/wp-content/uploads/2013/04/Stupid-Emails-150x150.jpg\" class=\"alignleft wp-post-image tfe\" alt=\"Stupid Emails\" title=\"\" /></a>I decided to publish a selection of the craziest/weirdest/funniest emails I get on a monthly basis. They&amp;#8217;re just too too hilarious not to share. So here is the latest batch. Note: If you happen to see an email you sent here, sorry, but it was either daft, funny, or both. And we all need a [...]<img src=\"http://feeds.feedburner.com/~r/MuscleHack/~4/y1I-gJyu8iA\" height=\"1\" width=\"1\"/>");
        rssFeedList.add(map);

        // adding HashList to ArrayList
        for(int i=0; i< 8; i++){
            map = new HashMap<String, String>();
            // adding each child node to HashMap key => value
            map.put(TAG_TITLE, "Article title 2");
            map.put(TAG_TEXT, "<h1>This is the text of the article...</h1>");
            rssFeedList.add(map);
        }


        // adding HashList to ArrayList
        rssFeedList.add(map);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1,countries);
 
        /** Setting the list adapter for the ListFragment */
        ListAdapter adapter = new SimpleHtmlAdapter(this.getActivity(),
        										rssFeedList,
        										R.layout.fragment1rss_row,
        										new String[] { TAG_TITLE, TAG_TEXT },
        			                            new int[] { R.id.title, R.id.text });
        setListAdapter(adapter);
 
        return super.onCreateView(inflater, container, savedInstanceState);
    
        
        //View view = inflater.inflate(R.layout.fragment1rss, container, false);
        
        //return view;
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
    	Uri uri = Uri.parse("http://www.musclehack.com");
    	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    	startActivity(intent);
    }
}
