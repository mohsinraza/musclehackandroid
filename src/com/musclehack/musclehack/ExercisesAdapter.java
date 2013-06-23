package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class ExercisesAdapter extends BaseAdapter {

	protected Context context;
	protected ArrayList<HashMap<Integer, String>> data;
	protected LayoutInflater inflater;
	
	public ExercisesAdapter(Context context,
			ArrayList<HashMap<Integer, String>> data){
		Log.d("ExercisesAdapter", "public ExercisesAdapter(…){ called");
		this.context = context;
		this.data = data;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d("ExercisesAdapter", "public ExercisesAdapter(…){ end");
	}
	
	@Override
	public int getCount() {
		Log.d("ExercisesAdapter", "public int getCount()  called");
		int count = data.size();
		Log.d("ExercisesAdapter", "public int getCount()  end");
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		Log.d("ExercisesAdapter", "public Object getItem(int arg0) called");
		Object item = data.get(0);
		Log.d("ExercisesAdapter", "public Object getItem(int arg0) end");
		return item;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup listView) {
		Log.d("ExercisesAdapter", "public View getView(…) called");
		if(view == null){
			view = this.inflater.inflate(R.layout.fragment2worklog_exercise,
											listView,
											false);
		}
		this.setValue(position, view, R.id.exerciseName);
		this.setValue(position, view, R.id.exerciseNumber);
		this.setValue(position, view, R.id.exerciseId);
		this.setValue(position, view, R.id.range);
		this.setWorkoutDataValue(position, view, R.id.rest);
		this.setWorkoutDataValue(position, view, R.id.weight);
		this.setWorkoutDataValue(position, view, R.id.nreps);
		this.setValue(position, view, R.id.previousRest);
		this.setValue(position, view, R.id.previousWeight);
		this.setValue(position, view, R.id.previousNreps);
		this.connectTimerButton(view);
		this.colorRowIfExerciseDone(view);
		Log.d("ExercisesAdapter", "public View getView(…) end");
		return view;
	}
	
	protected void setValue(int position, View view, int id) {
		Log.d("ExercisesAdapter", "protected void setValue(…) called");
		HashMap<Integer, String> row = this.data.get(position);
		TextView textView = (TextView)
				view.findViewById(id);
		String value = row.get(id);
		textView.setText(value);
		Log.d("ExercisesAdapter", "protected void setValue(…) end");
	}
	
	protected void setWorkoutDataValue(int position, View view, int id) {
		Log.d("ExercisesAdapter", "protected void setWorkoutDataValue(…) called");
		HashMap<Integer, String> row = this.data.get(position);
		EditText editText = (EditText)
				view.findViewById(id);
		String value = row.get(id);
		if(Float.parseFloat(value) > 0.f){
			editText.setText(value);
		}
		editText.setOnFocusChangeListener(new OnWorkoutTextEditFocusChanged(position));
		Log.d("ExercisesAdapter", "protected void setWorkoutDataValue(…) end");
	}
	
	public void connectTimerButton(View view){
		Log.d("ExercisesAdapter", "Button button = (Button) view.findViewById(R.id.buttonRest);...");
		ImageButton button = (ImageButton) view.findViewById(R.id.buttonRest);
		EditText restEditText = (EditText)view.findViewById(R.id.rest);
		OnClickListener restButtonOnClickListener = new OnRestButtonClickListener(restEditText);
		button.setOnClickListener(restButtonOnClickListener);
	}

	public void colorRowIfExerciseDone(View view){
		Log.d("ExercisesAdapter", "public void colorRowIfExerciseDone(…) called");
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
		View mainLayout = (View)view.findViewById(R.id.mainLayout);
		mainLayout.setBackgroundColor(backgroundColor);
		Log.d("ExercisesAdapter", "public void colorRowIfExerciseDone(…) end");
	}
	
	protected class OnWorkoutTextEditFocusChanged implements View.OnFocusChangeListener{
		protected int position;
		
		public OnWorkoutTextEditFocusChanged(int position){
			this.position = position;
		}
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {								   // run when focus is lost
				String value = ((EditText) v).getText().toString();		 // get the value from the EditText
				View topParent = (View)v.getParent();
				try{
					while(topParent.getParent() != null){
						topParent = (View)topParent.getParent();
					}
				}catch(ClassCastException e){
				}
				Log.d("ExercisesAdapter", "topParent id:" + topParent.getId() +", main id:" + R.id.mainLayout);
				TextView textView = (TextView)topParent.findViewById(R.id.exerciseId);
				String exerciseId = textView.getText().toString();
				EditText editText = (EditText)topParent.findViewById(R.id.rest);
				String restText = editText.getText().toString();
				editText = (EditText) topParent.findViewById(R.id.weight);
				String weightText = editText.getText().toString();
				editText = (EditText) topParent.findViewById(R.id.nreps);
				String nRepsText = editText.getText().toString();
				boolean exerciseDone = false;
				if(!weightText.equals("") && !nRepsText.equals("")){
					float weight = Float.parseFloat(weightText);
					int nReps = Integer.parseInt(nRepsText);
					exerciseDone = weight > 0 && nReps > 0;
				}
				View mainLayout = (View)v.getParent().getParent().getParent().getParent();
				if(exerciseDone){
					mainLayout.setBackgroundColor(Color.CYAN);
				}else{
					mainLayout.setBackgroundColor(Color.WHITE);
				}
				int currentTextEditId = v.getId();
				HashMap<Integer, String> row
						= ExercisesAdapter.this.data.get(this.position);
				row.put(currentTextEditId, value);
				WorkoutManagerSingleton workoutManager = WorkoutManagerSingleton.getInstance();
				Log.d("ExerciseAdapter", "exerciseId id:" + topParent.getId() +", main id:" + R.id.mainLayout);
				workoutManager.setExerciceInfo(exerciseId,
												restText,
												weightText,
												nRepsText);
				workoutManager.saveLastSubWorkout();
			}
		}
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
		protected int currentRest;
		public RestButtonRunnable(TimerTask timerTask,
									EditText restEditText,
									int initialRestValue,
									Boolean[] timerStarted){
			Log.d("RestButtonRunnable", "public RestButtonRunnable(...){ called");
			this.timerTask = timerTask;
			this.restEditText = restEditText;
			this.initialRestValue = initialRestValue;
			this.timerStarted = timerStarted;
			String restText = this.restEditText.getText().toString();
			this.currentRest = Integer.parseInt(restText);
			Log.d("RestButtonRunnable", "public RestButtonRunnable(...){ end");
		}

		@Override
		public void run() {
			Log.d("SimpleExerciseAdapter", "public void run(){ called");
			this.currentRest--;
			if(this.currentRest <= 0){
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
			Vibrator vibrator;
			Activity activity = (Activity)this.restEditText.getContext();
			vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(800);
			Log.d("SimpleExerciseAdapter", "protected void playSound(){ end");
		}
		
	}
}
