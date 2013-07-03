package com.musclehack.musclehack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class Fragment2customize2name extends Fragment {
	/*//TODO je dois ajouter le fait qu'on peut
	 * jiste vouloir créé un nouveau workout
	 * dupuis un autre et affiché et remplir
	 * le spinner puis créé les requettes qui
	 * vont bien. Ensuite je devrais déboguer
	 * et gérer cette histoire de retour en
	 * arrière.
	 */
	protected View mainView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
								Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.create_workout,
				container, false);
		return view;
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
		workoutManager.createProgram(programName, nWeeks);
	}
}
