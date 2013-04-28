package edu.calpoly.codastjegga.auth;

import java.net.URI;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Application;
import android.util.Log;
import edu.calpoly.codastjegga.auth.OAuth2.TokenEndpointResponse;
import edu.calpoly.codastjegga.sdk.ICodastSDK;



/**
 * 
 * 
 * An adapter for obtaining a rest client for an existing Salesforce
 * account through the use of Token. @see Token for more info.
 * @author Gagandeep Kohli
 * 
 */
@Deprecated
public class RestClientAdapter {
	//Class name
	private final String CLASS_NAME = RestClientAdapter.class.getName();
	//ClientInfo whose account we would like to gain access to
	private final ClientInfo clientInfo;
	//Token for accessing ones Salesforce account
	private final Token token;
	//Salesforce account rest client 
	private RestClient restClient;
	
	/**
	 * Constructor for initializing and obtaining rest client for a particular account
	 * @param app application
	 * @param clientInfo client's information
	 * @param token token
	 */
	public RestClientAdapter(Application app, final ClientInfo clientInfo, final Token token) {
		this.clientInfo = clientInfo;
		this.token = token;
		init(app);
	}
	
	/**
	 * helper method for initializing Http Access and obtaining a rest client
	 * @param app application
	 */
	private void init(Application app){
		HttpAccess.init(app, ICodastSDK.APP_NAME);
		restClient = new RestClient(clientInfo,
				                token.getAccessToken(),
				                HttpAccess.DEFAULT, null);
		
	}
	
	/**
	 * Immutable access to Token
	 * @return Token
	 * @deprecated
	 */
	public Token getToken() {
		//return new Token(token.getAccessToken(), "");
	  return null;
	}
	
	/**
	 * Acessor to ClientInfo
	 * @return clientInfo
	 */
	public ClientInfo getClientInfo() {
		return clientInfo;
	}
	
	/**
	 * restClient accessor 
	 * @return restclient for a particular client
	 */
	public RestClient getRestClient(){
		return restClient;
	}
	
}
