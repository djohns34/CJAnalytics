package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Records {

  
  private static final String RECORDS = "records";
  private static final String __C = "__c";
 
  private List<Event> recordList;
  
  public Records (JSONObject records) throws JSONException {
    if (records == null) 
      this.recordList =  Collections.emptyList();
    else {
      recordList = new LinkedList<Event>();
      parseRecords(records);
    }
  }
  
  private void parseRecords (JSONObject records) throws JSONException {
    if (records != null) {
      JSONArray jsonRecordsArr = records.getJSONArray(RECORDS);
      int recordLen = jsonRecordsArr.length();
      for (int recordIndx = 0; recordIndx != recordLen; ++recordIndx) {
        JSONObject recordItem = (JSONObject) jsonRecordsArr.get(recordIndx);
        recordList.add(parseRecord (recordItem));
      }
    }
  }
  
  private Event parseRecord (JSONObject aRecord) throws JSONException {
 
    String recordType  = aRecord.getString(EventFields.ValueType.getColumnId());
    String eventName    = aRecord.getString(EventFields.EventName.getColumnId());
    String deviceId    = aRecord.getString(EventFields.DeviceId.getColumnId());
    String timestamp   = aRecord.getString(EventFields.TimestampV.getColumnId());
    String recordValue = aRecord.getString(recordType + __C);
    
    return EventFactory.createEvent(recordType, eventName, deviceId, timestamp, recordValue);
    
  }
}
