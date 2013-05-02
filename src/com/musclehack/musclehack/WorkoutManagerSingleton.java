package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class WorkoutManagerSingleton{
	private static WorkoutManagerSingleton instance = new WorkoutManagerSingleton();
	public HashMap<String, SubProgram> programs;

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

	public static WorkoutManagerSingleton getInstance(){
		return WorkoutManagerSingleton.instance;
	}
	
	private WorkoutManagerSingleton(){
		this.loadWorkoutData();
	}
	
	private void loadWorkoutData(){
		//TODO
	}
	
	public List<String> getAvailableProgramNames(){
		List<String> programs = new ArrayList<String>();
		programs.push_back("THT5 1");
		programs.push_back("THT5 2");
		programs.push_back("THT5 3");
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
