package edu.calpoly.codastjegga.cjanalyticsapp.datafetcher;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Pair;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import edu.calpoly.codastjegga.cjanalyticsapp.dashboard.Dashboard;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventFields;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventType;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Events;

/**
 * DataFetcher sends and retrieves data from Salesforce.com from
 * TrackedEvents__c custom object. It can get list of dbNames, list of metrics
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
  private static final String CUSTOM_OBJ_NAME = "TrackedEvents__c";

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
  public static List<Pair<String, EventType>> getDatabaseMetrics(String apiVersion,
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

  private static List<Pair<String, EventType>> parseMetricRecords(JSONObject recordsObj)
      throws JSONException {
    List<Pair<String, EventType>> result = new LinkedList<Pair<String, EventType>>();
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
          result.add(Pair.create(eventName, EventType.valueOf(eventType)));
          //add the event name to the list
          storedEvent.add(eventName);
        }
      }
    }
    return result;
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
  public static Events getDatabaseRecords(String apiVersion,
      RestClient client, String databaseName, String eventName,
      EventType eventType) throws Exception {
    // create a set of basic event fields
    Set<EventFields> eventFields = EnumSet.copyOf(BASIC_EVENT_FIELDS);
    // add the eventType to the basic set of event fields
    eventFields.add(eventType.getEventField());

    String getDBNameQuery = buildQuery(CUSTOM_OBJ_NAME, eventFields);
    //WHERE caluse for dbName and EventFields type
    String whereClause = WHERE + " " + EventFields.DatabaseName.getColumnId()
        + " = '" + databaseName + "'" + " " + AND + " "
        + EventFields.EventName.getColumnId() + " = '" + eventName + "'";
    //query to get the list of records from the db 
    String dbMetricQuery = getDBNameQuery + " " + whereClause;

    RestResponse reponse = sendSyncRequest(dbMetricQuery, apiVersion, client);

    JSONObject data;
    try {
      data = reponse.asJSONObject();
    } catch (ParseException ex) {
      throw new ParseException("Internal error, unable to parse response as JSON");
    }
    //create and return records created from the data
    return new Events(data);
  }
}
