package edu.calpoly.codastjegga.sdk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public abstract class Event<E> {

	
	
	private static final 		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
	protected Date timeStamp;
	protected String event;
	protected E value;
	
	
	
	String getTimeStamp(){
		return df.format(timeStamp);
	}
	
	public String getKey(){
		return event;
	}
	
	E getValue(){
		return value;
	}
	
	abstract String getRESTValue();
	
	abstract EventType getEventType();
	
}
