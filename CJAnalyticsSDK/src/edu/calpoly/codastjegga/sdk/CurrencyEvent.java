package edu.calpoly.codastjegga.sdk;

import java.util.Locale;


public class CurrencyEvent extends Event<Double> {
	

	public CurrencyEvent(String key, Double value){
		this.event =key;
		this.value=value;
	}

	@Override
	public EventType getEventType(){
		return EventType.Currency;
	}

	@Override
	String getRESTValue() {
		return String.format(Locale.US,"%.2f", value);
	}
}
