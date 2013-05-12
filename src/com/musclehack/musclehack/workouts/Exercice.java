package com.musclehack.musclehack.workouts;



public class Exercice{
	protected String name;
	protected int nRep;
	protected float weight;
	protected String repRange;
	protected int rest;
	protected int id;


	public Exercice(int id, String name, String repRange, int rest){
		this.id = id;
		this.name = name;
		this.nRep = 0;
		this.weight = 0.f;
		this.repRange = repRange;
		this.rest = rest;
	}
	
	public Exercice(int id, String name, int nRep, float weight, String repRange, int rest){
		this.id = id;
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

	public int getId(){
		return this.id;
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
	
	public String getNRepString(){
		String nRep = "";
		if(this.nRep > 0){
			nRep = "" + this.nRep;
		}
		return nRep;
	}

	public void setNRep(int nRep){
		this.nRep = nRep;
	}

	public float getWeight(){
		return this.weight;
	}
	
	public String getWeightString(){
		String weight = "";
		if(this.weight > 0.f){
			weight = "" + this.weight;
		}
		return weight;
	}

	public void setWeight(float weight){
		this.weight = weight;
	}

	public String getRepRange(){
		return this.repRange;
	}
	
	public int getRest(){
		return this.rest;
	}
};

