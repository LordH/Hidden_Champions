package com.hidden_champions.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import com.hidden_champions.containers.Data;
import com.hidden_champions.containers.Firm;
import com.hidden_champions.containers.Stats;

/**
 * Class for managing connection with and data insertion into the MySQL database
 * @author Harald
 *
 */
public class DatabaseManager {

	private static final int BATCH_SIZE = 6000;
	
	private static DatabaseManager instance;
	
	private String url 	= "jdbc:mysql://localhost:3306/hidden_champion?";
	private String mods = "user=LordH&password=1123581321aA!&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
	private Connection con;
	private LinkedList<Firm> firms;
	
	private DatabaseManager() {	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			System.out.println("Couldn't find database driver.");
			e1.printStackTrace();
		}
		firms = new LinkedList<>();
	}
	
	public static DatabaseManager instance() {
		if(instance == null) 
			instance = new DatabaseManager();
		return instance;
	}
	
	// ---------------------------------------------------------------------
	//	PRIVATE METHODS
	// ---------------------------------------------------------------------
	
	/**
	 * Method for inserting firms into the MySQL database
	 * @param firms A list with the firms to be inserted
	 * @throws SQLException If something goes wrong
	 */
	private void insertFirmData(LinkedList<Firm> firms) throws SQLException {
		connect();
				
		//Prepare statements for firm data insertion
		String addFirm = "INSERT INTO firms (id, name) VALUES (?, ?);";
		String addData = "INSERT INTO report "
				+ "(id, year, revenue, employees, total_assets, profit_margin, return_on_assets) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement firmPrep = con.prepareStatement(addFirm);
		PreparedStatement dataPrep = con.prepareStatement(addData);
		
		boolean ok = false;
		boolean insert = false;
		
		//Iterate over all firms in the list to add them to the batch insert
		for (Firm firm : firms) {
			
			//Test if firm should  be added
			ok = false;
			for (Stats stat : firm.getStats()) {
				String employees = stat.getAttribute(Data.EMPLOYEES);
				if(employees.isEmpty()) {
					continue;
				} else if(Integer.valueOf(employees)>499) {
					ok = true;
					break;
				}
			}
			if (!ok)
				continue;
			
			//Iterate over each firm's stats to add to the batch insert
			LinkedList<Stats> stats = firm.getStats();
			for (Stats stat : stats) {
				LinkedList<String> key_figures = new LinkedList<String>();
				key_figures.add(stat.getAttribute(Data.REVENUE));
				key_figures.add(stat.getAttribute(Data.EMPLOYEES));
				key_figures.add(stat.getAttribute(Data.TOTAL_ASSETS));
				key_figures.add(stat.getAttribute(Data.PROFIT_MARGIN));
				key_figures.add(stat.getAttribute(Data.RETURN_ON_ASSETS));
				
				//Check if data should be inserted (we don't want empty records)
				ok = false;
				for(String s : key_figures)
					if (s.isEmpty()) {
						ok = true;
						break;
					}
				if(!ok) {
					continue;
				}

				dataPrep.setString(1, firm.getOrgnr());
				dataPrep.setInt(2, Integer.valueOf(stat.getAttribute(Data.YEAR)));
				dataPrep.setDouble(3, Double.valueOf(stat.getAttribute(Data.REVENUE)));
				dataPrep.setDouble(4, Double.valueOf(stat.getAttribute(Data.EMPLOYEES)));
				dataPrep.setDouble(5, Double.valueOf(stat.getAttribute(Data.TOTAL_ASSETS)));
				dataPrep.setDouble(6, Double.valueOf(stat.getAttribute(Data.PROFIT_MARGIN)));
				dataPrep.setDouble(7, Double.valueOf(stat.getAttribute(Data.RETURN_ON_ASSETS)));
				dataPrep.addBatch();
				insert = true;
			}
			//Skip inserting firm if it had no valid data 
			if (!insert)
				continue;
			
			firmPrep.setString(1, firm.getOrgnr());
			firmPrep.setString(2, firm.getName());
			firmPrep.addBatch();
		}
		//Execute the batches and commit to the database
		con.setAutoCommit(false);
		firmPrep.executeBatch();
		dataPrep.executeBatch();
		con.commit();
		con.setAutoCommit(true);
		firms.clear();
		
		close();
	}
	
	private void connect() {
		try {
			con = DriverManager.getConnection(url+mods);
		} catch (SQLException e) {
			System.out.println("Failed to connect to database!");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void close() {
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("Failed to close connection properly.");
			e.printStackTrace();
		}
	}
	
	// ---------------------------------------------------------------------
	//	PUBLIC METHODS
	// ---------------------------------------------------------------------
	
	public void add(Firm newFirm) {
		if(newFirm.getJurform() == Data.AB) {
			firms.add(newFirm);
			if(firms.size() == DatabaseManager.BATCH_SIZE) {
				try {
					insertFirmData(firms);
				} catch (SQLException e) {
					System.out.println("Failed to insert firm data into database.");
					e.printStackTrace();
				}
			}
		}
	}
	
	public void finish() {
		try {
			insertFirmData(firms);
		} catch (SQLException e) {
			System.out.println("Failed to insert firm data into database.");
			e.printStackTrace();
		}
	}

	
}
