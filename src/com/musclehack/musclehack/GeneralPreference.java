package com.musclehack.musclehack;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;
 
public class GeneralPreference extends PreferenceActivity {
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		Preference button = (Preference)findPreference("buttonClearData");
		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
						@Override
						public boolean onPreferenceClick(Preference arg0) { 
							
							DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									switch (which){
									case DialogInterface.BUTTON_POSITIVE:
										WorkoutManagerSingleton.clearDatabase();
										
										
										//Activity mainActivity = (Activity) GeneralPreference.this.getBaseContext();
									    //Intent intent = mainActivity.getIntent();
									    //mainActivity.finish();
									    //startActivity(intent);
										break;
									case DialogInterface.BUTTON_NEGATIVE:
										//No button clicked
										break;
									}
								}
							};

							Context context = (Context)GeneralPreference.this;
							AlertDialog.Builder builder = new AlertDialog.Builder(context);
							builder.setMessage(context.getString(R.string.areYouSure))
								.setPositiveButton(context.getString(R.string.yes), dialogClickListener)
								.setNegativeButton(context.getString(R.string.no), dialogClickListener).show();
							
							
							
							return true;
						}
					});
	}
	//*
	public void onBackPressed() {
		super.onBackPressed();
		WorkoutManagerSingleton workoutManager = WorkoutManagerSingleton.getInstance();
		//Context context = workoutManager.getContext();
		Activity activity = workoutManager.getMainActivity();
		if(workoutManager.isDatabaseDeleted()){
			workoutManager.setDatabaseNotDeleted();
			startActivity(new Intent(activity, MainActivity.class));
			activity.finish();
		}
	}
	//*/
}