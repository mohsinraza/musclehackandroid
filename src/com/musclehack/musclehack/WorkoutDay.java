package com.musclehack.musclehack;

public class WorkoutDay{
	protected int nomberOfTheWeek;
	protected String name;
	protected list<Exercice> exercices;
	protected boolean isnull;

	public WorkoutDay(){
		this.exercices = new ArrayList<Exercice>();
		this.isNull = true;
	}

	public boolean isNull(){
		return this.isNull;
	}

	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}

	public int getNumberOfTheWeek(){
		return this.nomberOfTheWeek;
	}

	public void setNumberOfTheWeek(int numberOfTheWeek){
		this.numberOfTheWeek = numberOfTheWeek;
	}
};

