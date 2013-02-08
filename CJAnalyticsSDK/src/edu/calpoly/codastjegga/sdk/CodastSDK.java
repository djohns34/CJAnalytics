package edu.calpoly.codastjegga.sdk;

import android.app.Application;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.ClientInfo;


public class CodastSDK implements ICodastSDK{
	private RestClientAdapter restClientAdapter;
	private RestClient client;
	private SalesforceConnector connector;

	public CodastSDK(Application app, ClientInfo clientInfo, final Token token)
	{
		this.restClientAdapter = new RestClientAdapter(app, clientInfo, token);
		this.client = this.restClientAdapter.getRestClient();
		
		/*TODO: fill in the connector constructor */
		this.connector = new SalesforceConnector(client, null, null);
	}
	
	public void trackData(EventType type, String metric, Object data)
	{
		Event e = EventFactory.createEvent(type, metric, data);
		connector.addEvent(e);
	}
	
	public void sendData()
	{
		connector.sendEvents();
	}
}
