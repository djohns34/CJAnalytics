package edu.calpoly.codastjegga.cjanalyticsapp;

import java.util.Iterator;
import java.util.Set;

import org.json.JSONException;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;

import edu.calpoly.codastjegga.cjanalyticsapp.event.EventFields;

public class DataFetcher {
  private static final String SELECT = "SELECT";
  private static final String FROM = "FROM";
  private static final String WHERE = "WHERE";
  private static final String GROUP_BY = "GROUP BY";
  private static final String HAVING = "HAVING";
  private static final String ORDER_BY = "ORDER BY";
  private static final String LIMIT = "LIMIT";
  private static final String OFFSET = "OFFSET";
  private static final String EQUALS = "=";
  
  //RestClient - Salesforce RESTApi wrapper
  private RestClient client;
  private String apiVersion;
  
  
  public DataFetcher (RestClient restClient, String apiVersion) throws IllegalArgumentException{
    if (restClient == null || apiVersion == null || apiVersion.length() == 0) {
      throw new IllegalArgumentException("Invalid parameters");
    }
    this.client = restClient;
    this.apiVersion = apiVersion;
  }
  
  public void onRetrieve (String soql, AsyncRequestCallback callback) 
      throws IllegalArgumentException, Exception{
    sendRequest(soql, callback);
  }
  
  public static String buildQuery (String from, Set<EventFields> select) 
      throws IllegalArgumentException
  {
    if (from == null || select == null) {
      throw new IllegalArgumentException("onRetrieve Error: objectType and/or fields is null");
    }
    
    StringBuilder soql = new StringBuilder(SELECT + " ");
    
    Iterator<EventFields> columnIterator = select.iterator();
    while(columnIterator.hasNext()) {
      soql.append(columnIterator.next().getColumnId());
     
      if (columnIterator.hasNext())
         soql.append(", ");
    }
    
    soql.append(" " + FROM + " " + from);
    
    return soql.toString();
  }
  
  private void sendRequest(String soql, AsyncRequestCallback callback) throws Exception, JSONException {
    RestRequest restRequest = RestRequest.getRequestForQuery(apiVersion, soql);    
    client.sendAsync(restRequest, callback); 
  }
}
