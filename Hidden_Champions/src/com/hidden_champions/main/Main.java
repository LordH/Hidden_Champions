package com.hidden_champions.main;

import com.hidden_champions.database.Data;
import com.hidden_champions.database.DatabaseManager;
import com.hidden_champions.database.containers.Firm;
import com.hidden_champions.database.containers.Stats;
import com.hidden_champions.io.SerranoReader;

public class Main {
	
	public static void main (String[] args) {
		
		SerranoReader reader = SerranoReader.instance();
		DatabaseManager database = DatabaseManager.instance();
		Data factory = new Data();
		
		Stats data = new Stats(reader.getNext());					
		Firm firm = null;
		double i = 0;
		double done = 0;
		double max = 1193156;

		long start = System.currentTimeMillis();
				
		while(reader.hasNext()) {
			data = new Stats(reader.getNext());
			
			//Create the first firm
			if (firm == null) {
				firm = factory.createFirm(data);
				i++;
				
			//Add an extra year's worth of data to a firm
			} else if(data.getAttribute(0).equals(firm.getOrgnr())) {
				factory.addYear(firm, data);
				
			//Send old firm to database and create a new one
			} else {
				database.add(firm);
				firm = factory.createFirm(data);
				
				//Print progress
				i++;
				if((i/max)*100 > done) {
					Main.printProgress(done++, start);
				}
			}
		}
		database.finish();
		Main.printProgress(100, start);
		System.out.println("Done!");
	}
	
	public static void printProgress(double done, double start) {
		String output = (int) done + " % : " + String.format("%.01f s", (System.currentTimeMillis()-start)/1000);
		System.out.println(output);
	}
}
