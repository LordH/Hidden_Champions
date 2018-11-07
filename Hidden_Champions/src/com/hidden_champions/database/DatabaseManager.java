package com.hidden_champions.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import com.hidden_champions.database.containers.Firm;
import com.hidden_champions.database.containers.Stats;

/**
 * Class for managing connection with and data insertion into the MySQL database
 * @author Harald
 *
 */
public class DatabaseManager {

	private static final int BATCH_SIZE = 6000;
	
	private static DatabaseManager instance;
	
	private String url 	= "jdbc:mysql://localhost:3306/hidden_champions?";
	private String mods = "user=dev&password=temporarypassword&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
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
				+ "(id, year, owner_structure, owner_country, revenue, "
				+ "value_added, employees, total_assets, fixed_assets, "
				+ "operating_profit, profit_margin, turnover_rate, return_on_assets, solidity, debt_equity_ratio) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement firmPrep = con.prepareStatement(addFirm);
		PreparedStatement dataPrep = con.prepareStatement(addData);
		
		boolean ok = false;
		boolean insert = false;
		int count = 0;
		
		//Iterate over all firms in the list to add them to the batch insert
		for (Firm firm : firms) {
			
			//Test if firm should  be added
			ok = false;
			for (Stats stat : firm.getStats()) {
				String employees = stat.getAttribute(Data.EMPLOYEES);
				String year = stat.getAttribute(Data.YEAR);
				if(employees.isEmpty()) {
					continue;
				} else if(Integer.valueOf(employees)>249 & Integer.valueOf(year)==2016) {
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
				key_figures.add(stat.getAttribute(Data.VALUE_ADDED));
				key_figures.add(stat.getAttribute(Data.EMPLOYEES));
				key_figures.add(stat.getAttribute(Data.TOTAL_ASSETS));
				key_figures.add(stat.getAttribute(Data.FIXED_ASSETS));
				key_figures.add(stat.getAttribute(Data.OPERATING_PROFIT));
				key_figures.add(stat.getAttribute(Data.FINANCIAL_INCOME));
				key_figures.add(stat.getAttribute(Data.PROFIT_MARGIN));
				key_figures.add(stat.getAttribute(Data.TURNOVER_RATE));
				key_figures.add(stat.getAttribute(Data.RETURN_ON_ASSETS));
				key_figures.add(stat.getAttribute(Data.SOLIDITY));
				key_figures.add(stat.getAttribute(Data.DEBT_EQUITY_RATIO));
				
				//Check if data should be inserted (we don't want empty records)
				ok = true;
				for(String s : key_figures)
					if (s.isEmpty()) {
						ok = false;
						break;
					}
				if(!ok) {
					continue;
				}
				
				//Firm info
				count = 1;
				dataPrep.setString(count++, firm.getOrgnr());
				dataPrep.setInt(count++, Integer.valueOf(stat.getAttribute(Data.YEAR)));
				dataPrep.setString(count++, stat.getAttribute(Data.OWNER_STRUCTURE));
				dataPrep.setString(count++, stat.getAttribute(Data.OWNER_COUNTRY));
				//Key figures
				dataPrep.setDouble(count++, Double.valueOf(stat.getAttribute(Data.REVENUE)));
				dataPrep.setDouble(count++, Double.valueOf(stat.getAttribute(Data.VALUE_ADDED)));
				dataPrep.setDouble(count++, Double.valueOf(stat.getAttribute(Data.EMPLOYEES)));
				dataPrep.setDouble(count++, Double.valueOf(stat.getAttribute(Data.TOTAL_ASSETS)));
				dataPrep.setDouble(count++, Double.valueOf(stat.getAttribute(Data.FIXED_ASSETS)));
				dataPrep.setDouble(count++, Double.valueOf(stat.getAttribute(Data.OPERATING_PROFIT)) + Double.valueOf(stat.getAttribute(Data.FINANCIAL_INCOME)));
				dataPrep.setDouble(count++, Double.valueOf(stat.getAttribute(Data.PROFIT_MARGIN)));
				dataPrep.setDouble(count++, Double.valueOf(stat.getAttribute(Data.TURNOVER_RATE)));
				dataPrep.setDouble(count++, Double.valueOf(stat.getAttribute(Data.RETURN_ON_ASSETS)));
				dataPrep.setDouble(count++, Double.valueOf(stat.getAttribute(Data.SOLIDITY)));
				dataPrep.setDouble(count++, Double.valueOf(stat.getAttribute(Data.DEBT_EQUITY_RATIO)));
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
		if(newFirm.getJurform() == Data.AKTIEBOLAG) {
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
