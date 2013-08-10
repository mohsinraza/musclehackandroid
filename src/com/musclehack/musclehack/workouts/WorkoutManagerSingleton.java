package com.musclehack.musclehack.workouts;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.musclehack.musclehack.R;



public class WorkoutManagerSingleton{
	public static final String LAST_PROGRAM_SHORTCUT_NAME = "lastProgramShortcutName";
	public static final String LAST_PROGRAM = "lastProgram";
	public static final String CREATE_PROGRAM = "Create/Edit a workout";
	public static final String NEW_PROGRAM = "New";
	public static final String NEW_PROGRAM_FROM_EXISTING = "New from an existing one";
	public static final String DELETE_AN_EXISTING_ONE = "Delete an existing one";
	public static final String PREF_NAME = "MuscleHackPreferences";

	protected static WorkoutManagerSingleton instance = new WorkoutManagerSingleton();
	protected static Context context = null;
	protected static Activity mainActivity = null;
	//public HashMap<String, SubProgram> programs;
	protected ProgramDbHelper dbHelper;
	protected String selectedProgramName;
	protected String selectedWeek;
	protected Day selectedDay;
	protected ArrayList<HashMap<Integer, String>> currentData;
	protected int levelChoice;
	protected boolean databaseDeleted;
	
	protected final Lock _mutexExercises = new ReentrantLock(true);
	
	public static void setContext(Context context){
		WorkoutManagerSingleton.context = context;
		WorkoutManagerSingleton.instance.dbHelper = new ProgramDbHelper(WorkoutManagerSingleton.context);
		WorkoutManagerSingleton.instance.databaseDeleted = false;
	}
	
	public static void setMainActivity(Activity activity){
		WorkoutManagerSingleton.mainActivity = activity;
	}
	
	public Activity getMainActivity(){
		return WorkoutManagerSingleton.mainActivity;
	}
	
	public static void closeDatabase(){
		if(WorkoutManagerSingleton.instance.dbHelper != null){
			WorkoutManagerSingleton.instance.dbHelper.close();
		}
	}
	
	public Context getContext(){
		return WorkoutManagerSingleton.context;
	}
	
	public static void clearDatabase(){
		if(WorkoutManagerSingleton.context != null){
			WorkoutManagerSingleton.closeDatabase();
			WorkoutManagerSingleton.context.deleteDatabase(ProgramDbHelper.DATABASE_NAME);
			String prefName = getPrefName();
			SharedPreferences settings = WorkoutManagerSingleton.context.getSharedPreferences(prefName, 0);
			SharedPreferences.Editor settingsEditor = settings.edit();
			settingsEditor.putString(LAST_PROGRAM_SHORTCUT_NAME, "");
			settingsEditor.commit();
			WorkoutManagerSingleton.instance.levelChoice = 0;
			WorkoutManagerSingleton.instance.databaseDeleted = true;
			WorkoutManagerSingleton.instance.dbHelper = new ProgramDbHelper(WorkoutManagerSingleton.context);
		}
	}
	
	public static boolean databaseExists(){
		File dbFile = context.getDatabasePath(ProgramDbHelper.DATABASE_NAME);
	    boolean exists = dbFile.exists();
	    return exists;
	}
	
	public static WorkoutManagerSingleton getInstance(){
		Log.d("WorkoutManagerSingleton", "public static WorkoutManagerSingleton getInstance()...val: " + WorkoutManagerSingleton.instance);
		return WorkoutManagerSingleton.instance;
	}
	
	private WorkoutManagerSingleton(){
		this.levelChoice = 0;
		this.dbHelper = null;
		this.databaseDeleted = false;
		this.setLevelChoice(0);
	}
	
	public boolean isDatabaseDeleted(){
		return this.databaseDeleted;
	}
	
	public void setDatabaseNotDeleted(){
		this.databaseDeleted = false;
	}
	
	public void setLevelChoice(int levelChoice){
		this.levelChoice = levelChoice;
	}
	
	public int getLevelChoice(){
		return this.levelChoice;
	}
	

	public List<String> getAvailableProgramNames(){
		List<String> programs = this.dbHelper.getAvailableProgramNames();
		return programs;
	}

	public List<String> getAvailableProgramNamesWithShortcut(){
		List<String> programs = this.dbHelper.getAvailableProgramNames();
		if(WorkoutManagerSingleton.context != null){
			String lastProgram = this.getLastProgramShortcutName();
			if(!lastProgram.equals("")){
				programs.add(0, lastProgram);
			}
		}
		programs.add(WorkoutManagerSingleton.CREATE_PROGRAM);
		return programs;
	}

	public List<String> getAvailableProgramNamesToCustomize(){
		List<String> programs = this.dbHelper.getAvailableProgramNames();
		if(programs.size() > 0){
			programs.add(0, DELETE_AN_EXISTING_ONE);
			programs.add(0, NEW_PROGRAM_FROM_EXISTING);
		}
		programs.add(0, NEW_PROGRAM);
		return programs;
	}

	public static String getPrefName(){
		return WorkoutManagerSingleton.PREF_NAME;
	}

	public String getLastProgramShortcutName(){
		String prefName = getPrefName();
		SharedPreferences settings = WorkoutManagerSingleton.context.getSharedPreferences(prefName, 0);
		String lastProgram = settings.getString(LAST_PROGRAM_SHORTCUT_NAME, "");
		return lastProgram;
	}	
	
	public void saveLastProgram(){
		String prefName = getPrefName();
		SharedPreferences settings = WorkoutManagerSingleton.context.getSharedPreferences(prefName, 0);
		SharedPreferences.Editor settingsEditor = settings.edit();
		settingsEditor.putString(LAST_PROGRAM, this.selectedProgramName);
		String lastProgramShortcutName
		= this._getProgramShortcutName(this.selectedProgramName);
		settingsEditor.putString(LAST_PROGRAM_SHORTCUT_NAME, lastProgramShortcutName);
		settingsEditor.commit();
	}
	
	protected String _getProgramShortcutName(String programName){
		String shortcutName
		= WorkoutManagerSingleton.context.getString(R.string.currentSubWorkout)
				+ " " + programName;
		return shortcutName;
		
	}

	public String selectLastProgram(){
		String prefName = getPrefName();
		SharedPreferences settings = WorkoutManagerSingleton.context.getSharedPreferences(prefName, 0);
		String lastProgram = settings.getString(LAST_PROGRAM, "");
		if(!lastProgram.equals("")){
			this.selectProgram(lastProgram);
		}
		return lastProgram;
	}	

	public void selectProgram(String programName){
		this.selectedProgramName = programName;
	}

	public void selectWeek(String weekName){
		this.selectedWeek = weekName;
	}
	
	public void selectFistWeek(){
		String firstWeek
		= this.dbHelper.getFirstWeek(
				this.selectedProgramName);
		this.selectedWeek = firstWeek;
	}
	
	public void selectDay(String dayName){
		this.selectedDay = new Day(dayName, -1);;
	}
	
	public void selectDay(Day day){
		this.selectedDay = day;
	}
	
	protected void setCurrentData(ArrayList<HashMap<Integer, String>> data){
		this.currentData = data;
	}
	
	protected ArrayList<HashMap<Integer, String>> getCurrentData(){
		return this.currentData;
	}
	
	public void setExerciceInfo(
								String exerciseId,
								String rest,
								String weight,
								String nReps){
		this.dbHelper.setExerciceInfo(this.selectedProgramName,
										this.selectedWeek,
										this.selectedDay.workoutName,
										exerciseId,
										rest,
										weight,
										nReps);

	}

	
	public boolean isProgramCompleted(String programName){
		boolean completed = this.dbHelper.isProgramCompleted(this.selectedProgramName);
		return completed;
	}
	
	public List<String> getAvailableWeeks(){
		List<String> weeks = this.dbHelper.getAvailableWeeks(this.selectedProgramName);
		return weeks;
	}
	
	public boolean isWeekCompleted(String week){
		boolean completed = this.dbHelper.isWeekCompleted(this.selectedProgramName,
															week);
		return completed;
	}
	
	public List<Day> getAvailableDays(){
		List<Day> days = this.dbHelper.getAvailableDays(this.selectedProgramName,
															this.selectedWeek);
		return days;
	}
	
	public List<String> getAvailableWorkoutNames(){
		List<Day> days = this.dbHelper.getAvailableDays(this.selectedProgramName,
															this.selectedWeek);
		List<String> dayNames = new ArrayList<String>();
		for(Day day: days){
			dayNames.add(day.getWorkoutName());
		}
		return dayNames;
	}
	
	public boolean isDayCompleted(String day){
		boolean completed = this.dbHelper.isDayCompleted(this.selectedProgramName,
															this.selectedWeek,
															day);
		return completed;
	}
	
	public List<Exercice> getAvailableExercises(){
		Log.d("WorkoutManagerSingletion", "public List<Exercice> getAvailableExercises(...) called");
		this._mutexExercises.lock();
		List<Exercice> exercices = this.dbHelper.getAvailableExercices(
				this.selectedProgramName,
				this.selectedWeek,
				this.selectedDay.workoutName);
		this._mutexExercises.unlock();
		Log.d("WorkoutManagerSingletion", "public List<Exercice> getAvailableExercises(...) end");
		return exercices;
	}

	public boolean isExerciseCompleted(int idExercise){
		boolean completed = this.dbHelper.isExerciseCompleted(idExercise);
		return completed;
	}
	
	public List<Exercice> getPreviousExercices(){
		List<Exercice> exercices = this.dbHelper.getPreviousExercices(
				this.selectedProgramName,
				this.selectedWeek,
				this.selectedDay.workoutName);
		return exercices;
	}
	
	public void setExerciseCompleted(int exerciseId, boolean completed){
		//TODO)
	}
	
	public boolean isWorkoutNameAvailable(String name){
		boolean available = this.dbHelper.isWorkoutNameAvailable(name);
		return available;
	}
	
	public void createProgram(String name, int nWeeks){
		this.dbHelper.createProgram(name, nWeeks);
	}
	
	public void createProgramFromExistingOne(
			String name,
			int nWeeks,
			String existingProgramName){
		this.dbHelper.createProgramFromExistingOne(
				name,
				nWeeks,
				existingProgramName);
	}
	
	public void deleteProgram(String programName){
		this.dbHelper.deleteProgram(programName);
		String prefName = getPrefName();
		SharedPreferences settings = WorkoutManagerSingleton.context.getSharedPreferences(prefName, 0);
		String currentProgram = settings.getString(LAST_PROGRAM_SHORTCUT_NAME, "");
		String deletingProgramShortcutName
		= this._getProgramShortcutName(programName);
		if(currentProgram.equals(deletingProgramShortcutName)){
			SharedPreferences.Editor settingsEditor = settings.edit();
			settingsEditor.putString(LAST_PROGRAM_SHORTCUT_NAME, "");
			settingsEditor.putString(LAST_PROGRAM, "");
			settingsEditor.commit();
		}
	}
	
	public void deleteDay(
			int dayOfTheWeek){
		this.dbHelper.deleteDay(
				this.selectedProgramName,
				dayOfTheWeek);
	}
	
	
	public void createDay(
			String dayName,
			int dayOfTheWeek){
		this.dbHelper.createDay(
				this.selectedProgramName,
				dayName,
				dayOfTheWeek);
	}
	
	public void createDayFromExistingOne(
			String dayName,
			int dayOfTheWeek,
			String existngOne){
		this.dbHelper.createDayFromExistingOne(
				this.selectedProgramName,
				dayName,
				dayOfTheWeek,
				existngOne);
	}
	
	public void setExercices(
			List<Exercice> exercises){
		Log.d("WorkoutManagerSingletion", "public void setExercices(...) called");
		this._mutexExercises.lock();
		this.dbHelper.setExercices(
				this.selectedProgramName,
				this.selectedDay,
				exercises);
		this._mutexExercises.unlock();
		Log.d("WorkoutManagerSingletion", "public void setExercices(...) caendlled");
	}
}
