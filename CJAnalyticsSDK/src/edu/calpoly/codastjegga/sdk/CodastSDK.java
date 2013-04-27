package edu.calpoly.codastjegga.sdk;

import java.net.URI;
import java.net.URISyntaxException;

import android.app.Application;
import edu.calpoly.codastjegga.auth.ClientInfo;
import edu.calpoly.codastjegga.auth.RestClient;
import edu.calpoly.codastjegga.auth.RestClientAdapter;
import edu.calpoly.codastjegga.auth.Token;




public class CodastSDK implements ICodastSDK{
	
	private RestClientAdapter restClientAdapter;
	private ClientInfo client;
	private SalesforceConnector connector;

	/**
	 * @deprecated
	 * Intialize's Codast SDK, setups the database, and obtains a HTTP connection object for send data
	 * @param app - application context {@link Application}
	 * @param loginInstance  server the developer account is logged in at (obtained from Salesforce..ex. na9.Salesforce.com)
	 * @param token - token {@link Token}
	 * @param appName - app name
	 * @param api - api version
	 * @param clientId - developers client id 
	 * @throws URISyntaxException - if loginInstance is invalid
	 */
	public CodastSDK(Application app, String loginInstance, final Token token, 
	    String appName, String api, String clientId) throws URISyntaxException
	{
		//Create new URI for login instance
		URI loginInstanceURI = new URI(loginInstance);
		//Create new URI for salesforce login (used for refreshing tokens)
		URI salesforceLoginURI = new URI("https://login.salesforce.com");
		//create a new rest client adapater
		this.restClientAdapter = new RestClientAdapter(app, new ClientInfo(clientId, 
		                             loginInstanceURI, salesforceLoginURI), token);
		
		//Create new salesforce connector - used for sending Events to Salesforce 
		//account
		this.connector = new SalesforceConnector(this.restClientAdapter.getRestClient() 
		     ,appName, api, app.getApplicationContext());
	}
	
	/**
	 * Intialize's Codast SDK, setups the database, and obtains a HTTP 
	 * connection object for send data
	 * @param app - application context {@link Application}
	 * @param loginInstance  server the developer account is logged in at 
	 * (obtained from Salesforce..ex. na9.Salesforce.com)
	 * @param token - token {@link Token}
	 * @param appName - app name
	 * @param api - api version
	 * @param clientId - developers client id 
	 * @throws URISyntaxException - if loginInstance is invalid
	 */
  public CodastSDK(Application app, String loginInstance, String refreshToken, 
      String appName, String clientId) throws URISyntaxException
  {
    //Create new URI for login instance
    URI loginInstanceURI = new URI(loginInstance);
    //Create new URI for salesforce login (used for refreshing tokens)
    URI salesforceLoginURI = new URI("https://login.salesforce.com");
    //create a new rest client adapater
    this.client = new ClientInfo(app, appName, clientId, loginInstanceURI, salesforceLoginURI,
                                 refreshToken);
    
    //Create new salesforce connector - used for sending Events to Salesforce account
    this.connector = new SalesforceConnector(this.restClientAdapter.getRestClient(), 
                                    appName, app.getApplicationContext());
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
