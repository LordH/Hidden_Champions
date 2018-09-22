package com.hidden_champions.containers;

public class StatsFactory {

	//----------------------------
	//	INSTANTIATION
	//----------------------------
	
	private static StatsFactory instance = new StatsFactory();
	
	private StatsFactory() {
		
	}
	
	public static StatsFactory getStatsFactory() {
		return instance;
	}
	
	//----------------------------
	//	GETTERS
	//----------------------------
	
	public StatsInterface getStats() {
		return new Stats();
	}
	
}
