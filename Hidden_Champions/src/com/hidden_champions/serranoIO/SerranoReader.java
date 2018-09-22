package com.hidden_champions.serranoIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class SerranoReader {

	private File 	serranoFile;
	private BufferedReader reader;
	
	
	//----------------------------
	//	INSTANTIATION
	//----------------------------
	
	private static SerranoReader instance = null;
	
	private SerranoReader() {
		this.getSerranoFile();
	}
	
	public static SerranoReader getInstance() {
		if (instance == null)
			instance = new SerranoReader();
		
		return instance;
	}

	//----------------------------
	//	READ FROM SERRANO FILE
	//----------------------------
	
	public String readNext() {
		
		return "I ain't found shit!";
	}

	//----------------------------
	//	PRIVATE METHODS
	//----------------------------
		
	private void getSerranoFile () {
		try {
			String[] path = findFileLocation();
			serranoFile = new File(path[0], path[1]);
			reader = new BufferedReader(new FileReader(serranoFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String[] findFileLocation() {
		String filePath = "C:/Serrano";
		String fileName = "serrano.csv";
		return new String[] {filePath, fileName};
	}
}
