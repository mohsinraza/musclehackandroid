package com.musclehack.musclehack;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;



public class SimpleExerciseAdapter extends SimpleCustomableAdapter {
	public SimpleExerciseAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
	}

	
	protected void bindView(int position, View view) {
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
								if (!hasFocus) {								   // run when focus is lost
									String value = ((EditText) v).getText().toString();		 // get the value from the EditText
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
									WorkoutManagerSingleton workoutManager = WorkoutManagerSingleton.getInstance();
									workoutManager.setExerciceInfo(exerciseId,
																	restText,
																	weightText,
																	nRepsText);
									workoutManager.saveLastSubWorkout();
								}
							}
						});
					}
				}
			}
		}
		
		//*
		//if(view instanceof LinearLayout && ((LinearLayout)view).getChildAt(1) instanceof LinearLayout){
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
		RelativeLayout mainLayout = (RelativeLayout)view.findViewById(R.id.mainLayout);
		mainLayout.setBackgroundColor(backgroundColor);
		//secondLayout.setBackgroundColor(backgroundColor);
		//secondLayout.getChildAt(1).setBackgroundColor(backgroundColor);
		//secondLayout.getChildAt(3).setBackgroundColor(backgroundColor);
		//secondLayout.getChildAt(5).setBackgroundColor(backgroundColor);

	}
}