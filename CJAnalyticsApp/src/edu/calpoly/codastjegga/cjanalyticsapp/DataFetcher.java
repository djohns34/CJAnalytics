package edu.calpoly.codastjegga.cjanalyticsapp;

import java.io.UnsupportedEncodingException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;

import android.util.Log;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

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
  
  
  public DataFetcher (RestClient restClient, String apiVersion) {
    this.client = restClient;
    this.apiVersion = apiVersion;
  }
  
  public void onRetrieve (String from, Set<EventFields> select) 
      throws IllegalArgumentException, Exception{
    
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
    sendRequest(soql.toString());
  }
  
  private void sendRequest(String soql) throws Exception {
    RestRequest restRequest = RestRequest.getRequestForQuery(apiVersion, soql);
    DataFetcherResult result = new DataFetcherResult();
    
    client.sendAsync(restRequest, result);
    
    if (result.isSuccessfull()) {
      //parsedata
    }
    else {
      throw result.exception;
    }
  }
  
  
  private final class DataFetcherResult implements AsyncRequestCallback {
    RestResponse restResponse;
    boolean successful;
    Exception exception;

    public RestResponse getRestResponse () {
      return this.restResponse;
    }
    
    public boolean isSuccessfull () {
      return this.successful;
    }
    
    public Exception getException () {
      return this.exception;
    }
    
    @Override
    public void onSuccess(RestRequest request, RestResponse result) {
      this.successful = true;
      this.restResponse = result;
     }
     
     @Override
     public void onError(Exception exception) {
       Log.e("DataFetcher", "Unable to make soql call", exception);
       this.successful = false;
       this.exception = exception;
     }
  }
}
