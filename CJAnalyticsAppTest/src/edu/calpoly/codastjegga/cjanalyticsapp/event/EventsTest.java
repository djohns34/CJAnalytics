package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Unit test for Records class
 * @author Gagandeep S. Kohli
 *
 */
public class EventsTest extends TestCase {

  private static final int totalRecords = 7;

  private static final String deviceId = "deviceId";

  private static final String timestamp = "2013-01-29T08:00:00.000+0000";

  private static final String databaseName = "dbName";

  private static final String cusNumEventName = "Level";
  private static final String levelV1 = "2";
  private static final String levelV2 = "3";
  private static final String levelV3 = "4";
  private static final int totalNumEvent = 3;

  private static final BigDecimal currValue = new BigDecimal(23.00);
  private static final Float floatV = new Float(1.99);
  private static final String textV = "textV";
  private static final String localV1 = "US";

  private static final String localEventName = "localeEventName";
  private static final int totalLocalEvent = 1;
  private static final String textEventName = "textEventName";
  private static final int totalTextEvent = 1;
  private static final String currEventName = "currencyEventName";
  private static final int totalCurrEvent = 1;
  private static final String floatEventName = "floatEventName";
  private static final int totalFloatEvent = 1;
  
  private static final String dbName = "DB Name";


  private static final String recordsString = "{\"records\":[" +
      "{\"codastjegga__DatabaseName__c\":\"" + dbName + "\", \"codastjegga__LocaleValue__c\":null,\"codastjegga__Device_Id__c\":\""+ deviceId + "\",\"codastjegga__Timestamp__c\":\"" + timestamp + "\",\"codastjegga__ValueType__c\":\"Number\",\"codastjegga__EventName__c\":\"" + cusNumEventName + "\",\"Id\":null,\"codastjegga__TextValue__c\":null,\"codastjegga__FloatValue__c\":null,\"codastjegga__NumberValue__c\":" + levelV1 +",\"codastjegga__CurrencyValue__c\":null}," +
      "{\"codastjegga__DatabaseName__c\":\"" + dbName + "\", \"codastjegga__LocaleValue__c\":null,\"codastjegga__Device_Id__c\":\""+ deviceId + "\",\"codastjegga__Timestamp__c\":\"" + timestamp + "\",\"codastjegga__ValueType__c\":\"Number\",\"codastjegga__EventName__c\":\"" + cusNumEventName +  "\",\"Id\":null,\"codastjegga__TextValue__c\":null,\"codastjegga__FloatValue__c\":null,\"codastjegga__NumberValue__c\":"+ levelV2 + ",\"codastjegga__CurrencyValue__c\":null}," +
      "{\"codastjegga__DatabaseName__c\":\"" + dbName + "\", \"codastjegga__LocaleValue__c\":null,\"codastjegga__Device_Id__c\":\"" + deviceId +"\",\"codastjegga__Timestamp__c\":\"" + timestamp + "\",\"codastjegga__ValueType__c\":\"Number\",\"codastjegga__EventName__c\":\""+ cusNumEventName + "\",\"Id\":null,\"codastjegga__TextValue__c\":null,\"codastjegga__FloatValue__c\":null,\"codastjegga__NumberValue__c\":" + levelV3 + ",\"codastjegga__CurrencyValue__c\":null}," +
      "{\"codastjegga__DatabaseName__c\":\"" + dbName + "\", \"codastjegga__LocaleValue__c\":\""+ localV1 + "\",\"codastjegga__Device_Id__c\":\"" + deviceId + "\",\"codastjegga__Timestamp__c\":\"" + timestamp + "\",\"codastjegga__ValueType__c\":\"Locale\",\"codastjegga__EventName__c\":\"" + localEventName + "\",\"Id\":null,\"codastjegga__TextValue__c\":null,\"codastjegga__FloatValue__c\":null,\"codastjegga__NumberValue__c\":null,\"codastjegga__CurrencyValue__c\":null}," +
      "{\"codastjegga__DatabaseName__c\":\"" + dbName + "\", \"codastjegga__LocaleValue__c\":null,\"codastjegga__Device_Id__c\":\"" + deviceId +"\",\"codastjegga__Timestamp__c\":\"" + timestamp + "\",\"codastjegga__ValueType__c\":\"Text\",\"codastjegga__EventName__c\":\"" + textEventName + "\",\"Id\":null,\"codastjegga__TextValue__c\":\"" + textV + "\",\"codastjegga__FloatValue__c\":null,\"codastjegga__NumberValue__c\":null,\"codastjegga__CurrencyValue__c\":null}," +
      "{\"codastjegga__DatabaseName__c\":\"" + dbName + "\", \"codastjegga__LocaleValue__c\":null,\"codastjegga__Device_Id__c\":\"" + deviceId + "\",\"codastjegga__Timestamp__c\":\"" + timestamp + "\",\"codastjegga__ValueType__c\":\"Float\",\"codastjegga__EventName__c\":\"" + floatEventName+ "\",\"Id\":null,\"codastjegga__TextValue__c\":null,\"codastjegga__FloatValue__c\":\"" + floatV + "\",\"codastjegga__NumberValue__c\":null,\"codastjegga__CurrencyValue__c\":null}," +
      "{\"codastjegga__DatabaseName__c\":\"" + dbName + "\", \"codastjegga__LocaleValue__c\":null,\"codastjegga__Device_Id__c\":\"" + deviceId + "\",\"codastjegga__Timestamp__c\":\"" + timestamp + "\",\"codastjegga__ValueType__c\":\"Currency\",\"codastjegga__EventName__c\":\"" + currEventName + "\",\"Id\":null,\"codastjegga__TextValue__c\":null,\"codastjegga__FloatValue__c\":null,\"codastjegga__NumberValue__c\":null,\"codastjegga__CurrencyValue__c\":\""+ currValue+ "\"}]}";

  private static JSONObject jsonRecords;

  private Events events;

  protected void setUp() throws Exception {
    super.setUp();
    try {
      jsonRecords = new JSONObject(recordsString);
    }catch (Exception ex)
    {
      fail("internal Exception");
    }

    events = new Events(jsonRecords);
  }

  public void testGetEvents(){
    List<Event> eventsList = events.getEvents();
    assertNotNull(eventsList);

    assertEquals(totalRecords, eventsList.size());
  }

  public void testAddEvent() {
    //adding one more event
    events.addEvent(new TextEvent("eventName", "deviceid", "timestamp", "dbName", "text"));
    assertEquals(totalRecords + 1, events.getEvents().size());
  }

  public void testAddRecords() {
    //adding one more event
    try {
      events.addEvents(jsonRecords);
      assertEquals(totalRecords * 2, events.getEvents().size());
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      fail("adding records should not throw an exception");
    }
  }

  public void testGetEventByName() {
    //test invalid name
    assertNull(events.getEvents("does not exist")); 
    //test for total num of our custom number events
    assertEquals(totalNumEvent, events.getEvents(cusNumEventName).size());
    //test the number of currency event
    assertEquals(totalCurrEvent, events.getEvents(currEventName).size());
    //test for total num of local Events
    assertEquals(totalLocalEvent, events.getEvents(localEventName).size());
    //test for total num of text events
    assertEquals(totalTextEvent, events.getEvents(textEventName).size());
    //test for total num of float events
    assertEquals(totalFloatEvent, events.getEvents(floatEventName).size());

    //adding new event for locale
    LocaleEvent localeEvent = new LocaleEvent(localEventName, deviceId, timestamp, databaseName, "NEW-LOCALE");

    events.addEvent(localeEvent);
    //test for total num of local Events
    assertEquals(totalLocalEvent + 1, events.getEvents(localEventName).size());

  }

  public void testInvalidAddEvents ()
  {
    String event = "{\"codastjegga__LocaleValue__c\":null,\"codastjegga__Device_Id__c\":\""+ deviceId + "\",\"codastjegga__Timestamp__c\":\"" + timestamp + "\",\"codastjegga__ValueType__c\":\"Number\",\"codastjegga__EventName__c\":\"" + cusNumEventName + "\",\"Id\":null,\"codastjegga__TextValue__c\":null,\"codastjegga__FloatValue__c\":null,\"codastjegga__NumberValue__c\":" + levelV1 +",\"codastjegga__CurrencyValue__c\":null},";
    JSONObject eventJson = null;
    try {
      eventJson = new JSONObject(event);
    } catch (JSONException e) {
      fail("internal error in creating an event json obj");
    }
    try {
      events.addEvents(eventJson);
      fail("adding invalid record, should thrown JSONException");
    } catch (JSONException e) {
      //passed
    }
  }

}
