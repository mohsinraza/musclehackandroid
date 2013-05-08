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

	/*
	public class Program{
		public String name;
		List<SubProgram> subPrograms;
		HashMap<String, SubProgram> subProgramsHash;
		public Program(){
			this.subPrograms = new ArrayList<SubProgram>();
			this.subProgramsHash = new HashMap<String, SubProgram>();
		}
	};

	public class SubProgram{
		public String name;
		List<Week> weeks;
		public SubProgram(){
			this.weeks = new ArrayList<Week>();
		}
	};

	public class Week{
		public String name;
		public Day days[];
		public Week(){
			this.days = new Day[7];
			for(int i=0; i<7; i++){
				this.days = null;
			}
		}
	};
	//*/
	
	public static void setContext(Context context){
		WorkoutManagerSingleton.context = context;
		WorkoutManagerSingleton.instance.dbHelper = new ProgramDbHelper(WorkoutManagerSingleton.context);
		WorkoutManagerSingleton.instance.loadWorkoutData();
	}
	
	public static WorkoutManagerSingleton getInstance(){
		return WorkoutManagerSingleton.instance;
	}
	
	private WorkoutManagerSingleton(){
		
	}
	
	private void loadWorkoutData(){
		//TODO
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
