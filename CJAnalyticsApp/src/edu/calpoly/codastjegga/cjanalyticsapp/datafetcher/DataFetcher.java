package edu.calpoly.codastjegga.cjanalyticsapp.datafetcher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import edu.calpoly.codastjegga.cjanalyticsapp.event.EventFields;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventType;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Records;

/**
 * DataFetcher sends and retrieves data from Salesforce.com
 * based on the requested soql query.
 * @author Gagandeep Kohli
 *
 */
public class DataFetcher {
  /** Constant for SELECT soql keyword**/
  private static final String SELECT = "SELECT";
  /** Constant for FROM soql keyword**/
  private static final String FROM = "FROM";
  /** Constant for WHERE soql keyword**/
  private static final String WHERE = "WHERE";
  /** Constant for GROUP_BY soql keyword**/
  private static final String GROUP_BY = "GROUP BY";
  /** Constant for HAVING soql keyword**/
  private static final String HAVING = "HAVING";
  /** Constant for ORDER_BY soql keyword**/
  private static final String ORDER_BY = "ORDER BY";
  /** Constant for LIMIT soql keyword**/
  private static final String LIMIT = "LIMIT";
  /** Constant for OFFSET soql keyword**/
  private static final String OFFSET = "OFFSET";
  /** Constant for EQUALS soql keyword**/
  private static final String EQUALS = "=";
  
  private static final String CUSTOM_OBJ_NAME = "TrackedEvents__c";
  
  //Static set of custom event fields
  private static final EnumSet<EventFields> BASIC_EVENT_FIELDS = EnumSet.of(EventFields.DatabaseName, 
                                                                        EventFields.DeviceId, 
                                                                        EventFields.EventName, 
                                                                        EventFields.TimestampV, 
                                                                        EventFields.ValueType);
  //RestClient - Salesforce RESTApi wrapper
  private RestClient client;
  //api version
  private String apiVersion;
  
  /**
   * Construct an DataFetcher that routes soql calls to the rest client
   * using the api version requested.
   * @param restClient {@link RestClient}
   * @param apiVersion api version to use when making the soql calls
   * @throws IllegalArgumentException if restClient or api version is null or apiversion is empty
   */
  public DataFetcher (RestClient restClient, String apiVersion) throws IllegalArgumentException{
    if (restClient == null || apiVersion == null || apiVersion.length() == 0) {
      throw new IllegalArgumentException("Invalid parameters");
    }
    this.client = restClient;
    this.apiVersion = apiVersion;
  }
//  
//  /**
//   * Performs a soql query request asynchronous . Caller should use the callback to handle the returned data.
//   * @param soql soql query
//   * @param callback @see {@link AsyncRequestCallback}
//   * @throws UnsupportedEncodingException @see {@link RestRequest#getRequestForQuery(String, String)}
//   */
//  public void onSoqlQuery (String soql, AsyncRequestCallback callback) 
//      throws  UnsupportedEncodingException{
//    sendRequest(soql, callback);
//  }
  
  /**
   * Helper for building soql query
   * @param from - FROM soql command
   * @param select set of EventFields to select
   * @return soql query
   * @throws IllegalArgumentException if from or select is null
   */
  public static String buildQuery (String from, Set<EventFields> select) 
      throws IllegalArgumentException
  {
    if (from == null || select == null) {
      throw new IllegalArgumentException("onRetrieve Error: objectType and/or fields is null");
    }
    //append SELECT to soql
    StringBuilder soql = new StringBuilder(SELECT + " ");
    
    Iterator<EventFields> columnIterator = select.iterator();
    //WHILE there are elements in columIterator
    while(columnIterator.hasNext()) {
      //append the EventField column
      soql.append(columnIterator.next().getColumnId());
     
      //add the comma delimiter if there are more EventFields to add
      if (columnIterator.hasNext())
         soql.append(", ");
    }
    //append the FROM command
    soql.append(" " + FROM + " " + from);
    //RETURN soql query
    return soql.toString();
  }
  
  public static void getAllData (String apiVersion, RestClient client, 
      AsyncRequestCallback callback) {
    Set<EventFields> allFields = EnumSet.allOf(EventFields.class);  
    
    String soql = buildQuery(CUSTOM_OBJ_NAME, allFields);
    
  }
  
  /**
   * Carries out the soql call
   * @param soql soql query
   * @param callback @see {@link AsyncRequestCallback}
   * @throws UnsupportedEncodingException if soql query is invalid
   */
  public void sendAsyncRequest(String soql, AsyncRequestCallback callback) throws UnsupportedEncodingException {
    RestRequest restRequest = RestRequest.getRequestForQuery(apiVersion, soql);    
    client.sendAsync(restRequest, callback); 
  }
  
  public static RestResponse sendSyncRequest(String soql, String apiVersion, RestClient client) throws IOException {
    RestRequest restRequest = RestRequest.getRequestForQuery(apiVersion, soql);    
    return client.sendSync(restRequest); 
     
  }
  
  //soql to get databases
  public static List<String> getDatabasesName (String apiVersion, RestClient client) throws Exception {
    String getDBNameQuery = buildQuery(CUSTOM_OBJ_NAME, EnumSet.of(EventFields.DatabaseName));
    RestResponse reponse = sendSyncRequest(getDBNameQuery, apiVersion, client);
    
    JSONObject databases = null;
    try {
      databases = reponse.asJSONObject();
    } catch (Exception ex) {
      throw new Exception("Internal error, unable to parse response as JSON", ex);
    }
    
    return parseDBRecords(databases);
  }
  
  private static List<String> parseDBRecords(JSONObject recordsObj) {
    List<String> result = null;
    
    return result;
  }
  
  //get database records
  public static Map<String, EventType> getDatabaseMetrics (String apiVersion, RestClient client, String databaseName) throws Exception {
    String getDBNameQuery = buildQuery(CUSTOM_OBJ_NAME, EnumSet.of(EventFields.DatabaseName));
    
    String whereClause = WHERE + " " + EventFields.DatabaseName.getColumnId() + " = " + databaseName;
    
    String dbMetricQuery = getDBNameQuery + " " + whereClause;
    RestResponse reponse = sendSyncRequest(dbMetricQuery, apiVersion, client);
    
    JSONObject databases;
    try {
      databases = reponse.asJSONObject();
    } catch (Exception ex) {
      throw new Exception("Internal error, unable to parse response as JSON", ex);
    }
    return parseMetricRecords (databases);
  }
  
  private static Map<String, EventType> parseMetricRecords(JSONObject recordsObj) {
    HashMap<String, EventType> result;
    
    return result;
  }
  //event name and type GROUP_BY 
//get database records
  public static Records getRecords (String apiVersion, RestClient client, String databaseName, String eventName, EventType eventType) throws Exception {
    //create a set of basic event fields
    Set<EventFields> eventFields = EnumSet.copyOf(BASIC_EVENT_FIELDS);
    //add the eventType to the basic set of event fields
    eventFields.add(eventType.getEventField());
    
    String getDBNameQuery = buildQuery(CUSTOM_OBJ_NAME, eventFields);
    String whereClause = WHERE + " " + EventFields.DatabaseName.getColumnId() + " = " + databaseName;
    String groupClause = GROUP_BY + " " + eventType.getEventField().getColumnId();
    
    String dbMetricQuery = getDBNameQuery + " " + whereClause +  " " + groupClause;
    RestResponse reponse = sendSyncRequest(getDBNameQuery, apiVersion, client);
    
    JSONObject data;
    try {
      data = reponse.asJSONObject();
    } catch (Exception ex) {
      throw new Exception("Internal error, unable to parse response as JSON", ex);
    }
    return new Records(data);
  }
}
