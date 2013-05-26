package com.musclehack.musclehack;
/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.musclehack.musclehack.images.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;



public class SimpleCustomableAdapter extends BaseAdapter implements Filterable {
	protected int[] mTo;
	protected String[] mFrom;
	protected ViewBinder mViewBinder;

	protected List<? extends Map<String, ?>> mData;

	protected int mResource;
	protected int mDropDownResource;
	protected LayoutInflater mInflater;

	protected SimpleFilter mFilter;
	protected ArrayList<Map<String, ?>> mUnfilteredData;

	public ImageLoader imageLoader; 
	
	public SimpleCustomableAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		Log.d("SimpleCustomableAdapter", "public SimpleCustomableAdapter(...) called");
		mData = data;
		mResource = mDropDownResource = resource;
		mFrom = from;
		mTo = to;
		this.imageLoader = new ImageLoader(context);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d("SimpleCustomableAdapter", "public SimpleCustomableAdapter(...) end");
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
		Log.d("SimpleCustomableAdapter", "public View getView(int position, View convertView, ViewGroup parent)...");
		return createViewFromResource(position, convertView, parent, mResource);
	}

	private View createViewFromResource(int position, View convertView,
			ViewGroup parent, int resource) {
		Log.d("SimpleCustomableAdapter", "private View createViewFromResource(...) called");
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
		Log.d("SimpleCustomableAdapter", "private View createViewFromResource(...) end");
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
		Log.d("SimpleCustomableAdapter", "public View getDropDownView(...) ...return");
		return createViewFromResource(position, convertView, parent, mDropDownResource);
	}

	protected void bindView(int position, View view) {
		Log.d("SimpleCustomableAdapter", "protected void bindView(int position, View view) called");
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
						setViewHtml((WebView)v, text);
					} else {
						throw new IllegalStateException(v.getClass().getName() + " is not a " +
								" view that can be bounds by this SimpleAdapter");
					}
				}
			}
		}
		Log.d("SimpleCustomableAdapter", "protected void bindView(int position, View view) end");
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


	public void setViewImage(ImageView imageView, String value) {
		Log.d("SimpleCustomableAdapter", "public void setViewImage(ImageView imageView, String value) called");
		try {
			imageView.setImageResource(Integer.parseInt(value));
		} catch (NumberFormatException nfe) {
			//v.setImageURI(Uri.parse(value));
			//String imageUrl ="http://ioe.edu.np/exam/notices/8560Result Diploma I_I.jpg";
			String imageUrl = value;
			imageLoader.DisplayImage(value, imageView);
			/*
			SetImageAsyncTask setImageAsyncTask = new SetImageAsyncTask(imageView);
			setImageAsyncTask.execute(imageUrl);
			//*/
		}
		Log.d("SimpleCustomableAdapter", "public void setViewImage(ImageView imageView, String value) end");
	}

	public class SetImageAsyncTask extends AsyncTask<String, Void, Bitmap>  {
		ImageView imageView;

		public SetImageAsyncTask(ImageView imageView) {
			this.imageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String imageUrl = params[0];
			URL url;
			Bitmap bmp = null;
			try {
				if(true){
				url = new URL(imageUrl);
				bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
				//this.imageView.setImageBitmap(bmp);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return bmp;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// set the correct bound according to the result from HTTP call
			//int width = result.getIntrinsicWidth();
			//int height = result.getIntrinsicHeight();
//		  //urlDrawable.setBounds(0, 0, 0, 0);
			//urlDrawable.setBounds(0, 0, width, height);
			this.imageView.setImageBitmap(result);
		}

	}

	public void setViewHtml(WebView v, String value) {
		WebView webView = (WebView) v;
		WebSettings webSettings = webView.getSettings();
		webSettings.setDefaultFontSize(20);
		webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		webView.setInitialScale(100);
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
//			  //urlDrawable.setBounds(0, 0, 0, 0);
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

	
	protected class SimpleFilter extends Filter {

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
