package com.hidden_champions.containers;

public class Firm implements FirmInterface {
	
	private int 			id;
	private StatsInterface 	stats;
	
	//----------------------------
	//	CONSTRUCTORS
	//----------------------------
	
	protected Firm (int newId) {
		id = newId;
		stats = null;
	}
	
	protected Firm (int newId, StatsInterface initialStats) {
		id = newId;
		stats = initialStats;
	}
	
	//----------------------------
	//	GETTERS & SETTERS
	//----------------------------
	
	public int getId() {
		return id;
	}
		
	public StatsInterface getStats() {
		return stats;
	}
	
	public void setId(int newId) {
		id = newId;
	}
	
	public void setStats(StatsInterface newStats) {
		stats = newStats;
	}

}
