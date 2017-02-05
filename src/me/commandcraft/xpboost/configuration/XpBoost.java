package me.commandcraft.xpboost.configuration;

public class XpBoost {

	private static int maxId = 0;

	private double multiplier;
	private int time_left;
	private boolean active;
	private int id;
	private String player;
	
	public XpBoost(String player, double multiplier, int time_left) {
		this.multiplier = multiplier;
		this.time_left = time_left;
		this.player = player;
		this.active = false;
		this.player = player;
		this.id = maxId++;
	}
	
	public double getMultiplier() {
		return multiplier;
	}
	
	public int getTimeLeft() {
		return time_left;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive() {
		this.active = true;
	}
	
	public String getPlayer() {
		return player;
	}
	
	public int getId() {
		return id;
	}
	
	public boolean run() {
		if (!active) return false;
		time_left--;
		return time_left < 0;
	}
	
	public boolean equals(int id) {
		return this.id == id;
	}
	
	public static void setMaxId(int id) {
		maxId = id;
	}
	
	public static int getMaxId() {
		return maxId;
	}
}
