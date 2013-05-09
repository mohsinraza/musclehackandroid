package com.musclehack.musclehack.workouts;



public class Exercice{
	protected String name;
	protected int nRep;
	protected float weight;
	protected String repRange;
	protected String rest;


	public Exercice(String name, String repRange, String rest){
		this.name = name;
		this.nRep = 0;
		this.weight = 0.f;
		this.repRange = repRange;
		this.rest = rest;
	}
	
	public Exercice(String name, int nRep, float weight, String repRange, String rest){
		this.name = name;
		this.nRep = nRep;
		this.weight = weight;
		this.repRange = repRange;
		this.rest = rest;
	}

	public boolean isDone(){
		if(this.nRep > 0 && this.weight > 0.f){
			return true;
		}else{
			return false;
		}
	}

	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}

	public int getNRep(){
		return this.nRep;
	}

	public void setNRep(int nRep){
		this.nRep = nRep;
	}

	public float getWeight(){
		return this.weight;
	}

	public void setWeight(float weight){
		this.weight = weight;
	}

	public String getRepRange(){
		return this.repRange;
	}
	
	public String getRest(){
		return this.rest;
	}
};

