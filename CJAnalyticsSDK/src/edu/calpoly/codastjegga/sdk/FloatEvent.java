package edu.calpoly.codastjegga.sdk;

import java.util.Locale;


public class FloatEvent extends Event<Float> {
	

	public FloatEvent(String key, Float value){
		this.event =key;
		this.value=value;
	}

	@Override
	public EventType getEventType(){
		return EventType.Float;
	}

	@Override
	String getRESTValue() {
		return String.format(Locale.US,"%.2f", value);
	}
}
