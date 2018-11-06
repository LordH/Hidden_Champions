package com.hidden_champions.database.containers;

import java.util.LinkedList;

/**
 * Class for storing firm attributes
 * @author Harald
 *
 */
public class Stats {

	private LinkedList<String> attributes;
	
	public Stats(String data) {
		attributes = new LinkedList<String>();
		divideString(data);
	}
	
	private void divideString(String data) {
		String text = "";
		int i = 0;
		int j = 0;
		
		while (i<data.lastIndexOf(";")) {
			j = data.indexOf(";", i);
			text = data.substring(i, j);
			text = text.replaceAll(",", "\\.");
			text = text.trim();
//			if (text.codePointAt(0) == 32)
//				text = "";
			attributes.add(text);
			i = j+1;
		}
	}
	
	public LinkedList<String> getAttributes(){
		return attributes;
	}
	
	public String getAttribute(int id) {
		return attributes.get(id);
	}
	
	public int getNr() {
		return attributes.size();
	}
}
