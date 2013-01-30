package edu.calpoly.codastjegga.sdk;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * Helper class to assist in the creation of event objects
 * 
 * @author Daniel
 *
 */
public class EventFactory {
	
	private static Map<String,EventType> typeMap= new HashMap<String,EventType>();
	
	/**
	 * Creates the event based on the given data
	 * 
	 * Mote: performs a check to verify only EventType is being stored for each key
	 * i.e can't store both strings and ints under a "Weight" Key
	 * 
	 * @param type the EventType of the object
	 * @param key 
	 * @param value Must be an appropriate object for the specified EventType 
	 * @return the created event or throws an exception if creation failed
	 */
	public static Event <?> createEvent(EventType type,String key,Object value){
		Event<?> e=null;
		
		if(!typeMap.containsKey(key)){
			typeMap.put(key, type);
		}else if(!typeMap.get(key).equals(type)){
            throw new IllegalArgumentException("The key'"+key+"'is already used by a "+type +" Event");
		}

        try {
            if (type == EventType.Currency) {
                e = new CurrencyEvent(key, (Double) value);
            } else if (type == EventType.Float) {
                    e = new FloatEvent(key, (Float) value);
            } else if (type == EventType.Integer) {
                e = new IntegerEvent(key, (Integer) value);
            } else if (type == EventType.Locale) {
                e = new LocaleEvent(key, (String) value);

            } else if (type == EventType.Text) {
                e = new TextEvent(key, (String) value);
            }
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(value.getClass().getName()
                    + " is the incorrect type for a " + type + " Event");
        }


		e.timeStamp=new Date();
		return e;
	}
}
