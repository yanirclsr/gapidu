package com.gapidu.CRUDS;

import java.sql.Timestamp;

public class Visitors {
	
	private int id;
	private int accountId;
	private String vid;
	private int idsCount;
	private String landingPage;
	private String referral;
	private String userAgentId;
	private String ip;
	private int dayBucket;
	private boolean engaged;
	private Timestamp created;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public int getIdsCount() {
		return idsCount;
	}
	public void setIdsCount(int idsCount) {
		this.idsCount = idsCount;
	}
	public String getLandingPage() {
		return landingPage;
	}
	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}
	public String getReferral() {
		return referral;
	}
	public void setReferral(String referral) {
		this.referral = referral;
	}
	public String getUserAgentId() {
		return userAgentId;
	}
	public void setUserAgentId(String userAgentId) {
		this.userAgentId = userAgentId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getDayBucket() {
		return dayBucket;
	}
	public void setDayBucket(int dayBucket) {
		this.dayBucket = dayBucket;
	}
	public boolean isEngaged() {
		return engaged;
	}
	public void setEngaged(boolean engaged) {
		this.engaged = engaged;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	
}
