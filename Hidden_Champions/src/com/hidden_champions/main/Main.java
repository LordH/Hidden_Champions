package com.hidden_champions.main;

import com.hidden_champions.serranoIO.ReadSerrano;

public class Main {
	
	public static void main (String args[]) {
		
		ReadSerrano serrano = new ReadSerrano();
		
		String svar = serrano.getNextInfo();
		System.out.println(svar);
		
	}
}
