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
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class ExercisesAdapter extends BaseAdapter {

	protected Context context;
	protected ArrayList<HashMap<Integer, String>> data;
	protected LayoutInflater inflater;
	protected ListView listView;
	protected static Activity currentActivity = null;
	protected static ExercisesAdapter currentAdapter = null;
	
	public ExercisesAdapter(Context context,
			ArrayList<HashMap<Integer, String>> data){
		Log.d("ExercisesAdapter", "public ExercisesAdapter(…){ called");
		this.context = context;
		ExercisesAdapter.currentActivity = (Activity)this.context;
		ExercisesAdapter.currentAdapter = this;
		this.data = data;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d("ExercisesAdapter", "public ExercisesAdapter(…){ end");
	}
	
	public static Activity getCurrentActivity(){
		return ExercisesAdapter.currentActivity;
	}	
	
	public static ExercisesAdapter getCurrentAdapter(){
		return ExercisesAdapter.currentAdapter;
	}
	
	public static void cancelTimerEventually(){
		TimerRestButtonTask.cancelTimerEventually();
	}
	
	@Override
	public int getCount() {
		Log.d("ExercisesAdapter", "public int getCount()  called");
		int count = 0;
		if(data != null){
			count = data.size();
		}
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
		this.listView = (ListView)listView;
		//FrameLayout frameLayout = (FrameLayout)this.listView.getParent();
		//FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
		//params.setMargins(0, 0, 0, -50);
		//this.listView.setPadding(0, 0, 0, 50);
		//frameLayout.setLayoutParams(params);
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
		HashMap<Integer, String> row = this.data.get(position);
		int idExercise = Integer.parseInt(row.get(R.id.exerciseId));
		this.connectTimerButton(view, idExercise);
		this.colorRowIfExerciseDone(view);
		if(position == this.data.size()-1){
			//LayoutParams layoutParams = view.getLayoutParams();
			view.setPadding(0, 0, 0, 150);
			//layoutParams.ssetMargins(0, 0, 0, 100);
			//view.setLayoutParams(layoutParams);
		}
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
		Log.d("ExercisesAdapter", "value: " + value); 
		if(value.equals("") || Float.parseFloat(value) > 0.f){
			editText.setText(value);
		}
		editText.setOnFocusChangeListener(new OnWorkoutTextEditFocusChanged(position, view));
		Log.d("ExercisesAdapter", "protected void setWorkoutDataValue(…) end");
	}
	
	public void connectTimerButton(View view, int idExercise){
		Log.d("ExercisesAdapter", "Button button = (Button) view.findViewById(R.id.buttonRest);...");
		ImageButton button = (ImageButton) view.findViewById(R.id.buttonRest);
		OnClickListener restButtonOnClickListener
			= new OnRestButtonClickListener(idExercise);
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
	
	/*
	public int getCurrentRestValue(int position){
		HashMap<Integer, String> row = this.data.get(position);
		String restString = row.get(R.id.rest);
		int rest = 0;
		if(!restString.equals("")){
			rest = Integer.parseInt(restString);
		}
		return rest;
	}
	//*/
	
	public void setCurrentRestValue(int idExercise, int value){
		Log.d("ExercisesAdapter", "public void setCurrentRestValue(…) called");
		Log.d("ExercisesAdapter", "value: " + value);
		String valueString = "" + value;
		for(HashMap<Integer, String> row: this.data){
			String exerciseIdString = row.get(R.id.exerciseId);
			
			//if(exerciseIdString.equals(valueString)){
			if(Integer.parseInt(exerciseIdString) == idExercise){
				row.put(R.id.rest, valueString);
			}
		}
		//HashMap<Integer, String> row = this.data.get(position);
		//row.put(R.id.rest, "" + value);
		//int idExercise = Integer.parseInt(row.get(R.id.exerciseId));
		int nViews = this.listView.getCount();
		Log.d("ExercisesAdapter", "nViews: " + nViews);
		for(int i=0; i<nViews; i++){
			View view = listView.getChildAt(i);
			if(view != null){
				TextView textView = (TextView)view.findViewById(R.id.exerciseId);
				String currentIdExesciseText = textView.getText().toString();
				int currentIdExercise = -1;
				if(!currentIdExesciseText.equals("")){
					currentIdExercise = Integer.parseInt(currentIdExesciseText);
				}
				
				if(currentIdExercise == idExercise){
					Log.d("ExercisesAdapter", "Position rest value:: " + i);
					EditText restEditText = (EditText)view.findViewById(R.id.rest);
					restEditText.setText("" + value);
					Log.d("ExercisesAdapter", "Rest value set");
					break;
				}
			}
		}
		Log.d("ExercisesAdapter", "public void setCurrentRestValue(…) end");
	}
	
	protected class OnWorkoutTextEditFocusChanged implements View.OnFocusChangeListener{
		protected int position;
		protected View rowView;
		
		public OnWorkoutTextEditFocusChanged(int position, View rowView){
			this.position = position;
			this.rowView = rowView;
		}
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {								   // run when focus is lost
				String value = ((EditText) v).getText().toString();		 // get the value from the EditText
				View topParent = this.rowView;
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
				View mainLayout = (View)topParent.findViewById(R.id.mainLayout);
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
				workoutManager.saveLastProgram();
				
			}
		}
	}
	
	protected class OnRestButtonClickListener implements OnClickListener{
		//protected EditText restEditText;
		int idExercise;
		protected Boolean[] timerStarted;
		public OnRestButtonClickListener(int idExercise){ 
			Log.d("OnRestButtonClickListener", "public OnRestButtonClickListener(EditText restEditText){ called");
			this.idExercise = idExercise;
			this.timerStarted = new Boolean [] {Boolean.valueOf(false)};
			Log.d("OnRestButtonClickListener", "public OnRestButtonClickListener(EditText restEditText){ end");
		}

		@Override
		public void onClick(View view) {
			Log.d("ExerciseAdapter", "public void onClick(View view){ called");
			ViewParent parentView = view.getParent();
			
			while(!(parentView instanceof LinearLayout)
					&& ((View)parentView).getId() != R.id.mainLayout){
				parentView = parentView.getParent();
			}
			View viewRow = (View)parentView;
			viewRow.findViewById(R.id.hidenForFocus).requestFocus();
			EditText restEditText = (EditText)viewRow.findViewById(R.id.rest);
			String restText = restEditText.getText().toString();
			if(!restText.equals("")){
				int initialRestValue = Integer.parseInt(restText);
				if(initialRestValue > 0){
					if(!this.timerStarted[0]){
						Timer timer = new Timer();
						this.timerStarted[0] = Boolean.valueOf(true);
						TimerTask task = new TimerRestButtonTask(this.idExercise,
																initialRestValue,
																this.timerStarted);
						timer.scheduleAtFixedRate(task, 0, 1000);
					}
				}
			}
			Log.d("ExerciseAdapter", "public void onClick(View view){ end");
		}
	}

	static protected class TimerRestButtonTask extends TimerTask {
		//protected EditText restEditText;
		protected int idExercise;
		protected int initialRestValue;
		protected Boolean[] timerStarted;
		static protected TimerRestButtonTask lastTask = null;
		public static void cancelTimerEventually(){
			if(TimerRestButtonTask.lastTask != null){
				Log.d("TimerRestButtonTask", "Cancelling last task");
				ExercisesAdapter adapter = ExercisesAdapter.getCurrentAdapter();
				adapter.setCurrentRestValue(
						TimerRestButtonTask.lastTask.idExercise,
						TimerRestButtonTask.lastTask.initialRestValue);
				TimerRestButtonTask.lastTask.cancel();
			}
		}
		public TimerRestButtonTask(int idExercise,
									int initialRestValue,
									Boolean[] timerStarted){
			super();
			TimerRestButtonTask.cancelTimerEventually();
			TimerRestButtonTask.lastTask = this;
			this.idExercise = idExercise;
			this.initialRestValue = initialRestValue;
			RestButtonRunnable.currentRest = this.initialRestValue;
			this.timerStarted = timerStarted;
		}
		
		public void run() {
			Activity activity = (Activity)ExercisesAdapter.getCurrentActivity();
			Runnable runnable = new RestButtonRunnable(this,
														this.idExercise,
														this.initialRestValue,
														this.timerStarted);
			activity.runOnUiThread(runnable);
		}
	}

	static protected class RestButtonRunnable implements Runnable {
		//protected EditText restEditText;
		int idExercise;
		protected int initialRestValue;
		protected Boolean[] timerStarted;
		protected TimerTask timerTask;
		public static int currentRest;
		public RestButtonRunnable(TimerTask timerTask,
									int idExercise,
									int initialRestValue,
									Boolean[] timerStarted){
			Log.d("RestButtonRunnable", "public RestButtonRunnable(...){ called");
			this.timerTask = timerTask;
			this.idExercise = idExercise;
			this.initialRestValue = initialRestValue;
			this.timerStarted = timerStarted;
			/*
			View view = this.listView.getChildAt(this.position);
			if(view != null){
				EditText restEditText = (EditText)view.findViewById(R.id.rest);
				String restText = restEditText.getText().toString();
				Log.d("RestButtonRunnable", " rest :'" + restText + "'");
				this.currentRest = Integer.parseInt(restText); ///TODO adapter get rest value
			}
			//*/
			Log.d("RestButtonRunnable", "public RestButtonRunnable(...){ end");
		}

		@Override
		public void run() {
			Log.d("ExerciseAdapter", "public void run(){ called");
			RestButtonRunnable.currentRest--;
			ExercisesAdapter adapter = ExercisesAdapter.getCurrentAdapter();
			if(RestButtonRunnable.currentRest <= 0){
				this.playSound();
				adapter.setCurrentRestValue(this.idExercise, this.initialRestValue);
				this.timerStarted[0] = Boolean.valueOf(false);
				this.timerTask.cancel();
			}else{
				//if(view != null){
					//EditText restEditText = (EditText)view.findViewById(R.id.rest);
					//restEditText.setText("" + currentRest);
				//}
				adapter.setCurrentRestValue(this.idExercise, this.currentRest);
				//adapter.notifyDataSetInvalidated();
			}
			Log.d("ExerciseAdapter", "public void run(){ end");
		}
		
		protected void playSound(){
			Log.d("ExerciseAdapter", "protected void playSound(){ called");
			Context context = (Context)ExercisesAdapter.getCurrentActivity();
			MediaPlayer mediaPlayer = MediaPlayer.create(context,
					R.raw.power_up);
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.release();
				}
			});
			mediaPlayer.start();
			Vibrator vibrator;
			Activity activity = (Activity)context;
			vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(800);
			Log.d("ExerciseAdapter", "protected void playSound(){ end");
		}
		
	}
}
