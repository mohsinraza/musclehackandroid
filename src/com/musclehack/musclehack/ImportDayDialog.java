package com.musclehack.musclehack;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.musclehack.musclehack.workouts.Day;
import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;



public class ImportDayDialog extends DialogFragment {
	protected View view;
	protected Context context;
	protected CustomizeDayAdapter customizeDayAdapter;
	protected Day dayToCreate;
	protected static String dontImport = "Don't import";
	static protected ProgressDialog progressDialog = null;
	protected CreateDayAsyncTask createDayAsyncTask;

	public void init(
			CustomizeDayAdapter adapter,
			Day dayToCreate,
			Context context){
		this.dayToCreate = dayToCreate;
		this.context = context;
		this.customizeDayAdapter = adapter;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d("ImportDayDialog", "onConfigurationChanged() Called");
		this.cancel();
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("ImportDayDialog", "public View onCreateView(...) called");
		this.view = inflater.inflate(
				R.layout.import_exercises_in_day,
				container);
		getDialog().setTitle("Create day"); //TODO translate
		this._fillSpinner(view);
		this._connectButtons(view);
		Log.d("ImportDayDialog", "public View onCreateView(...) end");
		return this.view;
	}
	
	public void _fillSpinner(View view){
		Log.d("ImportDayDialog", "public void _fillSpinner(...) called");
		List<String> dayNames =
				WorkoutManagerSingleton
				.getInstance().getAvailableWorkoutNames();
		dayNames.add(0, ImportDayDialog.dontImport);
		Spinner spinner = (Spinner)
				view.findViewById(R.id.spinnerDay);
		spinner.setVisibility(View.VISIBLE);
		Context context = this.getActivity();
		ArrayAdapter adapter = new ArrayAdapter<String>(context,
				R.layout.small_spinner,
				dayNames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		Log.d("ImportDayDialog", "public void _fillSpinner(...) end");
	}
	
	public void _connectButtons(View view){
		Log.d("ImportDayDialog", "public void _connectButtons(...) called");
		Button importButton
		= (Button)view.findViewById(
				R.id.buttonOkImport);
		OnClickListener importButtonListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ImportDayDialog.this.importDay();
			}
			
		};
		importButton.setOnClickListener(importButtonListener);

		
		Button cancelButton
		= (Button)view.findViewById(
				R.id.buttonCancel);
		OnClickListener cancelButtonListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ImportDayDialog.this.cancel();
			}
			
		};
		cancelButton.setOnClickListener(cancelButtonListener);
	}
	
	public void importDay(){
		Log.d("ImportDayDialog", "public void importDay(...) called");
		WorkoutManagerSingleton workoutManager
		= WorkoutManagerSingleton.getInstance();
		EditText editText
		= (EditText) this.view.findViewById(
				R.id.editTextWorkoutName);
		Log.d("ImportDayDialog", "edit text got");
		String workoutName = editText.getText().toString();
		List<String> dayNames =
				workoutManager.getAvailableWorkoutNames();
		if(dayNames.contains(workoutName)){
			Activity activity
			= (Activity)this.context;
			Log.d("ImportDayDialog", "dayNames.contains(workoutName)");
			//Resources res = activity.getResources();
			new AlertDialog.Builder(activity)
			.setTitle("Workout name")
			.setMessage("This name is already used.")
			.setNeutralButton("Ok",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			})
			.show();
		}else if(workoutName.equals("")){ //TODO check name available and add day eventually
			Activity activity
			= (Activity)this.context;
			Log.d("ImportDayDialog", "activity got");
			//Resources res = activity.getResources();
			new AlertDialog.Builder(activity)
			.setTitle("Workout name")
			.setMessage("You have to type a workout name!")
			.setNeutralButton("Ok",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			})
			.show();
		}else{
			Activity activity = (Activity)this.context;
			ImportDayDialog.progressDialog
			= ProgressDialog.show(activity,
									"",
									"Saving workout",
									true);
			this.createDayAsyncTask = new CreateDayAsyncTask();
			this.createDayAsyncTask.execute(workoutName);
			Log.d("ImportDayDialog", "public void importDay(...) end");
		}
	}
	
	protected class CreateDayAsyncTask
	extends AsyncTask<String, Void, Void> {
		protected String workoutName;
		@Override
		protected Void doInBackground(String... data) {
			Log.d("ImportDayDialog task", "protected Void doInBackground(...) called");
			this.workoutName = data[0];
			WorkoutManagerSingleton workoutManager
			= WorkoutManagerSingleton.getInstance();
			int dayOfTheWeek = ImportDayDialog.this.dayToCreate
					.getDayOfTheWeek();
			Spinner spinner = (Spinner)ImportDayDialog.this.view
					.findViewById(R.id.spinnerDay);
			String fromDay
			= spinner.getSelectedItem().toString();
			if(fromDay.equals(ImportDayDialog.dontImport)){
				workoutManager.createDay(
						this.workoutName,
						dayOfTheWeek); //TODO check we have a selected program name
			}else{
				workoutManager.createDayFromExistingOne(
						this.workoutName,
						dayOfTheWeek,
						fromDay);
			}
			
			Log.d("ImportDayDialog task", "protected Void doInBackground(...) end");
			return null;
		}
		


		@Override
		protected void onPostExecute(Void nothing) { 
			Log.d("ImportDayDialog task", "protected List<String> doInBackground(Void... urls) called");
			if(ImportDayDialog.progressDialog != null){
				ImportDayDialog.progressDialog.dismiss();
				ImportDayDialog.this.customizeDayAdapter.setEditingDayWorkoutName(this.workoutName);
				ImportDayDialog.this.dismiss();
			}
			Log.d("ImportDayDialog task", "protected void onPostExecute(...) end");
		}
	}
	
	@Override
	public void onDestroyView(){
		Log.d("ImportDayDialog", "public void onDestroyView() called");
		
		
		super.onDestroyView();
		Log.d("ImportDayDialog", "public void onDestroyView() end");
	}
	
	public void cancel(){
		Log.d("ImportDayDialog", "public void cancel() called");
		if(ImportDayDialog.progressDialog != null){
			ImportDayDialog.progressDialog.dismiss();
			ImportDayDialog.progressDialog = null;
		}
		if(this.createDayAsyncTask != null){
			try {
				this.createDayAsyncTask.get(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.createDayAsyncTask.cancel(false);
			Log.d("ImportDayDialog", "createDayAsyncTask canceled.");
			this.createDayAsyncTask = null;
		}else{
			this.customizeDayAdapter.setCheckLastPosition(false);
		}
		this.dismiss();
		Log.d("ImportDayDialog", "public void cancel() end");
	}
}
