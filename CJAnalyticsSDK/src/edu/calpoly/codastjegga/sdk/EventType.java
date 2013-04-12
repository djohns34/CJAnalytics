package edu.calpoly.codastjegga.sdk;

public enum EventType{
	Currency("codastjegga__CurrencyValue__c"),
	Float("codastjegga__FloatValue__c"),
	Integer("codastjegga__NumberValue__c"),
	Locale("codastjegga__LocaleValue__c"),
	Text("codastjegga__TextValue__c");
	
	private String row;

	private EventType(String row) {
			this.row=row;
	}
	public String getField(){
		return row;
	}
	public String getFieldType(){
		return getField().replace("codastjegga__", "").replace("Value__c", "");
	}
	
	public String toString(){
		return this.name();
	}
}
