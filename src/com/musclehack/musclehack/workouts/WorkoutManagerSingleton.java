package com.musclehack.musclehack.workouts;

import java.util.List;

import com.musclehack.musclehack.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;



public class WorkoutManagerSingleton{
	public static final String LAST_SUB_PROGRAM_SHORTCUT_NAME = "lastSubProgramShortcutName";
	public static final String LAST_SUB_PROGRAM = "lastSubProgram";
	public static final String LAST_PROGRAM = "lastProgram";

	protected static WorkoutManagerSingleton instance = new WorkoutManagerSingleton();
	protected static Context context = null;
	//public HashMap<String, SubProgram> programs;
	protected ProgramDbHelper dbHelper;
	protected String selectedProgramName;
	protected String selectedSubProgramName;
	protected String selectedWeek;
	protected String selectedDay;
	protected int levelChoice;
	
	public static void setContext(Context context){
		WorkoutManagerSingleton.context = context;
		WorkoutManagerSingleton.instance.dbHelper = new ProgramDbHelper(WorkoutManagerSingleton.context);
	}
	
	public static void closeDatabase(){
		if(WorkoutManagerSingleton.instance.dbHelper != null){
			WorkoutManagerSingleton.instance.dbHelper.close();
		}
	}
	
	public static WorkoutManagerSingleton getInstance(){
		Log.d("WorkoutManagerSingleton", "public static WorkoutManagerSingleton getInstance()...val: " + WorkoutManagerSingleton.instance);
		return WorkoutManagerSingleton.instance;
	}
	
	private WorkoutManagerSingleton(){
		this.levelChoice = 0;
		this.dbHelper = null;
	}
	
	public void setLevelChoice(int levelChoice){
		this.levelChoice = levelChoice;
	}
	
	public int getLevelChoice(){
		return this.levelChoice;
	}

	public List<String> getAvailableProgramNames(){
		List<String> programs = this.dbHelper.getAvailableProgramNames();
		if(WorkoutManagerSingleton.context != null){
			String lastSubProgram = this.getLastSubProgramShortcutName();
			if(!lastSubProgram.equals("")){
				programs.add(0, lastSubProgram);
			}
		}
		return programs;
	}
	
	public static String getPrefName(){
		return "MuscleHackPreferences";
	}	
	
	public String getLastSubProgramShortcutName(){
		String prefName = getPrefName();
		SharedPreferences settings = WorkoutManagerSingleton.context.getSharedPreferences(prefName, 0);
		String lastSubProgram = settings.getString(LAST_SUB_PROGRAM_SHORTCUT_NAME, "");
		return lastSubProgram;
	}	
	
	public void saveLastSubWorkout(){
		String prefName = getPrefName();
		SharedPreferences settings = WorkoutManagerSingleton.context.getSharedPreferences(prefName, 0);
		SharedPreferences.Editor settingsEditor = settings.edit();
		settingsEditor.putString(LAST_PROGRAM, this.selectedProgramName);
		settingsEditor.putString(LAST_SUB_PROGRAM, this.selectedSubProgramName);
		String lastSubPRogramShortcutName = WorkoutManagerSingleton.context.getString(R.string.currentSubWorkout)
											+ " " + this.selectedSubProgramName;
		settingsEditor.putString(LAST_SUB_PROGRAM_SHORTCUT_NAME, lastSubPRogramShortcutName);
		settingsEditor.commit();
	}
	
	public String selectLastSubProgram(){
		String prefName = getPrefName();
		SharedPreferences settings = WorkoutManagerSingleton.context.getSharedPreferences(prefName, 0);
		String lastProgram = settings.getString(LAST_PROGRAM, "");
		String lastSubProgram = settings.getString(LAST_SUB_PROGRAM, "");
		if(!lastProgram.equals("") && !lastSubProgram.equals("")){
			this.selectProgram(lastProgram);
			this.selectSubProgram(lastSubProgram);
		}
		return lastSubProgram;
	}	

	public void selectProgram(String programName){
		this.selectedProgramName = programName;
	}
	
	public void selectSubProgram(String subProgramName){
		this.selectedSubProgramName = subProgramName;
	}

	public void selectWeek(String weekName){
		this.selectedWeek = weekName;
	}
	
	public void selectDay(String dayName){
		this.selectedDay = dayName;
	}
	
	public void setExerciceInfo(
								String exerciseId,
								String rest,
								String weight,
								String nReps){
		this.dbHelper.setExerciceInfo(this.selectedProgramName,
										this.selectedSubProgramName,
										this.selectedWeek,
										this.selectedDay,
										exerciseId,
										rest,
										weight,
										nReps);

	}

	public List<String> getAvailableSubProgramNames(){
		List<String> subPrograms = this.dbHelper.getAvailableSubProgramNames(this.selectedProgramName);
		return subPrograms;
	}

	public List<String> getAvailableSubProgramNames(String programName){
		List<String> subPrograms = this.dbHelper.getAvailableSubProgramNames(programName);
		return subPrograms;
	}
	
	public boolean isSubProgramCompleted(String subProgramName){
		boolean completed = this.dbHelper.isSubProgramCompleted(this.selectedProgramName,
																subProgramName);
		return completed;
	}
	
	public List<String> getAvailableWeeks(){
		List<String> weeks = this.dbHelper.getAvailableWeeks(this.selectedProgramName,
															 this.selectedSubProgramName);
		return weeks;
	}
	
	public boolean isWeekCompleted(String week){
		boolean completed = this.dbHelper.isWeekCompleted(this.selectedProgramName,
															this.selectedSubProgramName,
															week);
		return completed;
	}
	
	public List<String> getAvailableDays(){
		List<String> days = this.dbHelper.getAvailableDays(this.selectedProgramName,
															this.selectedSubProgramName,
															this.selectedWeek);
		return days;
	}
	
	public boolean isDayCompleted(String day){
		boolean completed = this.dbHelper.isDayCompleted(this.selectedProgramName,
															this.selectedSubProgramName,
															this.selectedWeek,
															day);
		return completed;
	}
	
	public List<Exercice> getAvailableExercices(){
		List<Exercice> exercices = this.dbHelper.getAvailableExercices(this.selectedProgramName,
																	this.selectedSubProgramName,
																	this.selectedWeek,
																	this.selectedDay);
		return exercices;
	}

	public boolean isExerciseCompleted(int idExercise){
		boolean completed = this.dbHelper.isExerciseCompleted(idExercise);
		return completed;
	}
	
	public List<Exercice> getPreviousExercices(){
		List<Exercice> exercices = this.dbHelper.getPreviousExercices(this.selectedProgramName,
																	this.selectedSubProgramName,
																	this.selectedWeek,
																	this.selectedDay);
		return exercices;
	}
	
	public void setExerciseCompleted(int exerciseId, boolean completed){
		//TODO)
	}
}
