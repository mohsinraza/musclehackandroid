package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.musclehack.musclehack.CustomizeExerciseAdapter.SaveAsyncTask;
import com.musclehack.musclehack.workouts.Exercice;
import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class Fragment2customize2name extends Fragment {

	protected boolean fromExistingProgramMode;
	protected View mainView;
	static protected ProgressDialog progressDialog = null;
	protected SaveAndContinueAsyncTask saveAsyncTask;
	
	public Fragment2customize2name(){
		super();
		this.fromExistingProgramMode = false;
		this.mainView = null;
	}

	
	public void enableExistingProgramMode(){
		this.fromExistingProgramMode = true;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
								Bundle savedInstanceState) {
		this.mainView = inflater.inflate(R.layout.create_workout,
				container, false);
		this.fillSpinnerEventually(this.mainView);
		this.connectButton(this.mainView);
		if(this.fromExistingProgramMode){
			WorkoutManagerSingleton.getInstance().setLevelChoice(13);
		}else{

			WorkoutManagerSingleton.getInstance().setLevelChoice(12);
		}
		return this.mainView;
	}
	
	public void fillSpinnerEventually(View view){
		Log.d("Fragment2customize2name task", "public void fillSpinnerEventually(...) called");
		if(this.fromExistingProgramMode){
			List<String> programNames =
					WorkoutManagerSingleton
					.getInstance().getAvailableProgramNames();
			Spinner spinner = (Spinner)
					view.findViewById(R.id.spinnerProgramNames2);
			spinner.setVisibility(View.VISIBLE);
			//View program = 
					//view.findViewById(R.id.textViewProgram);
			//program.setVisibility(View.VISIBLE);
			Context context = this.getActivity();
			ArrayAdapter adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_list_item_1,
					programNames);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		}
		Log.d("Fragment2customize2name task", "public void fillSpinnerEventually(...) end");
	}
	public void connectButton(View view){
		Button button = (Button)view.findViewById(R.id.buttonSave);
		button.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Fragment2customize2name.this.saveAndContinue();
		    	
		    }
		});
		
	}
	
	public void saveAndContinue(){
		Log.d("Fragment2customize2name task", "protected void saveAndContinue() called");
		EditText programNameTextEdit = (EditText) 
		this.mainView.findViewById(R.id.editTextName);
		EditText nWeeksEditText = (EditText) 
		this.mainView.findViewById(R.id.editTextNWeeks);
		String programName = programNameTextEdit.getText().toString();
		String nWeeksString = nWeeksEditText.getText().toString();
		WorkoutManagerSingleton workoutManager
		= WorkoutManagerSingleton.getInstance();
		if(programName.equals("")){
			Activity activity = this.getActivity();
			Resources res = activity.getResources();
			
			new AlertDialog.Builder(activity)
			.setTitle(res.getString(R.string.warningNeedName))
			.setMessage(res.getString(R.string.warningNeedNameMessage))
			.show();
			return;
		}else if (nWeeksString.equals("")){
			Activity activity = this.getActivity();
			Resources res = activity.getResources();
			new AlertDialog.Builder(activity)
			.setTitle(res.getString(R.string.warningNeedNWeeks))
			.setMessage(res.getString(R.string.warningNeedNWeeksMessage))
			.show();
			return;
		}
		boolean nameAvailable
		= workoutManager.isWorkoutNameAvailable(programName);
		if (!nameAvailable){
			Activity activity = this.getActivity();
			Resources res = activity.getResources();
			new AlertDialog.Builder(activity)
			.setTitle(res.getString(R.string.warningName))
			.setMessage(res.getString(R.string.warningNameMessage))
			.show();
			return;
		}
		int nWeeks = Integer.parseInt(nWeeksString);
		if(nWeeks < 2 || nWeeks > 50){
			Activity activity = this.getActivity();
			Resources res = activity.getResources();
			new AlertDialog.Builder(activity)
			.setTitle(res.getString(R.string.warningNWeeks))
			.show();
			return;
		}
		if(this.fromExistingProgramMode){
			Spinner spinner = (Spinner)
					this.mainView.findViewById(
							R.id.spinnerProgramNames2);
			String existingProgramName
			= spinner.getSelectedItem().toString();
			Activity activity = this.getActivity();
			CustomizeExerciseAdapter.progressDialog
			= ProgressDialog.show(activity,
									"",
									"Creating workout",
									true);
			this.saveAsyncTask = new SaveAndContinueAsyncTask(
					programName,
					nWeeks,
					existingProgramName);
			this.saveAsyncTask.execute();
		}else{
			workoutManager.createProgram(programName, nWeeks);
			this.goNext(programName);
		}
		
		Log.d("Fragment2customize2name task", "protected void saveAndContinue() end");

	}
	
	protected void goNext(String programName){
		WorkoutManagerSingleton workoutManager
		= WorkoutManagerSingleton.getInstance();
		workoutManager.selectProgram(programName);
		workoutManager.selectFistWeek();
		Fragment newFragment = new Fragment2customize3day();
		FragmentTransaction transaction
			= getFragmentManager().beginTransaction();

		transaction.replace(this.getId(), newFragment);
		transaction.addToBackStack("customizationFromName");

		transaction.commit();
	}
	
	protected class SaveAndContinueAsyncTask
	extends AsyncTask<Void, Void, Void> {
		protected String programName;
		protected int nWeeks;
		protected String existingProgramName;
		public SaveAndContinueAsyncTask(
				String programName,
				int nWeeks,
				String existingProgramName){
			this.programName = programName;
			this.nWeeks = nWeeks;
			this.existingProgramName = existingProgramName;
		}
		
		@Override
		protected Void doInBackground(
				Void... params) {
			Log.d("Fragment2customize2name task", "protected Void doInBackground(...) called");
			WorkoutManagerSingleton workoutManager
			= WorkoutManagerSingleton.getInstance();
			workoutManager.createProgramFromExistingOne(
					programName,
					nWeeks,
					existingProgramName); //TODO in asynchrone task
			
			Log.d("Fragment2customize2name task", "protected Void doInBackground(...) end");
			return null;
		}

		@Override
		protected void onPostExecute(Void nothing) { 
			Log.d("Fragment2customize2name task", "protected void onPostExecute(...) called");
			if(CustomizeExerciseAdapter.progressDialog != null){
				Log.d("Fragment2customize2name task", "progress dialog not null");
				CustomizeExerciseAdapter.progressDialog.dismiss();
			}
			Fragment2customize2name.this.goNext(programName);
			Log.d("Fragment2customize2name task", "protected void onPostExecute(...) end");
		}
	}
	public void onDestroyView(){
		Log.d("Fragment2customize2name", "public void onDestroyView() called");
		
		if(Fragment2customize2name.progressDialog != null){
			Fragment2customize2name.progressDialog.dismiss();
			Fragment2customize2name.progressDialog = null;
		}
		if(this.saveAsyncTask != null){
			this.saveAsyncTask.cancel(false);
			Log.d("Fragment2customize2name", "saveAsyncCask canceled.");
			this.saveAsyncTask = null;
		}
		super.onDestroyView();
		Log.d("Fragment2customize2name", "public void onDestroyView() end");
	}
}
