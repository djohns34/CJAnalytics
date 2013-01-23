package edu.calpoly.codastjegga.sdk;


public class TextEvent extends Event<String> {

	public TextEvent(String key, String value){
		this.event =key;
		this.value=value;
	}

	@Override
	public EventType getEventType(){
		return EventType.Text;
	}

	@Override
	String getRESTValue() {
		return value;
	}
}
