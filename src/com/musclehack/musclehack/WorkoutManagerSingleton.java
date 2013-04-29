package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class WorkoutManagerSingleton{

	private static WorkoutManagerSingleton instance = new WorkoutManagerSingleton();
	public HashMap<String, String> programs;

	public static WorkoutManagerSingleton getInstance(){
		return WorkoutManagerSingleton.instance;
	}
	
	private WorkoutManagerSingleton(){
		this.loadWorkoutData();
	}
	
	private void loadWorkoutData(){
		
	}
	
	public List<String> getAvailableProgramNames(){
		List<String> programs = new ArrayList<String>();
		return programs;
	}

	public void selectProgram(String programName){
		//TODO
	}
	
	public void selectSubProgram(String subProgramName){
		//TODO
	}
	
	public void selectWeek(String weekName){
		//TODO
	}
	
	public void selectDay(String dayName){
		//TODO
	}
	
	public List<String> getAvailableSubProgramNames(){
		List<String> programs = new ArrayList<String>();
		return programs;
	}
	
	public List<String> getAvailableSubProgramNames(String programName){
		List<String> programs = new ArrayList<String>();
		return programs;
	}
	
	public List<String> getAvailableWeeks(){
		List<String> programs = new ArrayList<String>();
		return programs;
	}
	
	public List<String> getAvailableDays(){
		List<String> programs = new ArrayList<String>();
		return programs;
	}
	
	public List<String> getAvailableExercices(){
		List<String> programs = new ArrayList<String>();
		return programs;
	}
	
}