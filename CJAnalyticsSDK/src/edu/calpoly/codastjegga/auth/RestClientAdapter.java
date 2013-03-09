package edu.calpoly.codastjegga.auth;

import java.net.URI;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Application;
import android.util.Log;
import edu.calpoly.codastjegga.auth.OAuth2.TokenEndpointResponse;
import edu.calpoly.codastjegga.auth.RestClient.AuthTokenProvider;
import edu.calpoly.codastjegga.auth.RestClient.ClientInfo;
import edu.calpoly.codastjegga.sdk.ICodastSDK;



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
		HttpAccess.init(app, ICodastSDK.APP_NAME);
		restClient = new RestClient(clientInfo,
				                token.getAccessToken(),
				                HttpAccess.DEFAULT, new AuthTokenRefresher());
		
	}
	
	/**
	 * Immutable access to Token
	 * @return Token
	 */
	public Token getToken() {
		return new Token(token.getAccessToken(), token.getRefreshToken());
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
	
	/**
	 * A class for handing flow of refreshing auth. token.
	 * 
	 * @author Gagandeep Kohli
	 * 
	 */
	private class AuthTokenRefresher implements AuthTokenProvider {
		private final ReentrantLock tokenAccessLock = new ReentrantLock();
		// last time the token was refreshed
		private long lastRefresh;

		public AuthTokenRefresher() {
			lastRefresh = 0;
		}

		@Override
		public String getNewAuthToken() {
			synchronized (tokenAccessLock) {
				if (tokenAccessLock.isLocked())
				{
					try {
						tokenAccessLock.wait();
					} catch (InterruptedException e) {
//						Log.w("RestClientAdapter", "error obtaining a lock on getting new access token", e);
//						e.printStackTrace();
					}
					//if another thread has released the lock then just
					//get the access token
					return token.getAccessToken();
				}
				//if other threads do not hold the lock, we lock it
				else {
					tokenAccessLock.lock();
				}
			}
			//at this point this thread holds the lock 
			TokenEndpointResponse tr = null;
			try {
				URI loginUrl = clientInfo.loginUrl;
				String clientId = clientInfo.clientId;
				tr = OAuth2.refreshAuthToken(HttpAccess.DEFAULT,
						loginUrl, clientId,
						token.getRefreshToken());

				lastRefresh = System.currentTimeMillis() - lastRefresh;
				token.setAccessToken(tr.authToken);
			} catch (Exception e) {
				Log.e(CLASS_NAME, "Error in obtaining refresh token", e);

			}
			finally {
				//before this method returns, tokenAccessLock needs to unlock itself and
				//notify any waiting threads
				synchronized (tokenAccessLock) {
					tokenAccessLock.unlock();
					tokenAccessLock.notifyAll();
				}
			}
			//return the access token
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
