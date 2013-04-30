package edu.calpoly.codastjegga.sdk;

import java.net.URI;
import java.net.URISyntaxException;

import android.app.Application;
import android.util.Log;
import edu.calpoly.codastjegga.auth.ClientInfo;
import edu.calpoly.codastjegga.auth.HttpAccess;
import edu.calpoly.codastjegga.auth.Token;




public class CodastSDK implements ICodastSDK{
	
	private static final String CODAST_SDK = "CodastSDK";
	private static final String USER_AGENT = "android";
	private static final String SF_LOGIN = "https://login.salesforce.com";

	private ClientInfo mClientInfo;
	private SalesforceConnector connector;
	
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
  public CodastSDK(Application app, String appName, String loginInstance, 
      String refreshToken, String clientId) throws URISyntaxException
  {
    //Create new URI for login instance
    URI loginInstanceURI = new URI(loginInstance);
    //Create new URI for salesforce login (used for refreshing tokens)
  
    URI salesforceLoginURI = new URI(SF_LOGIN);
    //SET httpAccess so it can get network state
    HttpAccess.init(app, USER_AGENT);
    Log.i(CODAST_SDK, "start: Obtaining ClientInfo");
    //create a new rest client adapater
    this.mClientInfo = new ClientInfo(clientId, loginInstanceURI, salesforceLoginURI,
                                 refreshToken);
    Log.i(CODAST_SDK, "end: Obtaining ClientInfo");
    Log.i(CODAST_SDK, "start: Obtaining SalesforceConnector");
    //Create new salesforce connector - used for sending Events to Salesforce account
    this.connector = new SalesforceConnector(mClientInfo, appName, 
                                             app.getApplicationContext());
    Log.i(CODAST_SDK, "end: Obtaining SalesforceConnector");
  }
	
  public void trackData(EventType type, String metric, Object data)
  {
    Event e = EventFactory.createEvent(type, metric, data);
    Log.i(CODAST_SDK, "Tracked Event");
    connector.addEvent(e);
  }

  public void sendData()
  {
    Log.i(CODAST_SDK, "checking internet access");
    if(HttpAccess.DEFAULT.hasNetwork()) {
      Log.i(CODAST_SDK, "Start: sending data");
      connector.sendEvents();
      Log.i(CODAST_SDK, "end: sending data");
    }else {
      Log.i(CODAST_SDK, "internet access not available");
    }
  }
}
