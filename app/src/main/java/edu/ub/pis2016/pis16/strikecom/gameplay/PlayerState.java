package edu.ub.pis2016.pis16.strikecom.gameplay;

import edu.ub.pis2016.pis16.strikecom.gameplay.items.Inventory;

public class PlayerState {
	private String name;
	private int scrap;
	private float fuel;
	private int score;
	private int enemyCounter =  0; // number of defeated enemies

	public Inventory inventory;

	public PlayerState(String name) {
		this.name = name;
		scrap = 0;
		fuel = 0;
		score = 0;
		inventory = new Inventory();
	}

	public void addScrap(int scrap) {
		this.scrap += scrap;
	}

	public void addFuel(float fuel) {
		this.fuel += fuel;
	}

	public void increaseCounter(){
		enemyCounter += 1;
	}

	public int getScrap() {
		return this.scrap;
	}

	public float getFuel() {
		return this.fuel;
	}

	public int getScore() {
		return computeScore();
	}

	private int computeScore() {
		score = scrap + enemyCounter * enemyCounter;
		return score;
	}


}
