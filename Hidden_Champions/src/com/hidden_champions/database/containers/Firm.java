package com.hidden_champions.database.containers;

import java.util.LinkedList;

/**
 * Class for representing a firm
 * @author Harald
 *
 */
public class Firm {
	
	private String orgnr;
	private String name;
	private int jurForm;
	private LinkedList<Stats> stats;
	
	public Firm (String orgnr, String name, String jurForm) {
		this.orgnr = orgnr;
		this.name = name;
		this.jurForm = Integer.valueOf(jurForm);
		this.stats = new LinkedList<Stats>();
	}
	
	public void addYear(Stats stat) {
		stats.add(stat);
	}
	
	public LinkedList<Stats> getStats(){
		return stats;
	}
	
	public String getOrgnr() {
		return orgnr;
	}
	
	public String getName() {
		return name;
	}
	
	public int getJurform() {
		return jurForm;
	}
}
