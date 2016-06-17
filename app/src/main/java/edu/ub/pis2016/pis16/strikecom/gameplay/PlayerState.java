package edu.ub.pis2016.pis16.strikecom.gameplay;

import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Inventory;

public class PlayerState {
	private String name;
	private int scrap;
	private float fuel;
	private int score;
	private int enemyCounter =  0; // number of defeated enemies

	private float scrapMultiplier = 1f;

	public Inventory inventory;

	public PlayerState(String name) {
		this.name = name;
		scrap = 0;
		fuel = 0;
		score = 0;
		inventory = new Inventory();
	}

	public  void setScrapMultiplier(float multiplier) {
		this.scrapMultiplier = multiplier;
	}

	public void addScrap(int scrap) {
		this.scrap += scrap * scrapMultiplier;
	}

	public void addFuel(float fuel) {
		this.fuel = MathUtils.max(0, this.fuel + fuel);
	}

	public boolean isOutOfFuel() {
		return fuel == 0;
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
		score = scrap + 2*enemyCounter*enemyCounter + MathUtils.round(fuel);
		return score;
	}
}
