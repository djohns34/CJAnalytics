package edu.calpoly.codastjegga.sdk;

import java.util.Locale;


public class IntegerEvent extends Event<Integer> {
	

	public IntegerEvent(String key, Integer value){
		this.event =key;
		this.value=value;
	}

	@Override
	public EventType getEventType(){
		return EventType.Integer;
	}

	@Override
	String getRESTValue() {
		return String.format(Locale.US,"%d", value);
	}
}
