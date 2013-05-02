package com.musclehack.musclehack;

public class Exercice{
	protected String name;
	protected int nRep;
	protected float weight;	
	protected boolean done;

	public Exercice(){
		this.name = "noname";
		this.nRep = 0;
		this.nWeight = 0.0;
	}

	public boolean isDone(){
		if(this.nRep > 0 && this.nWeight > 0){
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

	public String getWeight(){
		return this.weight;
	}

	public void setWeight(float weight){
		this.weight = weight;
	}
};

