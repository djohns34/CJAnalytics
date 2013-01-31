package edu.calpoly.codastjegga.sdk;

public enum EventType{
	Currency("CurrencyValue__c"),
	Float("FloatValue__c"),
	Integer("NumberValue__c"),
	Locale("LocaleValue__c"),
	Text("TextValue__c");
	
	private String row;

	private EventType(String row) {
			this.row=row;
	}
	public String getField(){
		return row;
	}
	public String getFieldType(){
		return getField().replace("Value__c", "");
	}
	
	public String toString(){
		return this.name();
	}
}