package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

/**
 * An Event represents a TrackedEvents Object.
 * @author gagandeep
 *
 */
public class Event {
  //data formatter
  private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
  
  //name of the event
  private String eventName;
  //device id that triggered the event
  private String deviceId;
  //the time the event was triggered
  private Date timestamp;
  //
  
  /**
   * Creates a new Event object with name, device id and timestamp
   * @param eventName name of the event
   * @param deviceId device id that triggered the event
   * @param timestamp the time the event was triggered
   */
  public Event (String eventName, String deviceId, String timestamp) {
    this.eventName = eventName;
    this.deviceId = deviceId;
    try {
      this.timestamp = df.parse(timestamp);
    } catch (ParseException e) {
      Log.e("Event", "Unable to parse timestamp, setting null");
      this.timestamp = null;
    }
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
   * Getter for timestamp
   * @return timestamp of the event
   */
  public Date getTimestamp () {
    return timestamp;
  }
  
  
}
