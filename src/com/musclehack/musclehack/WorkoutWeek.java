package com.musclehack.musclehack;

public class WorkoutWeek{
	public String name;
	public Day days[];

	public WorkoutWeek(){
		this.days = new Day[7];
		for(int i=0; i<7; i++){
			this.days = null;
		}
	}

	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}
};

