package com.musclehack.musclehack.workouts;

import java.util.ArrayList;
import java.util.List;

public class WorkoutDay{
	protected int numberOfTheWeek;
	protected String name;
	protected List<Exercice> exercices;
	protected boolean isnull;

	public WorkoutDay(){
		this.exercices = new ArrayList<Exercice>();
		this.isnull = true;
	}

	public boolean isNull(){
		return this.isnull;
	}

	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}

	public int getNumberOfTheWeek(){
		return this.numberOfTheWeek;
	}

	public void setNumberOfTheWeek(int numberOfTheWeek){
		this.numberOfTheWeek = numberOfTheWeek;
	}
};

