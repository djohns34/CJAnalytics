package edu.calpoly.codastjegga.cjanalyticsapp.datafetcher;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Set;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;

import edu.calpoly.codastjegga.cjanalyticsapp.event.EventFields;

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
  
  /**
   * Performs a soql query request asynchronous . Caller should use the callback to handle the returned data.
   * @param soql soql query
   * @param callback @see {@link AsyncRequestCallback}
   * @throws UnsupportedEncodingException @see {@link RestRequest#getRequestForQuery(String, String)}
   */
  public void onSoqlQuery (String soql, AsyncRequestCallback callback) 
      throws  UnsupportedEncodingException{
    sendRequest(soql, callback);
  }
  
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
  
  /**
   * Carries out the soql call
   * @param soql soql query
   * @param callback @see {@link AsyncRequestCallback}
   * @throws UnsupportedEncodingException if soql query is invalid
   */
  private void sendRequest(String soql, AsyncRequestCallback callback) throws UnsupportedEncodingException {
    RestRequest restRequest = RestRequest.getRequestForQuery(apiVersion, soql);    
    client.sendAsync(restRequest, callback); 
  }
}
