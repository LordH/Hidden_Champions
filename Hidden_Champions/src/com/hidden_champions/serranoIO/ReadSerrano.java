package com.hidden_champions.serranoIO;

public class ReadSerrano {
	
	//----------------------------
	//	CONSTRUCTOR
	//----------------------------
	
	public ReadSerrano() {
		
	}
	
	//----------------------------
	//	GETTERS & SETTERS
	//----------------------------
	
	public String getNextInfo() {
		String info = this.readNext();
		
		return info;
	}
	
	//----------------------------
	//	INTERNAL METHODS
	//----------------------------
	
	private String readNext() {
		return SerranoReader.getInstance().readNext();
	}
}
