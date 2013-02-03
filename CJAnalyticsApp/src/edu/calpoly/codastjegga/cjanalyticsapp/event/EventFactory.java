package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * EventFactory class are helper constructors for creating Event's.
 * @author Gagandeep S Kohli
 *
 */
public class EventFactory {
  //custom object tag
  private static final String __C = "__c";
  

  
  /**
   * Constructs a Event object that matches the recordType. Therefore, it is up to the
   * caller to cast the return object to the correct type.
   * @param recordType type of record
   * @param eventName name of the event
   * @param deviceId device id
   * @param timestamp timestamp of when the event was triggered
   * @param recordValue value of the record must match the recordType or IllegalArgumentException
   * is thrown. If record value is an illegal string represent of record type then NumberFormatException 
   * is thrown.
   * @throws IllegalArgumentException if recordValue doesn't match the recordType 
   * @throws NumberFormatException  if record value is an illegal string represent of record type
   * @return Event object
   */
  public static Event createEvent (String recordType, String eventName, 
      String deviceId, String timestamp, String recordValue) throws IllegalArgumentException, 
      NumberFormatException {

    switch (RecordType.valueOf(recordType)) {
    case Currency:
      return createCurrencyEvent(eventName, deviceId, timestamp, recordValue);
    case Float:
      return createFloatEvent(eventName, deviceId, timestamp, recordValue);
    case Locale:
      return createLocaleEvent(eventName, deviceId, timestamp, recordValue);
    case Number:
      return createNumberEvent(eventName, deviceId, timestamp, recordValue);
    case Text:
      return createTextEvent(eventName, deviceId, timestamp, recordValue);
    default:
      throw new IllegalArgumentException("Invalid recordType: " + recordType);
    }    
  }

  /**
   * Constructs a FloatEvent object that matches the recordType.
   * @param eventName name of the event
   * @param deviceId device id
   * @param timestamp timestamp of when the event was triggered
   * @param recordValue Float value of the record must be valid valid Float string or NumberFormatException
   * is thrown
   * @throws NumberFormatException if recordValue isn't valid Float string
   * @return FloatEvent object
   */
  public static FloatEvent createFloatEvent (String eventName, 
      String deviceId, String timestamp, String recordValue) throws NumberFormatException {
    return new FloatEvent(eventName, deviceId, timestamp, Float.valueOf(recordValue));

  }

  /**
   * Constructs a LocaleEvent object that matches the recordType.
   * @param eventName name of the event
   * @param deviceId device id
   * @param timestamp timestamp of when the event was triggered
   * @param recordValue locale value of the record 
   * @return LocaleEvent object
   */
  public static LocaleEvent createLocaleEvent (String eventName, 
      String deviceId, String timestamp, String recordValue) {
    return new LocaleEvent(eventName, deviceId, timestamp, recordValue);

  }

  /**
   * Constructs a FloatEvent object that matches the recordType.
   * @param eventName name of the event
   * @param deviceId device id
   * @param timestamp timestamp of when the event was triggered
   * @param recordValue Number value of the record must be valid valid Integer string or NumberFormatException
   * is thrown
   * @throws NumberFormatException if recordValue isn't valid Integer string
   * @return NumberEvent object
   */
  public static NumberEvent createNumberEvent (String eventName, 
      String deviceId, String timestamp,  String recordValue) throws NumberFormatException {
    return new NumberEvent(eventName, deviceId, timestamp, Integer.valueOf(recordValue));

  }

  /**
   * Constructs a TextEvent object that matches the recordType.
   * @param eventName name of the event
   * @param deviceId device id
   * @param timestamp timestamp of when the event was triggered
   * @param recordValue String value of the record 
   * @return TextEvent object
   */
  public static TextEvent createTextEvent (String eventName, 
      String deviceId, String timestamp, String recordValue) {
    return new TextEvent(eventName, deviceId, timestamp, recordValue);

  }
  
  /**
   * Constructs a CurrencyEvent object that matches the recordType.
   * @param eventName name of the event
   * @param deviceId device id
   * @param timestamp timestamp of when the event was triggered
   * @param recordValue BigDecimal value of the record 
   * @throws NumberFormatException  if val does not contain a valid string representation of a big decimal.  
   * @return CurrencyEvent object
   */
  public static CurrencyEvent createCurrencyEvent (String eventName, 
      String deviceId, String timestamp, String recordValue) throws NumberFormatException {
    return new CurrencyEvent(eventName, deviceId, timestamp, new BigDecimal(recordValue));

  }


}
