package com.hidden_champions.containers;

public class FirmFactory {
	
	//----------------------------
	//	INSTANTIATION
	//----------------------------
	
	private static FirmFactory instance = new FirmFactory();
	
	private FirmFactory() {
		
	}
	
	public static FirmFactory getInstance() {
		return instance;
	}
	
	//----------------------------
	//	GETTERS
	//----------------------------
	
	public FirmInterface getFirm(int orgnr) {
		return new Firm(orgnr);
	}
	
	public FirmInterface getFirm(int orgnr, StatsInterface stats) {
		return new Firm(orgnr, stats);
	}
	
}
