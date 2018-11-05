package com.hidden_champions.main;

import com.hidden_champions.containers.Data;
import com.hidden_champions.containers.Firm;
import com.hidden_champions.containers.Stats;
import com.hidden_champions.database.DatabaseManager;
import com.hidden_champions.io.SerranoReader;

public class Main {

	public static void main (String[] args) {
		
		SerranoReader reader = SerranoReader.instance();
		DatabaseManager database = DatabaseManager.instance();

		Stats data = new Stats(reader.getNext());					
		Firm firm = new Firm("","","-1");
		double i = 0;
		double done = 0;
		double max = 1193156;

		double start = System.currentTimeMillis();
				
		while(reader.hasNext()) {
			data = new Stats(reader.getNext());
			
			if(data.getAttribute(0).equals(firm.getOrgnr())) {
				firm.addYear(data);
			} else {
				//Send old firm to database
				if(!firm.getOrgnr().equals(""))
					database.add(firm);

				//Create new firm
				firm = new Firm(data.getAttribute(Data.ORGNR),data.getAttribute(Data.NAME),data.getAttribute(Data.JURFORM));
				firm.addYear(data);
				
				//Print progress
				i++;
				if(i/max>done) {
					String output = (int)(done*100) + " % : " + String.format("%.01f s", (System.currentTimeMillis()-start)/1000);
					System.out.println(output);
					done = done + 0.01;
				}
			}
		}
		database.finish();
		System.out.println("Done!");
	}
}
