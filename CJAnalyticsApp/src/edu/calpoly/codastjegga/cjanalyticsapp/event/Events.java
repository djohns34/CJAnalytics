package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Records parses and stores JSON records in Events. 
 * @author Gagandeep S. Kohli
 *
 */
public class Events {

  /** Constant for RECORDS**/
  private static final String RECORDS = "records";
  /** Constant for custom object field tag **/
  private static final String __C = "__c";
  /** constant for value tag **/
  private static final String VALUE = "Value";
  /** constant for codastjegga **/
  private static final String CODASTJEGGA = "codastjegga__";

  /** map of list of events by name (key) **/
  private Map<String, List<Event>> eventsMap;

  /**
   * Constructs a list of empty records
   */
  public Events () {
    init(); 
  }
  /**
   * Constructs a list of records.
   * @param records json object that represents records from codastjegga__TrackedEvent__c object.
   * @throws JSONException if JSON record is an invalid JSON string 
   */
  public Events (JSONObject records) throws JSONException {
    init();
    parseEvents(records);
  }

  private void init() {
    eventsMap = new HashMap<String, List<Event>>();
  }

  /**
   * Adds set of records.
   * @param records json object that represents records from codastjegga__TrackedEvent__c object.
   * @throws JSONException  if JSON record is an invalid JSON string 
   */
  public void addEvents (JSONObject records) throws JSONException {
    parseEvents(records);
  }
  /**
   * Parses Event from JSON record objection
   * @param records JSONObject that represents records
   * @throws JSONException if records json object doesn't contain the correct 
   * (recordType, eventName, deviceID, timestamp, and recordValue) codastjegga__TrackedEvent__c object
   * fields.
   */
  private void parseEvents (JSONObject records) throws JSONException {

    if (records != null) {
      JSONArray jsonRecordsArr = records.getJSONArray(RECORDS);
      int recordLen = jsonRecordsArr.length();
      //FOR each event in records
      for (int recordIndx = 0; recordIndx != recordLen; ++recordIndx) {
        JSONObject recordItem = (JSONObject) jsonRecordsArr.get(recordIndx);
        //parse the event
        addEvent(parseEvent (recordItem));

      }
    }
  }

  /**
   * Getter for list of all events
   * @return list of all events or empty list of none exist
   */
  public List<Event> getEvents() {
    List<Event> eventList = new LinkedList<Event>();
    for (Entry<String, List<Event>> event : this.eventsMap.entrySet()) {
      eventList.addAll(event.getValue());
    }
    return eventList;
  }

  /**
   * Getter for list of events by event name
   * @param eventName list of events of null if events don't exist
   * @return
   */
  public List<Event> getEvents (String eventName) {
    return this.eventsMap.get(eventName);
  }

  /**
   * Adds and event into the record
   * @param event an event @see {@link Event}
   */
  public void addEvent (Event event) {
    //get the name of the event
    String eventName = event.getName();
    List<Event> events = eventsMap.get(eventName);
    //IF the list of events don't exist
    if (events == null)
      events = new LinkedList<Event>();
    //add the event to the list of events
    events.add(event);
    //put the list back in the event
    eventsMap.put(eventName, events);
  }



  /**
   * Parses a Event from JSON event objection
   * @param records JSONObject that represents records
   * @throws JSONException if records json object doesn't contain the correct 
   * (recordType, eventName, deviceID, timestamp, and recordValue) codastjegga__TrackedEvent__c object
   * fields.
   * @return an Event
   */
  private Event parseEvent (JSONObject aEvent) throws JSONException {

    String recordType  = aEvent.getString(EventFields.ValueType.getColumnId());
    String eventName    = aEvent.getString(EventFields.EventName.getColumnId());
    String deviceId    = aEvent.getString(EventFields.DeviceId.getColumnId());
    String timestamp   = aEvent.getString(EventFields.TimestampV.getColumnId());
    String recordValue = aEvent.getString(CODASTJEGGA + recordType + VALUE + __C);
    String dbName   = aEvent.getString(EventFields.DatabaseName.getColumnId());
    
    return EventFactory.createEvent(recordType, eventName, deviceId, timestamp, dbName, recordValue);

  }
}
