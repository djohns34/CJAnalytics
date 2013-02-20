package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import edu.calpoly.codastjegga.cjanalyticsapp.utils.DateUtils;

/**
 * An Event represents a TrackedEvents Object.
 * @author Gagandeep S. Kohli
 *
 */
public class Event {
  
  private static String EVENTNAME = "eventname";
  private static String DEVICEID = "deviceid";
  private static String TIMPSTAMP = "timestamp";
  private static String DATABASENAME = "databasename";
  
  //name of the event
  private String eventName;
  //device id that triggered the event
  private String deviceId;
  //the time the event was triggered
  private Date timestamp;
  //name of the database that this event is part of
  private String databaseName;

  //
  static final long NOT_PERSISTED=-1l;
  
  
  /**
   * Creates a new Event object with name, device id and timestamp
   * @param eventName name of the event
   * @param deviceId device id that triggered the event
   * @param timestamp the time the event was triggered
   */
  public Event (String eventName, String deviceId, String timestamp) {
    this.eventName = eventName;
    this.deviceId = deviceId;
    this.timestamp = DateUtils.parse(timestamp);
    setDatabaseName(null);
  }
  
  /**
   * Creates a new Event object with name, device id, db name and timestamp
   * @param eventName name of the event
   * @param deviceId device id that triggered the event
   * @param name of the db where the event is stored or belongs to
   * @param timestamp the time the event was triggered
   */
  public Event (String eventName, String deviceId, String timestamp, String databaseName) {
    this.eventName = eventName;
    this.deviceId = deviceId;
    setTimestamp(timestamp);
    setDatabaseName(databaseName);
  }
  
  
  /**
   * Setter for event name
   * @param eventName name of the event
   */
  public void setEventName (String eventName) {
    this.eventName = eventName;
  }
  
  /**
   * Getter for event name
   * @return name of the event
   */
  public String getEventName () {
    return eventName;
  }
  
  /**
   * device id Setter
   * @param devideID device id
   */
  public void setDeviceId (String deviceId) {
    this.deviceId = deviceId;
  }
  
  /**
   * Getter for device id
   * @return device id that triggered the event
   */
  public String getDeviceId () {
    return deviceId;
  }
  
  /**
   * timestamp Setter
   * @param timestamp timestamp of when the event was recorded
   */
  public void setTimestamp (Date timestamp) {
    this.timestamp = timestamp;
  }
  
  /**
   * timestamp Setter
   * @param timestamp timestamp of when the event was recorded
   */
  public void setTimestamp (String timestamp) {
    this.timestamp = DateUtils.parse(timestamp);
  }
  
  /**
   * Getter for timestamp
   * @return timestamp of the event
   */
  public Date getTimestamp () {
    return timestamp;
  }
  
  
  /**
   * database name Setter
   * @param databaseName name of the database that event is associated with
   */
  public void setDatabaseName (String databaseName) {
    this.databaseName = databaseName;
  }
  
  /**
   * Getter for database name
   * @return database name
   */
  public String getDatabaseName () {
    return this.databaseName;
  }
  
  protected JSONObject getJSONObject() {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put(EVENTNAME, eventName);
    map.put(DEVICEID, deviceId);
    map.put(TIMPSTAMP, timestamp.toString());
    map.put(DATABASENAME, databaseName);
    
    return new JSONObject(map);
  }
  /**
   * Returns "{eventname:"eventName", deviceid:"deviceid", timestamp:"timestamp"}
   */
  @Override
  public String toString() {
    return getJSONObject().toString();
  }
  
}
