package edu.calpoly.codastjegga.sdk;


public class LocaleEvent extends Event<String> {

	public LocaleEvent(String key, String value){
		this.event =key;
		this.value=value;
	}

	@Override
	public EventType getEventType(){
		return EventType.Locale;
	}

	@Override
	String getRESTValue() {
		return value;
	}
}
