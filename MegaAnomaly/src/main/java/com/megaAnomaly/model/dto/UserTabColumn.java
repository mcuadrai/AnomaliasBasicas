package com.megaAnomaly.model.dto;

public class UserTabColumn {
	
	private String TABLE_NAME;
	private String COLUMN_NAME;
	
	public String getTABLE_NAME() {
		return TABLE_NAME;
	}
	public void setTABLE_NAME(String tABLE_NAME) {
		TABLE_NAME = tABLE_NAME;
	}
	public String getCOLUMN_NAME() {
		return COLUMN_NAME;
	}
	public void setCOLUMN_NAME(String cOLUMN_NAME) {
		COLUMN_NAME = cOLUMN_NAME;
	}
	
	public String toString() {
		return "tabla="+TABLE_NAME +":columna="+ COLUMN_NAME;
	}
	
	
	
	

}
