package com.musclehack.musclehack.workouts;



public class Exercice{
	protected String name;
	protected int nRep;
	protected float weight;

	public Exercice(){
		this.name = "noname";
		this.nRep = 0;
		this.weight = 0.f;
	}
	
	public Exercice(String name){
		this.name = name;
		this.nRep = 0;
		this.weight = 0.f;
	}
	
	public Exercice(String name, int nRep, float weight){
		this.name = name;
		this.nRep = nRep;
		this.weight = weight;
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
		this.updateDataInDB();
	}

	public float getWeight(){
		return this.weight;
	}

	public void setWeight(float weight){
		this.weight = weight;
		this.updateDataInDB();
	}
	
	public void updateDataInDB(){
		
	}
};

