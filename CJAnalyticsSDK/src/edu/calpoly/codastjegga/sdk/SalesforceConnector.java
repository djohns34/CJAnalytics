package edu.calpoly.codastjegga.sdk;

import java.io.UnsupportedEncodingException;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sforce.async.AsyncApiException;

import edu.calpoly.codastjegga.auth.ClientInfo;
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
  private String apiVersion;
  private ClientInfo clientInfo;

  SalesforceDBAdapter db;

  //Our salesforce object
  private static final String table ="codastjegga__TrackedEvents__c";
  private final String  appName;


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
}
