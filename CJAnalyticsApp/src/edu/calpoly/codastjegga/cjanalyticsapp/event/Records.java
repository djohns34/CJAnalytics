package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Records parses and stores JSON records in Events. 
 * @author Gagandeep S. Kohli
 *
 */
public class Records {

  /** Constant for RECORDS**/
  private static final String RECORDS = "records";
  /** Constant for custom object field tag **/
  private static final String __C = "__c";
  /** constant for value tag **/
  private static final String VALUE = "Value";
 
  /** list of events **/
  private List<Event> eventList;
  
  /**
   * Constructs a list of records.
   * @param records json string that represents records from TrackedEvent__c object.
   * @throws JSONException if JSON record is an invalid JSON string 
   */
  public Records (JSONObject records) throws JSONException {
    //IF records is null construct an empty list
    if (records == null) 
      this.eventList =  Collections.emptyList();
    else {
      eventList = new LinkedList<Event>();
      parseRecords(records);
    }
  }
  
  /**
   * Parses Event from JSON record objection
   * @param records JSONObject that represents records
   * @throws JSONException if records json object doesn't contain the correct 
   * (recordType, eventName, deviceID, timestamp, and recordValue) TrackedEvent__c object
   * fields.
   */
  private void parseRecords (JSONObject records) throws JSONException {
   
    if (records != null) {
      JSONArray jsonRecordsArr = records.getJSONArray(RECORDS);
      int recordLen = jsonRecordsArr.length();
      //FOR each event in records
      for (int recordIndx = 0; recordIndx != recordLen; ++recordIndx) {
        JSONObject recordItem = (JSONObject) jsonRecordsArr.get(recordIndx);
        //parse and add the event to eventlist
        eventList.add(parseEvent (recordItem));
      }
    }
  }
  
  /**
   * Parses a Event from JSON event objection
   * @param records JSONObject that represents records
   * @throws JSONException if records json object doesn't contain the correct 
   * (recordType, eventName, deviceID, timestamp, and recordValue) TrackedEvent__c object
   * fields.
   * @return an Event
   */
  private Event parseEvent (JSONObject aEvent) throws JSONException {
 
    String recordType  = aEvent.getString(EventFields.ValueType.getColumnId());
    String eventName    = aEvent.getString(EventFields.EventName.getColumnId());
    String deviceId    = aEvent.getString(EventFields.DeviceId.getColumnId());
    String timestamp   = aEvent.getString(EventFields.TimestampV.getColumnId());
    String recordValue = aEvent.getString(recordType + VALUE + __C);
    
    return EventFactory.createEvent(recordType, eventName, deviceId, timestamp, recordValue);
    
  }
}
