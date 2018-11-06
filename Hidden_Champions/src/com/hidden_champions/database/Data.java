package com.hidden_champions.database;

import com.hidden_champions.database.containers.Firm;
import com.hidden_champions.database.containers.Stats;

public class Data {
	public static final int AKTIEBOLAG			=  49;
	
	public static final int ORGNR 				=   0;
	public static final int NAME 				=  27;
	public static final int JURFORM 			=   1;
	public static final int YEAR 				=   2;
	public static final int OWNER_STRUCTURE		=  21; //Koncernkod
	public static final int OWNER_COUNTRY		=  22; //�garens land
	
	public static final int REVENUE 			=  92; //Nettooms�ttning				rr01_ntoms
	public static final int VALUE_ADDED			= 123; //F�r�dlingsv�rde				rr04c_foradlv
	public static final int EMPLOYEES			=  99; //Antal anst�llda				bslov_antanst
	public static final int TOTAL_ASSETS		=  78; //Totala tillg�ngar				br09_tillgsu
	public static final int FIXED_ASSETS		=  76; //Anl�ggningstillg�ngar			br05_anltsu
	public static final int OPERATING_PROFIT	=  64; //R�relseresultat				rr07_rorresul
	public static final int FINANCIAL_INCOME	=  65; //Finansiella int�kter			rr08_finintk
	public static final int PROFIT_MARGIN		= 111; //Vinstmarginal					ny_vinstprc
	public static final int TURNOVER_RATE		= 101; //Kapitaloms�ttningshastighet	ny_kapomsh
	public static final int RETURN_ON_ASSETS	= 102; //Avkastning p� totalt kapital	ny_avktokap
	public static final int SOLIDITY			= 105; //Soliditet						ny_solid
	public static final int DEBT_EQUITY_RATIO	= 104; //Skulds�ttningsgrad				ny_skuldgrd
	
	public Firm createFirm(Stats data) {
		Firm firm = new Firm(data.getAttribute(Data.ORGNR),data.getAttribute(Data.NAME),data.getAttribute(Data.JURFORM));
		firm.addYear(data);
		return firm;
	}
	
	public void addYear(Firm firm, Stats data) {
		firm.addYear(data);
	}
}
