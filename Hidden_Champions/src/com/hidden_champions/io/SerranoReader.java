package com.hidden_champions.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class for reading from the Serrano .csv file.
 * Cannot be freely instantiated, instead use SerranoReader.instance() to access.
 * @author Harald
 */
public class SerranoReader {

	private static SerranoReader instance = null;
	private File serrano = null;
	private BufferedReader reader = null;
	
	private String nextLine;
	
	private SerranoReader() {
		serrano = new File("C:/Serrano/Serrano.csv");
		try {
			reader = new BufferedReader(new FileReader(serrano));
			System.out.println("Successfully accessed file.");
		} catch (FileNotFoundException e) {
			System.out.println("Can't find the Serrano file.");
			e.printStackTrace();
		}
	}

	/**
	 * Access the singleton of SerranoReader
	 * @return The instance of the reader
	 */
	public static SerranoReader instance() {
		if(instance == null) 
			instance = new SerranoReader();
		return instance;
	}
	
	// ---------------------------------------------------------------------
	//	METHODS
	// ---------------------------------------------------------------------
		
	/**
	 * Get the next line from the .csv file
	 * @return The next line as a comma-delineated string
	 */
	public String getNext() {
		if(nextLine!=null) {
			String s = nextLine;
			nextLine = null;
			return s;
		}
		else {
			try {
				return reader.readLine();
			} catch (IOException e) {
				System.out.println("Couldn't read line.");
				e.printStackTrace();
				return "";
			}
		}
	}
	
	public boolean hasNext() {
		String s = null;
		boolean next = false;
		
		try {
			s = reader.readLine();
		} catch (IOException e) {
			System.out.println("Couldn't read file to test next line.");
			e.printStackTrace();
		}
		
		if(s != null) {
			next = true;
			nextLine = s;
		}
		
		return next;
	}
}
