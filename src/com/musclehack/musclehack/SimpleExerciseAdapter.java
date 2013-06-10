package com.musclehack.musclehack;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageButton;
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
		Log.d("SimpleExerciseAdapter", "protected void bindView(int position, View view) called");
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

									//LinearLayout topLayout = (LinearLayout)v.getParent().getParent();
									//
									View topParent = (View)v.getParent();
									try{
										while(topParent.getParent() != null){
											topParent = (View)topParent.getParent();
										}
									}catch(ClassCastException e){
									}
									Log.d("SimpleExerciseAdapter", "topPArent id:" + topParent.getId() +", main id:" + R.id.mainLayout);
									TextView textView = (TextView)topParent.findViewById(R.id.exerciseId);
									String exerciseId = textView.getText().toString();
									EditText editText = (EditText)topParent.findViewById(R.id.rest);
									String restText = editText.getText().toString();
									editText = (EditText) topParent.findViewById(R.id.weight);
									String weightText = editText.getText().toString();
									float weight = Float.parseFloat(weightText);
									editText = (EditText) topParent.findViewById(R.id.nreps);
									String nRepsText = editText.getText().toString();
									int nReps = Integer.parseInt(nRepsText);
									boolean exerciseDone = weight > 0 && nReps > 0;
									RelativeLayout mainLayout = (RelativeLayout)v.getParent().getParent();
									if(exerciseDone){
										mainLayout.setBackgroundColor(Color.CYAN);
									}else{
										mainLayout.setBackgroundColor(Color.WHITE);
									}
									WorkoutManagerSingleton workoutManager = WorkoutManagerSingleton.getInstance();
									workoutManager.setExerciceInfo(exerciseId,
																	restText,
																	weightText,
																	nRepsText);
									workoutManager.saveLastSubWorkout();
								}else{
									EditText editText = (EditText) v;
									editText.selectAll();
									/*
									String value = editText.getText().toString();
									if(Float.parseFloat(value) == 0.f){
										editText.setText("");
									}
									//*/
								}
							}
						});
					}
				}
			}
		}
		
		Log.d("SimpleExerciseAdapter", "Button button = (Button) view.findViewById(R.id.buttonRest);...");
		ImageButton button = (ImageButton) view.findViewById(R.id.buttonRest);
		Log.d("SimpleExerciseAdapter", "ok 1");
		View tmp = view.findViewById(R.id.rest);
		Log.d("SimpleExerciseAdapter", "ok 2");
		EditText restEditText = (EditText)view.findViewById(R.id.rest);
		Log.d("SimpleExerciseAdapter", "ok 3");
		OnClickListener restButtonOnClickListener = new OnRestButtonClickListener(restEditText);
		button.setOnClickListener(restButtonOnClickListener);
		

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
		LinearLayout mainLayout = (LinearLayout)view.findViewById(R.id.mainLayout);
		mainLayout.setBackgroundColor(backgroundColor);
		Log.d("SimpleExerciseAdapter", "protected void bindView(int position, View view) end");
		//secondLayout.setBackgroundColor(backgroundColor);
		//secondLayout.getChildAt(1).setBackgroundColor(backgroundColor);
		//secondLayout.getChildAt(3).setBackgroundColor(backgroundColor);
		//secondLayout.getChildAt(5).setBackgroundColor(backgroundColor);

	}
	protected class OnRestButtonClickListener implements OnClickListener{
		protected EditText restEditText;
		protected int initialRestValue;
		protected Boolean[] timerStarted;
		public OnRestButtonClickListener(EditText restEditText){
			Log.d("SimpleExerciseAdapter", "public OnRestButtonClickListener(EditText restEditText){ called");
			this.restEditText = restEditText;
			this.timerStarted = new Boolean [] {Boolean.valueOf(false)};
			String restText = this.restEditText.getText().toString();
			if(!restText.equals("")){
				this.initialRestValue = Integer.parseInt(restText);
			}else{
				this.initialRestValue = 0;
			}
			Log.d("SimpleExerciseAdapter", "public OnRestButtonClickListener(EditText restEditText){ end");
		}

		@Override
		public void onClick(View view) {
			Log.d("SimpleExerciseAdapter", "public void onClick(View view){ called");
			if(this.initialRestValue > 0){
				if(!this.timerStarted[0]){
					Timer timer = new Timer();
					this.timerStarted[0] = Boolean.valueOf(true);
					TimerTask task = new TimerRestButtonTask(this.restEditText,
															this.initialRestValue,
															this.timerStarted);
					timer.scheduleAtFixedRate(task, 0, 1000);
				}
			}
			Log.d("SimpleExerciseAdapter", "public void onClick(View view){ end");
		}
	}

	static protected class TimerRestButtonTask extends TimerTask {
		protected EditText restEditText;
		protected int initialRestValue;
		protected Boolean[] timerStarted;
		static protected TimerRestButtonTask lastTask = null;
		public TimerRestButtonTask(EditText restEditText,
									int initialRestValue,
									Boolean[] timerStarted){
			super();
			if(TimerRestButtonTask.lastTask != null){
				TimerRestButtonTask.lastTask.cancel();
			}
			TimerRestButtonTask.lastTask = this;
			this.restEditText = restEditText;
			this.initialRestValue = initialRestValue;
			this.timerStarted = timerStarted;
		}
		
		public void run() {
			Activity activity = (Activity)this.restEditText.getContext();
			Runnable runnable = new RestButtonRunnable(this,
														this.restEditText,
														this.initialRestValue,
														this.timerStarted);
			activity.runOnUiThread(runnable);
		}
	}

	static protected class RestButtonRunnable implements Runnable {
		protected EditText restEditText;
		protected int initialRestValue;
		protected Boolean[] timerStarted;
		protected TimerTask timerTask;
		public RestButtonRunnable(TimerTask timerTask,
									EditText restEditText,
									int initialRestValue,
									Boolean[] timerStarted){
			Log.d("RestButtonRunnable", "public RestButtonRunnable(...){ called");
			this.timerTask = timerTask;
			this.restEditText = restEditText;
			this.initialRestValue = initialRestValue;
			this.timerStarted = timerStarted;
			Log.d("RestButtonRunnable", "public RestButtonRunnable(...){ end");
		}
		@Override
		public void run() {
			Log.d("SimpleExerciseAdapter", "public void run(){ called");
			String restText = this.restEditText.getText().toString();
			int currentRest = Integer.parseInt(restText);
			currentRest--;
			if(currentRest <= 0){
				this.playSound();
				this.restEditText.setText("" + this.initialRestValue);
				this.timerStarted[0] = Boolean.valueOf(false);
				this.timerTask.cancel();
			}else{
				this.restEditText.setText("" + currentRest);
			}
			Log.d("SimpleExerciseAdapter", "public void run(){ end");
		}
		
		protected void playSound(){
			Log.d("SimpleExerciseAdapter", "protected void playSound(){ called");
			MediaPlayer mediaPlayer = MediaPlayer.create(restEditText.getContext(),
					R.raw.power_up);
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.release();
				}
			});
			mediaPlayer.start();
			Log.d("SimpleExerciseAdapter", "protected void playSound(){ end");
		}
		
	}
}