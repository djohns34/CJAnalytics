package edu.calpoly.codastjegga.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.ParseException;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

/**
 * Defines operations required to send data to salesforce, handles storage
 * before sending via persistent storage
 * 
 * @author Daniel
 * */
public class SalesforceConnector{

    private RestClient client;
    private String apiVersion;

    SalesforceDBAdapter db;

    //Our salesforce object
    private static final String table ="TrackedEvents__c";
    
    
    
    
    public SalesforceConnector(RestClient client, String apiVersion,Context activity) {
        this.client = client;
        this.apiVersion = apiVersion;
        
        db=new SalesforceDBAdapter(activity);
        db.open();
        
    }
    
    
    /**
     * Adds an event to the queue of events that will be sent to salesforce. 
     * @param e
     */
    public void addEvent(Event<?> e){
        
        db.insertEvent(e);
        
    }

    /**
     * Retrieves all of the events from storage and sends them to salesforce.
     */
    public void sendEvents(){
        Map<Integer, Map<String, Object>> eventMap =db.fetchAllEvents();
        
        for(Entry<Integer, Map<String, Object>> e:eventMap.entrySet()){
            sendRequestForInsert(e.getKey(),e.getValue());
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
                
                
                try {
                    callback.onSuccess(r, getClient().sendSync(r));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                   callback.onError(e);
                }
                
//                client.sendAsync(r, callback);
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