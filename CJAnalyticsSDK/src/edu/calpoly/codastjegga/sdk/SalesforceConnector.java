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


public class SalesforceConnector{

	RestClient client;
	String apiVersion;

	SalesforceDBAdapter db;

	static final String table ="TrackedEvents__c";
	
	
	
	
	public SalesforceConnector(RestClient client, String apiVersion,Context activity) {
		this.client = client;
		this.apiVersion = apiVersion;
		
		db=new SalesforceDBAdapter(activity);
		db.open();
		
	}
	public void addEvent(Event<?> e){
        
	    db.insertEvent(e);
	    
	}

	public void sendTestData() {

		addEvent(EventFactory.createEvent(EventType.Currency, "Wage",32.45));
		addEvent(EventFactory.createEvent(EventType.Float, "Age",21.7f));
		addEvent(EventFactory.createEvent(EventType.Integer, "Level",4));
		addEvent(EventFactory.createEvent(EventType.Locale, "Location","US"));
		addEvent(EventFactory.createEvent(EventType.Text, "Name","Daniel Johnson!"));
		sendEvents();

	}
	
	public void sendEvents(){
		Map<Integer, Map> eventMap =db.fetchAllEvents();
		
		for(Entry<Integer, Map> e:eventMap.entrySet()){
		    sendRequestForInsert(e.getKey(),e.getValue());
		}
	}
		
		


	private void sendRequestForInsert(Integer i,Map map){

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
                
                client.sendAsync(r, callback);
            }
        }

	private class CodastRequestCallback implements AsyncRequestCallback{
	    private int dbKey;
	    public CodastRequestCallback(int androidDBkey) {
	        dbKey=androidDBkey;
	    }


	    @Override
	    public void onSuccess(RestRequest request, RestResponse result) {
	        if(result.isSuccess()){
	            try {
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