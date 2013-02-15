package edu.calpoly.codastjegga.cjanalyticsapp.datafetcher;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.json.JSONObject;
import org.mockito.Mockito;

import com.salesforce.androidsdk.auth.HttpAccess;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventFields;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventType;
import edu.calpoly.codastjegga.cjanalyticsapp.event.NumberEvent;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Records;

/**
 * Test class for DataFetcher
 * @author Gagandeep Kohli
 *
 */
public class DataFetcherTest extends TestCase {

  RestRequest request;
  AsyncRequestCallback callback;
  RestClient client;
  final String trackObj = "TrackedEvent__c";
  final EventFields field = EventFields.CurrencyV;
  final String apiV = "TestApi";
  final String ANGRYBIRD = "Angry Bird";
  final String TEMPLERUN = "Temple Run";
  final String METRIC0 = "Level";
  final String METRIC1 = "TimePlayed";
  final EventType METRICTYPE = EventType.Number;
  final String DEVICEID = "fba473108a269383";
  final String TIMESTAMP = "2013-01-29T08:00:00.000+0000";
  final int NUMBERV = 1;

  /** Constant for RECORDS**/
  private static final String RECORDS = "records";

  //set up the mocks
  protected void setUp() throws Exception {
    super.setUp();
    request = Mockito.mock(RestRequest.class);
    callback = Mockito.mock(AsyncRequestCallback.class);
    client = Mockito.mock(RestClient.class);

  }

  public void testQueryBuilder() {
    try {
      DataFetcher.buildQuery(null, null);
      fail("DataFetcher queryBuilder failed to throw illegal Argument excpetion since both param are null");
    }catch (IllegalArgumentException exp) {
      //passed
    }

    try {
      DataFetcher.buildQuery(null, EnumSet.noneOf(EventFields.class));
      fail("DataFetcher queryBuilder failed to throw illegal Argument excpetion since api ver param is null");
    }catch (IllegalArgumentException exp) {
      //passed
    }

    try {
      DataFetcher.buildQuery("1", null);
      fail("DataFetcher queryBuilder failed to throw illegal Argument excpetion since field param is null");
    }catch (IllegalArgumentException exp) {
      //passed
    }

  }

  public void testSendSyncRequest() {

  }

  public void testGetDatabasesName () {

    RestResponse requestMock = Mockito.mock(RestResponse.class);
    String[] dbName = {ANGRYBIRD, TEMPLERUN};

    //build the JSON response
    StringBuffer dbRecords = new StringBuffer("{" + RECORDS + ":[");
    for (String name : dbName) {
      String db = "{" + EventFields.DatabaseName.getColumnId() + ":\"" + name + "\" },";  
      dbRecords.append(db);
    }
    //remove the last comma
    dbRecords.deleteCharAt(dbRecords.length() - 1);
    dbRecords.append("]}");


    JSONObject records;
    try {
      records = new JSONObject(dbRecords.toString());

      Mockito.when(client.sendSync(Mockito.any(RestRequest.class))).thenReturn(requestMock);
      Mockito.when(requestMock.asJSONObject()).thenReturn(records);
    }catch (Exception exp) {
      // call to mock  shouldn't fail or creation of JSON
      fail("Internal error");
    }

    try {
      List<String> dbNameList = DataFetcher.getDatabasesName(apiV, client);
      //check the list is not null
      assertNotNull(dbNameList);
      //check the length of the string
      assertEquals(dbName.length, dbNameList.size());
      for (String db : dbName)
        //check if the list contains the name
        assertTrue(dbNameList.contains(db));
    } catch (Exception e) {
      fail("Internal error");
    }
  }

  public void testGetDatabaseMetrics() {
    RestResponse requestMock = Mockito.mock(RestResponse.class);

    String [] metrics = {METRIC0, METRIC1};
    String dbName = ANGRYBIRD; 

    //build the JSON response
    StringBuffer dbRecords = new StringBuffer("{" + RECORDS + ":[");
    for (String metric : metrics) {
      String db = "{\"" + EventFields.ValueType.getColumnId() + "\":\"" + METRICTYPE + "\"," + 
          "\"" + EventFields.EventName.getColumnId() + "\":\"" + metric     + "\"," + 
          "\"" + EventFields.DatabaseName.getColumnId() + "\":\"" + dbName + "\"},"; 
      dbRecords.append(db);
    }
    //remove the last comma
    dbRecords.deleteCharAt(dbRecords.length() - 1);
    dbRecords.append("]}");


    JSONObject records;
    try {
      records = new JSONObject(dbRecords.toString());

      Mockito.when(client.sendSync(Mockito.any(RestRequest.class))).thenReturn(requestMock);
      Mockito.when(requestMock.asJSONObject()).thenReturn(records);
    }catch (Exception exp) {
      // call to mock  shouldn't fail or creation of JSON
      fail("Internal error");
    }

    try {
      Map<String, EventType> dbMetrics = DataFetcher.getDatabaseMetrics(apiV, client, dbName);
      //check that the hash isn't null
      assertNotNull(dbMetrics);
      //check the length of the hash
      assertEquals(metrics.length, dbMetrics.size());
      for (String metric : metrics) {
        //check if db metrics contains the metric
        assertTrue(dbMetrics.containsKey(metric));
        //get the event type (value) of the metric
        EventType value = dbMetrics.get(metric);
        //check the value
        assertEquals(METRICTYPE, value);
      }
    } catch (Exception e) {
      fail("Internal error");
    }

  }

  public void testGetRecords() {

    RestResponse requestMock = Mockito.mock(RestResponse.class);
    //please do not change the order of these array ... test below depended on the order of it
    String metrics = METRIC0;
    String dbName = ANGRYBIRD;


    //build the JSON response
    StringBuffer dbRecords = new StringBuffer("{" + RECORDS + ":[");

    String db = "{\"" + EventFields.ValueType.getColumnId() + "\":\"" + METRICTYPE + "\"," + 
        "\"" + EventFields.EventName.getColumnId() + "\":\"" + metrics     + "\"," +
        "\"" + EventFields.DeviceId.getColumnId() + "\":\"" + DEVICEID     + "\"," +
        "\"" + METRICTYPE.getEventField().getColumnId() + "\":\"" + NUMBERV     + "\"," +
        "\"" + EventFields.TimestampV.getColumnId() + "\":\"" + TIMESTAMP     + "\"," +
        "\"" + EventFields.DatabaseName.getColumnId() + "\":\"" + dbName + "\"},"; 
    dbRecords.append(db);

    //remove the last comma
    dbRecords.deleteCharAt(dbRecords.length() - 1);
    dbRecords.append("]}");


    JSONObject records;
    try {
      records = new JSONObject(dbRecords.toString());

      Mockito.when(client.sendSync(Mockito.any(RestRequest.class))).thenReturn(requestMock);
      Mockito.when(requestMock.asJSONObject()).thenReturn(records);
    }catch (Exception exp) {
      // call to mock  shouldn't fail or creation of JSON
      fail("Internal error");
    }


    try {
      //testing ANGRYBIRD 

      Records rec = DataFetcher.getDatabaseRecords(apiV, client, dbName,null, METRICTYPE);
      //check that the hash isn't null
      assertNotNull(rec);
      //getting the list of events
      List<Event> events = rec.getEvents();
      assertNotNull(events);
      //check the length of the event
      assertEquals(1, events.size());

      //get metric one event
      NumberEvent actualEvent = (NumberEvent)events.get(0);
      NumberEvent expectedEvent = new NumberEvent(metrics, DEVICEID, TIMESTAMP, dbName, NUMBERV); 

      checkNumberEvent(expectedEvent, actualEvent);

    } catch (Exception e) {
      fail("Internal error");
    }
  }

  private void checkNumberEvent(NumberEvent expected, NumberEvent actual) {
    //check name
    assertEquals(expected.getEventName(), actual.getEventName());
    //check db
    assertEquals(expected.getDatabaseName(), actual.getDatabaseName());
    //device id
    assertEquals(expected.getDeviceId(), actual.getDeviceId());
    //check number value
    assertEquals(expected.getNumberValue(), actual.getNumberValue());
    //check timestamp
    assertEquals(expected.getTimestamp(), actual.getTimestamp());
  }
}
