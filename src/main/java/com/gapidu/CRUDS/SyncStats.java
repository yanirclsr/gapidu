package com.gapidu.CRUDS;

import java.sql.Timestamp;

public class SyncStats {
	
	private int id;
	private int rowDataCount;
	private int dbCount;
	private int mktoCount;
	private int guaCount;
	private int sfdcCount;
	private long startTime;
	private long runTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRowDataCount() {
		return rowDataCount;
	}
	public void setRowDataCount(int rowDataCount) {
		this.rowDataCount = rowDataCount;
	}
	public int getDbCount() {
		return dbCount;
	}
	public void setDbCount(int dbCount) {
		this.dbCount = dbCount;
	}
	public int getMktoCount() {
		return mktoCount;
	}
	public void setMktoCount(int mktoCount) {
		this.mktoCount = mktoCount;
	}
	public int getGuaCount() {
		return guaCount;
	}
	public void setGuaCount(int guaCount) {
		this.guaCount = guaCount;
	}
	public int getSfdcCount() {
		return sfdcCount;
	}
	public void setSfdcCount(int sfdcCount) {
		this.sfdcCount = sfdcCount;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getRunTime() {
		return runTime;
	}
	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	Timestamp created;
	
}
