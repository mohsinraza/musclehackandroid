package com.musclehack.musclehack.workouts;

public class Day {
	static public String[] dayNames = {"Monday",
								"Tuesday",
								"Wednesday",
								"Thursday",
								"Friday",
								"Saturday",
								"Sunday"};
	protected String workoutName;
	protected int dayOfTheWeek;
	public Day(String workoutName, int dayOfTheWeek){
		this.workoutName = workoutName;
		this.dayOfTheWeek = dayOfTheWeek;
	}
	
	public String getWorkoutName(){
		return this.workoutName;
	}
	
	public int getDayOfTheWeek(){
		return this.dayOfTheWeek;
	}
	
	public String getDayName(){
		String dayName = Day.dayNames[this.dayOfTheWeek];
		return dayName;
	}
}
