package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.math.BigDecimal;

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
   * @param dataname name of the database
   * @param recordValue value of the record must match the recordType or IllegalArgumentException
   * is thrown. If record value is an illegal string represent of record type then NumberFormatException 
   * is thrown.
   * @throws IllegalArgumentException if recordValue doesn't match the recordType 
   * @throws NumberFormatException  if record value is an illegal string represent of record type
   * @return Event object
   */
  public static Event createEvent (String recordType, String eventName, 
      String deviceId, String timestamp, String databaseName, String recordValue) throws IllegalArgumentException, 
      NumberFormatException {

    switch (EventType.valueOf(recordType)) {
    case Currency:
      return createCurrencyEvent(eventName, deviceId, timestamp, databaseName, recordValue);
    case Float:
      return createFloatEvent(eventName, deviceId, timestamp, databaseName, recordValue);
    case Locale:
      return createLocaleEvent(eventName, deviceId, timestamp, databaseName, recordValue);
    case Number:
      return createNumberEvent(eventName, deviceId, timestamp, databaseName, recordValue);
    case Text:
      return createTextEvent(eventName, deviceId, timestamp, databaseName, recordValue);
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
      String deviceId, String timestamp, String databaseName, String recordValue) throws NumberFormatException {
    return new FloatEvent(eventName, deviceId, timestamp, databaseName, Float.valueOf(recordValue));

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
      String deviceId, String timestamp, String databaseName, String recordValue) {
    return new LocaleEvent(eventName, deviceId, timestamp, databaseName, recordValue);

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
      String deviceId, String timestamp, String databaseName, String recordValue) throws NumberFormatException {
    int num = (int)Math.round(Double.parseDouble(recordValue));
    return new NumberEvent(eventName, deviceId, timestamp, databaseName, num);

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
      String deviceId, String timestamp, String databaseName, String recordValue) {
    return new TextEvent(eventName, deviceId, timestamp, databaseName, recordValue);

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
      String deviceId, String timestamp, String databaseName, String recordValue) throws NumberFormatException {
    return new CurrencyEvent(eventName, deviceId, timestamp, databaseName, new BigDecimal(recordValue));

  }


}
