package com.musclehack.musclehack;
/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;


public class SimpleHtmlAdapter extends BaseAdapter implements Filterable {
    private int[] mTo;
    private String[] mFrom;
    private ViewBinder mViewBinder;

    private List<? extends Map<String, ?>> mData;

    private int mResource;
    private int mDropDownResource;
    private LayoutInflater mInflater;

    private SimpleFilter mFilter;
    private ArrayList<Map<String, ?>> mUnfilteredData;


    public SimpleHtmlAdapter(Context context, List<? extends Map<String, ?>> data,
            int resource, String[] from, int[] to) {
        mData = data;
        mResource = mDropDownResource = resource;
        mFrom = from;
        mTo = to;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    

    public int getCount() {
        return mData.size();
    }


    public Object getItem(int position) {
        return mData.get(position);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView,
            ViewGroup parent, int resource) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(resource, parent, false);

            final int[] to = mTo;
            final int count = to.length;
            final View[] holder = new View[count];

            for (int i = 0; i < count; i++) {
                holder[i] = v.findViewById(to[i]);
            }

            v.setTag(holder);
        } else {
            v = convertView;
        }

        bindView(position, v);

        return v;
    }

    /**
     * <p>Sets the layout resource to create the drop down views.</p>
     *
     * @param resource the layout resource defining the drop down views
     * @see #getDropDownView(int, android.view.View, android.view.ViewGroup)
     */
    public void setDropDownViewResource(int resource) {
        this.mDropDownResource = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mDropDownResource);
    }

    private void bindView(int position, View view) {
        final Map dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }

        final ViewBinder binder = mViewBinder;
        final View[] holder = (View[]) view.getTag();
        final String[] from = mFrom;
        final int[] to = mTo;
        final int count = to.length;
        

        for (int i = 0; i < count; i++) {
            final View v = holder[i];
            if (v != null) {
                final Object data = dataSet.get(from[i]);
                String text = data == null ? "" : data.toString();
                if (text == null) {
                    text = "";
                }

                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, data, text);
                }
                
                if (!bound) {
                    if (v instanceof Checkable) {
                        if (data instanceof Boolean) {
                            ((Checkable) v).setChecked((Boolean) data);
                        } else {
                            throw new IllegalStateException(v.getClass().getName() +
                                    " should be bound to a Boolean, not a " + data.getClass());
                        }
                    } else if (v instanceof TextView) {
                        // Note: keep the instanceof TextView check at the bottom of these
                        // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                        setViewText((TextView) v, text);
                    } else if (v instanceof ImageView) {
                        if (data instanceof Integer) {
                            setViewImage((ImageView) v, (Integer) data);                            
                        } else {
                            setViewImage((ImageView) v, text);
                        }
                    } else if (v instanceof WebView) {
                    	WebView webView = (WebView) v;
                    	WebSettings webSettings = webView.getSettings();
                    	webSettings.setDefaultFontSize(20);
                    	webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
                    	webView.setInitialScale(100);
                    	webView.loadData(text, "text/html; charset=UTF-8", "UTF-8");
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a " +
                                " view that can be bounds by this SimpleAdapter");
                    }
                    if(v instanceof EditText){
                    	EditText editText = (EditText)v;
                    	editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    	    @Override
                    	    public void onFocusChange(View v, boolean hasFocus) {
                    	        if (!hasFocus) {                                   // run when focus is lost
                    	            String value = ((EditText) v).getText().toString();         // get the value from the EditText
                    	            //TextView textView = (TextView) ((LinearLayout)v.getParent()).findViewById(R.id.exerciseName);

                    	            LinearLayout topLayout = (LinearLayout)v.getParent().getParent();
                    	            LinearLayout firstLayout = (LinearLayout)topLayout.getChildAt(0);
                    	            LinearLayout secondLayout = (LinearLayout)topLayout.getChildAt(1);
                    	            TextView textView = (TextView)firstLayout.getChildAt(0);
                    	    		String exerciseName = textView.getText().toString();
                    	            textView = (TextView)firstLayout.getChildAt(1);
                    	    		String exerciseId = textView.getText().toString();
                    	    		EditText editText = (EditText) secondLayout.getChildAt(1);
                    	    		String restText = editText.getText().toString();
                    	    		//int rest = Integer.parseInt(restText);
                    	    		editText = (EditText) secondLayout.getChildAt(3);
                    	    		String weightText = editText.getText().toString();
                    	    		float weight = Float.parseFloat(weightText);
                    	    		editText = (EditText) secondLayout.getChildAt(5);
                    	    		String nRepsText = editText.getText().toString();
                    	    		int nReps = Integer.parseInt(nRepsText);
                    	    		boolean exerciseDone = weight > 0 && nReps > 0;
                    	    		if(exerciseDone){
                    	    			topLayout.setBackgroundColor(Color.CYAN);
                    	    		}else{
                    	    			topLayout.setBackgroundColor(Color.WHITE);
                    	    		}
                    	    		WorkoutManagerSingleton.getInstance().setExerciceInfo(exerciseId, restText, weightText, nRepsText);
                    	        }
                    	    }
                    	});
                    }
                }
            }
        }
        
        //*
        if(view instanceof LinearLayout && ((LinearLayout)view).getChildAt(1) instanceof LinearLayout){
            //LinearLayout secondLayout = (LinearLayout)view.findViewById(R.id.secondLayout);
    		//EditText editText = (EditText) view.findViewById(R.id.rest);
    		//String restText = editText.getText().toString();
    		EditText editText = (EditText) view.findViewById(R.id.weight);
    		String weightText = editText.getText().toString();
    		editText = (EditText) view.findViewById(R.id.nreps);
    		String nRepsText = editText.getText().toString();
    		boolean exerciseDone = false;
    		if(!weightText.equals("") && !nRepsText.equals("")){
        		float weight = Float.parseFloat(weightText);
        		int nReps = Integer.parseInt(nRepsText);
        		exerciseDone = weight > 0 && nReps > 0;
    		}
    		int backgroundColor = Color.WHITE; //TODO check it works whatever the theme, otherwise get the right color
    		if(exerciseDone){
    			backgroundColor = Color.CYAN;
    		}
    		LinearLayout mainLayout = (LinearLayout)view.findViewById(R.id.mainLayout);
    		mainLayout.setBackgroundColor(backgroundColor);
			//secondLayout.setBackgroundColor(backgroundColor);
			//secondLayout.getChildAt(1).setBackgroundColor(backgroundColor);
			//secondLayout.getChildAt(3).setBackgroundColor(backgroundColor);
			//secondLayout.getChildAt(5).setBackgroundColor(backgroundColor);
        }
        //*/
    }


    public ViewBinder getViewBinder() {
        return mViewBinder;
    }


    public void setViewBinder(ViewBinder viewBinder) {
        mViewBinder = viewBinder;
    }


    public void setViewImage(ImageView v, int value) {
        v.setImageResource(value);
    }


    public void setViewImage(ImageView v, String value) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException nfe) {
            v.setImageURI(Uri.parse(value));
        }
    }



    public void setViewText(TextView v, String text) {
    	if(v.getId() == R.id.text){
    		URLImageParser p = new URLImageParser(v, null);
    		
    		//Spanned htmlSpan = Html.fromHtml(text, p, null);
    		Spanned htmlSpan = Html.fromHtml(text, p, null);
    		v.setText(htmlSpan);
    	}else{
    		v.setText(text);
    	}
    }

    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new SimpleFilter();
        }
        return mFilter;
    }

    public static interface ViewBinder {
        boolean setViewValue(View view, Object data, String textRepresentation);
    }

    
    public class URLDrawable extends BitmapDrawable {
        // the drawable that you need to set, you could set the initial drawing
        // with the loading image if you need to
        protected Drawable drawable;

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if(drawable != null) {
                drawable.draw(canvas);
            }
        }
    }
    public class URLImageParser implements ImageGetter {
        Context c;
        View container;

        /***
         * Construct the URLImageParser which will execute AsyncTask and refresh the container
         * @param t
         * @param c
         */
        public URLImageParser(View t, Context c) {
            this.c = c;
            this.container = t;
        }

        public Drawable getDrawable(String source) {
            URLDrawable urlDrawable = new URLDrawable();

            // get the actual source
            ImageGetterAsyncTask asyncTask = 
                new ImageGetterAsyncTask( urlDrawable);

            asyncTask.execute(source);

            // return reference to URLDrawable where I will change with actual image from
            // the src tag
            return urlDrawable;
        }

        public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable>  {
            URLDrawable urlDrawable;

            public ImageGetterAsyncTask(URLDrawable d) {
                this.urlDrawable = d;
            }

            @Override
            protected Drawable doInBackground(String... params) {
                String source = params[0];
                return fetchDrawable(source);
            }

            @Override
            protected void onPostExecute(Drawable result) {
                // set the correct bound according to the result from HTTP call
            	//int width = result.getIntrinsicWidth();
            	//int height = result.getIntrinsicHeight();
//              //urlDrawable.setBounds(0, 0, 0, 0);
                //urlDrawable.setBounds(0, 0, width, height);


                urlDrawable.drawable = result;
                


                // redraw the image by invalidating the container
                URLImageParser.this.container.invalidate();
            }

            /***
             * Get the Drawable from URL
             * @param urlString
             * @return
             */
            public Drawable fetchDrawable(String urlString) {
                try {
                    InputStream is = fetch(urlString);
                    Drawable drawable = Drawable.createFromStream(is, "src");
                	//int width = drawable.getIntrinsicWidth();
                	//int height = drawable.getIntrinsicHeight();
                    //drawable.setBounds(0, 0, width, height);
                    return drawable;
                } catch (Exception e) {
                    return null;
                } 
            }

            private InputStream fetch(String urlString) throws MalformedURLException, IOException {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet request = new HttpGet(urlString);
                HttpResponse response = httpClient.execute(request);
                return response.getEntity().getContent();
            }
        }
    }

    
    private class SimpleFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mUnfilteredData == null) {
                mUnfilteredData = new ArrayList<Map<String, ?>>(mData);
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<Map<String, ?>> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<Map<String, ?>> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<Map<String, ?>> newValues = new ArrayList<Map<String, ?>>(count);

                for (int i = 0; i < count; i++) {
                    Map<String, ?> h = unfilteredValues.get(i);
                    if (h != null) {
                        
                        int len = mTo.length;

                        for (int j=0; j<len; j++) {
                            String str =  (String)h.get(mFrom[j]);
                            
                            String[] words = str.split(" ");
                            int wordCount = words.length;
                            
                            for (int k = 0; k < wordCount; k++) {
                                String word = words[k];
                                
                                if (word.toLowerCase().startsWith(prefixString)) {
                                    newValues.add(h);
                                    break;
                                }
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mData = (List<Map<String, ?>>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}