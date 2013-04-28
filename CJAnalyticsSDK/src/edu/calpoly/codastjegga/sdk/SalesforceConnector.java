package edu.calpoly.codastjegga.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.ParseException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sforce.async.AsyncApiException;

import edu.calpoly.codastjegga.auth.ClientInfo;
import edu.calpoly.codastjegga.auth.RestClient;
import edu.calpoly.codastjegga.auth.RestClient.AsyncRequestCallback;
import edu.calpoly.codastjegga.auth.RestRequest;
import edu.calpoly.codastjegga.auth.RestResponse;
import edu.calpoly.codastjegga.bulk.DBCsvInputStream;
import edu.calpoly.codastjegga.bulk.DataLoader;



/**
 * Defines operations required to send data to salesforce, handles storage
 * before sending via persistent storage
 * 
 * @author Daniel
 * @author Gagandeep adding BulkLoader
 * */
public class SalesforceConnector{

  private RestClient client;
  private String apiVersion;
  private ClientInfo clientInfo;

  SalesforceDBAdapter db;

  //Our salesforce object
  private static final String table ="codastjegga__TrackedEvents__c";

  //    private static final String  DatabaseName ="codastjegga__DatabaseName__c";

  private final String  appName;

  /**
   * @deprecated
   * @param client
   * @param appName
   * @param apiVersion
   * @param activity
   */
  public SalesforceConnector(RestClient client,String appName, String apiVersion,Context activity) {
    this.client = client;
    this.apiVersion = apiVersion;
    this.appName=appName;


    db=new SalesforceDBAdapter(activity);
    db.open();

  }

  /**
   * @deprecated
   * @param client
   * @param appName
   * @param activity
   */
  public SalesforceConnector(RestClient client,String appName,Context activity) {
    this.client = client;
    this.appName=appName;


    db=new SalesforceDBAdapter(activity);
    db.open();

  }

  public SalesforceConnector(ClientInfo clientInfo, String appName,Context activity) {
    this.clientInfo = clientInfo;
    this.appName=appName;

    db=new SalesforceDBAdapter(activity);
    db.open();

  }


  /**
   * Adds an event to the queue of events that will be sent to salesforce. 
   * @param e
   */
  public void addEvent(Event<?> e){

    db.insertEvent(e,appName);

  }

  /**
   * Retrieves all of the events from storage and sends them to salesforce.
   */
  public void sendEvents(){
    DBCsvInputStream stream = db.getAllEventsAsCSVInputStream();
    new BulkSend(db).execute(stream);
    
  }

  private class BulkSend extends AsyncTask<DBCsvInputStream, Void, Void> {
    SalesforceDBAdapter mDb;
    
    public BulkSend (SalesforceDBAdapter db) {
      this.mDb = db;
    }
    
    @Override
    protected Void doInBackground(DBCsvInputStream... args) {
      DBCsvInputStream inputStream = args[0];
      try {
        // Set the job
        DataLoader loader = new DataLoader(clientInfo, table);
        //send the stream
        loader.sendCSV(inputStream);
        //close the job
        loader.closeUploadJob();
        Log.e(this.getClass().toString(), "Sending Data");
        
        List<Integer> idsOfRowsLoaded = inputStream.getRowsIds();  
        Log.e(this.getClass().toString(), "Removing events in local db");
        //FOR each row send get its rowIds
        for (Integer rowId : idsOfRowsLoaded) {
          //delete the event in the local db
          mDb.deleteEvent(rowId);
          Log.e(this.getClass().toString(), "Removed { row id:" + rowId + " }");
        }
      } catch (AsyncApiException e) {
        Log.e(this.getClass().toString(), "Failed to send data", e);
      } catch (UnsupportedEncodingException e) {
        Log.e(this.getClass().toString(), "Malformatted InputStream", e);
      }
      return null;
    }
  }

  /**
   * Creates and executes the appropriate query to push data to salesforce
   * 
   * @param i the row id in the local database
   * @param map
   */
  private void sendRequestForInsert(Integer i,Map<String,Object> map){

    RestRequest r=null;
    try {
      r = RestRequest.getRequestForCreate(apiVersion, table, map);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if(r !=null){
      AsyncRequestCallback callback =new CodastRequestCallback(i);


      //                try {
      //                    callback.onSuccess(r, getClient().sendSync(r));
      //                } catch (IOException e) {
      //                    // TODO Auto-generated catch block
      //                   callback.onError(e);
      //                }

      client.sendAsync(r, callback);
    }
  }


  /*protected getter for unitTesting*/
  protected RestClient getClient() {
    return client;
  }

  /**
   * Defines a Callback class that handles the result of sending data to salesforce
   * 
   * @author Daniel
   *
   */
  private class CodastRequestCallback implements AsyncRequestCallback{
    private int dbKey;
    public CodastRequestCallback(int androidDBkey) {
      dbKey=androidDBkey;
    }


    @Override
    /**
     * Upon a successful send removes the event from the database
     */
    public void onSuccess(RestRequest request, RestResponse result) {
      if(result.isSuccess()){
        try {
          //Make sure it was an actual success /*May replace with statusCode =___
          if(result.asJSONObject().has("success") && result.asJSONObject().has("errors")){
            boolean success= result.asJSONObject().getBoolean("success");
            boolean hasErrors= result.asJSONObject().getJSONArray("errors").length() != 0;

            if(success && !hasErrors){
              db.deleteEvent(dbKey);
            }
          }
        } catch (ParseException e) {
          e.printStackTrace();
        } catch (JSONException e) {

          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      Log.i("onSuccess", result.toString());

    }

    @Override
    public void onError(Exception exception) {
      Log.i("onError", exception.toString());
    }
  }
}
