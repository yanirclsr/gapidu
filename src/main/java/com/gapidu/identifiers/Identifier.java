package com.gapidu.identifiers;

public class Identifier {

	private int identifierTypeId;
	private String identifier;
	
	public Identifier(int identifierTypeId, String identifier){
		
		this.identifierTypeId = identifierTypeId;
		this.identifier = identifier;
		
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public int getIdentifierTypeId() {
		return identifierTypeId;
	}
	public void setIdentifierTypeId(int identifierTypeId) {
		this.identifierTypeId = identifierTypeId;
	}
	

}
