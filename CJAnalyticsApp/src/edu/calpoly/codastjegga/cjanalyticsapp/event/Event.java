package edu.calpoly.codastjegga.cjanalyticsapp.event;

/**
 * An Event represents a TrackedEvents Object.
 * @author gagandeep
 *
 */
public class Event {
  //id number of the event, given by Salesforce when creating an event
  private String id;
  //name of the event
  private String eventName;
  
  /**
   * Creates a new Event object with id and name
   * @param id id of the event
   * @param eventName name of the event
   */
  public Event (String id, String eventName) {
    this.id = id;
    this.eventName = eventName;
  }
  
  /**
   * id Setter
   * @param id id of the event
   */
  public void setId (String id) {
    this.id = id;
  }
  
  /**
   * Getter for id
   * @return id of the event
   */
  public String getId () {
    return id;
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
}
