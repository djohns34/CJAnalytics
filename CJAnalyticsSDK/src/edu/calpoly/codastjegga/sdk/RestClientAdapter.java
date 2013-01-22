package edu.calpoly.codastjegga.sdk;

import android.app.Application;
import android.util.Log;

import com.salesforce.androidsdk.auth.HttpAccess;
import com.salesforce.androidsdk.auth.OAuth2;
import com.salesforce.androidsdk.auth.OAuth2.TokenEndpointResponse;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AuthTokenProvider;
import com.salesforce.androidsdk.rest.RestClient.ClientInfo;

/*
 * An adapter for obtaining a rest client for an existing Salesforce
 * account through the use of Token. @see Token for more info.
 * @author Gagandeep Kohli
 */
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
		HttpAccess.init(app, CodastSDK.APP_NAME);
		restClient = new RestClient(clientInfo,
				                token.getAccessToken(),
				                HttpAccess.DEFAULT, new AuthTokenRefresher());
		
	}
	
	/**
	 * restClient accessor 
	 * @return restclient for a particular client
	 */
	public RestClient getRestClient(){
		return restClient;
	}
	
	/**
	 * A class for handing flow of refreshing auth. token.
	 * 
	 * @author Gagandeep Kohli
	 * 
	 */
	private class AuthTokenRefresher implements AuthTokenProvider {
		// last time the token was refreshed
		private long lastRefresh;

		public AuthTokenRefresher() {
			lastRefresh = 0;
		}

		@Override
		public String getNewAuthToken() {
			TokenEndpointResponse tr = null;
			try {
				tr = OAuth2.refreshAuthToken(HttpAccess.DEFAULT,
						clientInfo.loginUrl, clientInfo.clientId,
						token.getRefreshToken());

				lastRefresh = System.currentTimeMillis() - lastRefresh;
				token.setAccessToken(tr.authToken);
			} catch (Exception e) {
				Log.e(CLASS_NAME, "Error in obtaining refresh token", e);

			}
			return token.getAccessToken();

		}

		@Override
		public String getRefreshToken() {
			return token.getRefreshToken();
		}

		@Override
		public long getLastRefreshTime() {
			return lastRefresh;
		}
	}
}
