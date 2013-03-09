package edu.calpoly.codastjegga.sdk;

import java.net.URI;
import java.net.URISyntaxException;

import android.app.Application;
import edu.calpoly.codastjegga.auth.RestClient;
import edu.calpoly.codastjegga.auth.RestClient.ClientInfo;



public class CodastSDK implements ICodastSDK{
	private RestClientAdapter restClientAdapter;
	private RestClient client;
	private SalesforceConnector connector;

	public CodastSDK(Application app, URI loginInstance, final Token token, String appName, String api, String clientId) throws URISyntaxException
	{
		this.restClientAdapter = new RestClientAdapter(app, new ClientInfo(clientId, loginInstance, new URI("https://login.salesforce.com/"), null, "", "","" , ""), token);
		this.client = this.restClientAdapter.getRestClient();
		
		/*TODO: fill in the connector constructor */
		this.connector = new SalesforceConnector(client,appName, api, app.getApplicationContext());
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
