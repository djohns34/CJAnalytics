package edu.calpoly.codastjegga.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.telephony.TelephonyManager;
import android.util.Log;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;


public class SalesforceConnector{

	RestClient client;
	String apiVersion;

	

	static final String table ="TrackedEvents__c";
	
	private static final String eventName="EventName__c";
	private static final String timeStamp="Timestamp__c";
	private static final String deviceId ="Device_Id__c";
	private static final String valueType="ValueType__c";
	
	
	private List<Event<?>> events; 
	
//	private static TelephonyManager manager;
	
	public SalesforceConnector(RestClient client, String apiVersion) {
		events =new ArrayList<Event<?>>();
		this.client = client;
		this.apiVersion = apiVersion;
		
	}
	public void addEvent(Event<?> event){
		events.add(event);
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
		HashMap<String, List<Event<?>>> eventMap =new HashMap<String, List<Event<?>>>();
		
		for(Event<?> e:events){
				try {

					HashMap<String, Object> map=new HashMap<String, Object>();
					map.put(eventName, e.getKey());
					map.put(timeStamp, e.getTimeStamp());
//					map.put(deviceId, manager.getDeviceId());
					map.put(deviceId, "Need Actual ID");

					map.put(e.getEventType().getField(), e.getRESTValue());
					map.put(valueType, e.getEventType().getFieldType());
					
					
					RestRequest r=RestRequest.getRequestForCreate(apiVersion, table, map);
					sendRequest(r);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	}
		
		


	private void sendRequest(RestRequest restRequest)
			throws UnsupportedEncodingException {

		client.sendAsync(restRequest, new AsyncRequestCallback() {
			@Override
			public void onSuccess(RestRequest request, RestResponse result) {
				Log.i("onSuccess", result.toString());

			}

			@Override
			public void onError(Exception exception) {

				Log.i("onError", exception.toString());
			}
		});
	}
}