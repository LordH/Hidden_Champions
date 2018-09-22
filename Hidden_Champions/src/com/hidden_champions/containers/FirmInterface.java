package com.hidden_champions.containers;

public interface FirmInterface {
	
	//----------------------------
	//	GETTERS & SETTERS
	//----------------------------

	public int getId();
	
	public StatsInterface getStats();
	
	public void setId(int newId);
	
	public void setStats(StatsInterface newStats);

}
