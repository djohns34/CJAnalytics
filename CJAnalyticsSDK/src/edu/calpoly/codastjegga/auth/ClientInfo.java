package edu.calpoly.codastjegga.auth;

import java.net.URI;
import java.util.concurrent.locks.ReentrantLock;

import android.util.Log;
import edu.calpoly.codastjegga.auth.OAuth2.TokenEndpointResponse;
/*
 * All immutable information for an authenticated client 
 * (e.g. username, org ID, etc.).
 */
final public class ClientInfo implements Tokenizable{
	public final String clientId;
	public final URI instanceUrl;
	public final URI loginUrl;
	private final Token mToken;
	private final ReentrantLock tokenAccessLock = new ReentrantLock();

	@Deprecated 
	public ClientInfo(String clientId, URI instanceUrl, URI loginUrl){
		this.clientId = clientId;
		this.instanceUrl = instanceUrl;
		this.loginUrl = loginUrl;
		mToken = null;

	}

	public ClientInfo(String clientId, URI instanceUrl, URI loginUrl, 
			String refreshToken){
		this.clientId = clientId;
		this.instanceUrl = instanceUrl;
		this.loginUrl = loginUrl;
		mToken = new Token("", refreshToken);
 
	}
	
	@Override
	public String getAccessToken() {
		return mToken.getAccessToken();
	}

	@Override
	public String getNewAccessToken() {
		return genreateNewAccessToken();
	}
	
 
	private String genreateNewAccessToken() {
		synchronized (tokenAccessLock) {
			if (tokenAccessLock.isLocked())
			{
				try {
					tokenAccessLock.wait();
				} catch (InterruptedException e) {
					Log.w("TokenClient", 
							"error obtaining a lock on getting new access token", e);
				 
				}
				//if another thread has released the lock then just
				//get the access token
				return mToken.getAccessToken();
			}
			//if other threads does not hold the lock, we lock it
			else {
				tokenAccessLock.lock();
			}
		}
		//at this point this thread holds the lock 
		TokenEndpointResponse tr = null;
		try {
			Log.i("TokenClient", "Obtaining new auth token");
			tr = OAuth2.refreshAuthToken(HttpAccess.DEFAULT,
					loginUrl, clientId,
					mToken.getRefreshToken());
			
			mToken.setAccessToken(tr.authToken);
		} catch (Exception e) {
			Log.e("TokenClient", "Error in obtaining refresh token", e);
			mToken.setAccessToken("");
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
		return mToken.getAccessToken();

	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("  ClientInfo: {\n")
		.append("     loginUrl: ").append(loginUrl.toString()).append("\n")
		.append("     instanceUrl: ").append(instanceUrl.toString()).append("\n")
		.append("  }\n");
		return sb.toString();
	}
}

