package com.musclehack.musclehack.workouts;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;



public class WorkoutManagerSingleton{
	protected static WorkoutManagerSingleton instance = new WorkoutManagerSingleton();
	protected static Context context = null;
	//public HashMap<String, SubProgram> programs;
	protected ProgramDbHelper dbHelper;
	protected String selectedProgramName;
	protected String selectedSubProgramName;
	protected String selectedWeek;
	protected String selectedDay;
	
	public static void setContext(Context context){
		WorkoutManagerSingleton.context = context;
		WorkoutManagerSingleton.instance.dbHelper = new ProgramDbHelper(WorkoutManagerSingleton.context);
	}
	
	public static WorkoutManagerSingleton getInstance(){
		return WorkoutManagerSingleton.instance;
	}
	
	private WorkoutManagerSingleton(){
		
	}

	public List<String> getAvailableProgramNames(){
		List<String> programs = this.dbHelper.getAvailableProgramNames();
		return programs;
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
	
	public void setExerciceInfo(String exerciceName, String rest, String weight, String nReps){
		this.dbHelper.setExerciceInfo(this.selectedProgramName,
						this.selectedSubProgramName,
						this.selectedWeek,
						this.selectedDay,
						exerciceName,
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
	
	public List<String> getAvailableWeeks(){
		List<String> weeks = this.dbHelper.getAvailableWeeks(this.selectedProgramName,
															 this.selectedSubProgramName);
		return weeks;
	}
	
	public List<String> getAvailableDays(){
		List<String> days = this.dbHelper.getAvailableDays(this.selectedProgramName,
															this.selectedSubProgramName,
															this.selectedWeek);
		return days;
	}
	
	public List<Exercice> getAvailableExercices(){
		List<Exercice> exercices = this.dbHelper.getAvailableExercices(this.selectedProgramName,
																	this.selectedSubProgramName,
																	this.selectedWeek,
																	this.selectedDay);
		return exercices;
	}
}
