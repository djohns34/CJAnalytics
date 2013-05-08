package edu.calpoly.codastjegga.cjanalyticsapp.datafetcher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Pair;
import android.widget.Toast;

import com.google.gson.Gson;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.rest.RestRequest.RestMethod;

import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.dashboard.Dashboard;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventFields;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventSummary;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventType;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Events;
import edu.calpoly.codastjegga.cjanalyticsapp.utils.DateUtils;

/**
 * DataFetcher sends and retrieves data from Salesforce.com from
 * codastjegga__TrackedEvents__c custom object. It can get list of dbNames, list of metrics
 * in a database, and Record of events in a database.
 * 
 * @author Gagandeep Kohli
 * 
 */
public class DataFetcher {
  /** Constant for SELECT soql keyword **/
  private static final String SELECT = "SELECT";
  /** Constant for FROM soql keyword **/
  private static final String FROM = "FROM";
  /** Constant for WHERE soql keyword **/
  private static final String WHERE = "WHERE";
  /** Constant for GROUP_BY soql keyword **/
  private static final String GROUP_BY = "GROUP BY";
  /** Constant for HAVING soql keyword **/
  private static final String HAVING = "HAVING";
  /** Constant for ORDER_BY soql keyword **/
  private static final String ORDER_BY = "ORDER BY";
  /** Constant for LIMIT soql keyword **/
  private static final String LIMIT = "LIMIT";
  /** Constant for OFFSET soql keyword **/
  private static final String OFFSET = "OFFSET";
  /** Constant for AND soql keyword **/
  private static final String AND = "AND";
  /** Constant for EQUALS soql keyword **/
  private static final String EQUALS = "=";
  /** Constant for Custom object **/
  private static final String CUSTOM_OBJ_NAME = "codastjegga__TrackedEvents__c";
  
  private static final String REQUEST_PATH = "services/apexrest/codastjegga/DataSummarizer";
  private static final String MIME_JSON = "application/json";

  /** Constant for RECORDS **/
  private static final String RECORDS = "records";

  // Static set of custom event fields
  private static final EnumSet<EventFields> BASIC_EVENT_FIELDS = EnumSet.of(
      EventFields.DatabaseName, EventFields.DeviceId, EventFields.EventName,
      EventFields.TimestampV, EventFields.ValueType);

  /**
   * Helper for building soql query
   * 
   * @param from
   *          - FROM soql command
   * @param select
   *          set of EventFields to select
   * @return soql query
   * @throws IllegalArgumentException
   *           if from or select is null
   */
  //making it visible for test and allow user to create custom query
  public static String buildQuery(String from, Set<EventFields> select)
      throws IllegalArgumentException {
    if (from == null || select == null) {
      throw new IllegalArgumentException(
          "onRetrieve Error: objectType and/or fields is null");
    }
    // append SELECT to soql
    StringBuilder soql = new StringBuilder(SELECT + " ");

    Iterator<EventFields> columnIterator = select.iterator();
    // WHILE there are elements in columIterator
    while (columnIterator.hasNext()) {
      // append the EventField column
      soql.append(columnIterator.next().getColumnId());

      // add the comma delimiter if there are more EventFields to add
      if (columnIterator.hasNext())
        soql.append(", ");
    }
    // append the FROM command
    soql.append(" " + FROM + " " + from);
    // RETURN soql query
    return soql.toString();
  }

  /**
   * Make a sync request call
   * 
   * @param soql
   *          - soql query
   * @param apiVersion
   *          - api version
   * @param client
   *          @see {@link RestClient}
   **/
  private static RestResponse sendSyncRequest(String soql, String apiVersion,
      RestClient client) throws IOException {
    RestRequest restRequest = RestRequest.getRequestForQuery(apiVersion, soql);
    return client.sendSync(restRequest);

  }

  /**
   * Gets list of databases name
   * 
   * @param apiVersion
   *          rest api-version
   * @param client
   *          @see {@link RestClient}
   * @return List of string of databases name
   * @throws Exception
   *           If client fails to send request to salesforce.com or if
   *           Salesforce return back a invalid JSONObject in response.
   */
  public static List<Dashboard> getDatabasesName(String apiVersion,
      RestClient client) throws Exception {
    // query for list getting the database
    String dBNameQuery = buildQuery(CUSTOM_OBJ_NAME,
        EnumSet.of(EventFields.DatabaseName));
    // appened to query to group events by DB names
    String groupClause = GROUP_BY + " "
        + EventFields.DatabaseName.getColumnId();
    // Build the query for getting database name
    String getDBNameQuery = dBNameQuery + " " + groupClause;
    RestResponse reponse = sendSyncRequest(getDBNameQuery, apiVersion, client);

    JSONObject databases = null;
    try {
      databases = reponse.asJSONObject();
    } catch (Exception ex) {
      throw new Exception("Internal error, unable to parse response as JSON",
          ex);
    }
    // parse the reponse from salesforce and return in as a list
    return parseDBRecords(databases);
  }

  /**
   * Helper method to parse list of db names
   * 
   * @param recordsObj
   *          response records obj
   * @return List of DB's name
   * @throws JSONException
   *           if recordsObj is an invalid JSON obejection
   */
  private static List<Dashboard> parseDBRecords(JSONObject recordsObj)
      throws JSONException {
    List<Dashboard> result = new LinkedList<Dashboard>();
    if (recordsObj != null) {
      JSONArray jsonRecordsArr = recordsObj.getJSONArray(RECORDS);
      int recordLen = jsonRecordsArr.length();
      // FOR each event in records
      for (int recordIndx = 0; recordIndx != recordLen; ++recordIndx) {
        JSONObject recordItem = (JSONObject) jsonRecordsArr.get(recordIndx);
        String dbName = recordItem.getString(EventFields.DatabaseName
            .getColumnId());
        result.add(new Dashboard(dbName));

      }
    }
    return result;
  }

  /**
   * Gets list of metrics in a database
   * 
   * @param apiVersion
   *          salesforce REST apiversion
   * @param client
   *          @see {@link RestClient}
   * @param databaseName
   *          name of the database whose metrics to get
   * @return map of metrics along with their event type @see {@link EventType}
   * @throws Exception
   *           If client fails to send request to salesforce.com or if
   *           Salesforce return back a invalid JSONObjection in response.
   */
  public static List<Map.Entry<String, EventType>> getDatabaseMetrics(String apiVersion,
      RestClient client, String databaseName) throws Exception {
    String getDBNameQuery = buildQuery(CUSTOM_OBJ_NAME, EnumSet.of(
        EventFields.DatabaseName, EventFields.EventName, EventFields.ValueType));

    String whereClause = WHERE + " " + EventFields.DatabaseName.getColumnId()
        + " = '" + databaseName + "'";
    String dbMetricQuery = getDBNameQuery + " " + whereClause;
    
    RestResponse reponse = sendSyncRequest(dbMetricQuery, apiVersion, client);

    JSONObject databases;
    try {
      databases = reponse.asJSONObject();
    } catch (Exception ex) {
      throw new Exception("Internal error, unable to parse response as JSON",
          ex);
    }
    return parseMetricRecords(databases);
  }

  private static List<Map.Entry<String, EventType>> parseMetricRecords(JSONObject recordsObj)
      throws JSONException {
    List<Map.Entry<String, EventType>> result = new LinkedList<Map.Entry<String, EventType>>();
    HashSet<String> storedEvent = new HashSet<String>();
    if (recordsObj != null) {
      JSONArray jsonRecordsArr = recordsObj.getJSONArray(RECORDS);
      int recordLen = jsonRecordsArr.length();
      // FOR each event in records
      for (int recordIndx = 0; recordIndx != recordLen; ++recordIndx) {
        JSONObject recordItem = (JSONObject) jsonRecordsArr.get(recordIndx);
        //get the event name
        String eventName = recordItem.getString(EventFields.EventName
            .getColumnId());
        //IF the event is not added to the return list
        if (!storedEvent.contains(eventName)) {
          //get the event type
          String eventType = recordItem.getString(EventFields.ValueType
              .getColumnId());
          //put event name and type in result map
          result.add(new AbstractMap.SimpleEntry<String, EventType>(eventName, EventType.valueOf(eventType)));
          //add the event name to the list
          storedEvent.add(eventName);
        }
      }
    }
    return result;
  }

  /**
   * Creates the header required to request the specified {@link ChartSettings}  
   * @param setting
   * @return
   * @throws UnsupportedEncodingException 
   */
  private static StringEntity createRequestBody(ChartSettings setting) throws UnsupportedEncodingException{
    Map<String, String> fields = new HashMap<String, String>();
    fields.put("appName", setting.getDatabase());
    fields.put("eventName", setting.getEventName());
    fields.put("eventField", setting.getEventType().getEventField().getColumnId());
    
    Date startTime=setting.getStartDate();
    if(startTime == null){
      /*The beginning of time (for computers at least)*/
      startTime=new Date(0);
    } 
    else {
      startTime=DateUtils.setTime(startTime,0,0,0,0);
    }
    fields.put("startTime", DateUtils.format(startTime));
    
    Date endTime=setting.getEndDate();
    if(endTime == null){
      /*Go to right now*/
      endTime =new Date();
    } else {
      /*Data summarizer doesn't include last time in range.
      Must set the end time as midnight of the next day*/
      endTime=DateUtils.addDays(endTime, 1);
      endTime=DateUtils.setTime(endTime,0,0,0,0);
    }
    fields.put("endTime", DateUtils.format(endTime));
    
    fields.put("timeInterval", setting.getTimeInterval().getMilliSeconds().toString());
    
    
    StringEntity entity = new StringEntity(new JSONObject(fields).toString(), HTTP.UTF_8);
    entity.setContentType(MIME_JSON);
    
    return entity;
    
  }
  
  /**
   * Gets list of tracked events in a database by Event name and type
   *  @see {@link EventType}
   * @param apiVersion salesforce REST apiversion
   * @param client @see {@link RestClient}
   * @param databaseName name of the database whose events to get
   * @param eventName - name of the event 
   * @param eventType - type of event {@link EventType}
   * @return Records {@link Events}
   * @throws Exception
   *           If client fails to send request to salesforce.com or if
   *           Salesforce return back a invalid JSONObjection in response.
   */
  public static EventSummary getDatabaseRecords(String apiVersion,RestClient client, ChartSettings setting) throws Exception {

    EventSummary resp = null;
    
    RestRequest req = new RestRequest(RestMethod.POST, REQUEST_PATH, createRequestBody(setting));
    
    RestResponse response = client.sendSync(req);
    if (response.isSuccess()) {
      // All of the quotations are escaped, we don't want that.
      String responseString = response.asString().replace("\\", "");
      // The String also starts and ends with quotes.
      responseString = responseString.substring(1,
          responseString.length() - 1);
      

      resp = new Gson().fromJson(responseString,
          EventSummary.class);

  }
    
  if (resp == null || resp.getSummarized() == null || resp.getCategorical() == null) {
    throw new IOException("Unable to retrieve data");
  }
  
  return resp;

  }
}
