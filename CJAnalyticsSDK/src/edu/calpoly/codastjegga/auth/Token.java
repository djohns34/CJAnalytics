package edu.calpoly.codastjegga.auth;

import java.util.concurrent.locks.ReentrantLock;

import android.util.Log;
import edu.calpoly.codastjegga.auth.OAuth2.TokenEndpointResponse;

/**
 * A class that holds tokens information, mainly refresh and access Token. Note
 * that refresh token cannot be changed once initialized.
 * 
 * @author Gagandeep Kohli
 * 
 */
public class Token implements Tokenizable{
	//Refresh token
	private final String refreshToken;
	//access token
	private String accessToken;	
	//Client info
	ClientInfo info;
	private final ReentrantLock tokenAccessLock = new ReentrantLock();
	
	/**
	 * Constructor for creating Token
	 * @param accessToken accessToken
	 * @param refreshToken refreshToken
	 */
	public Token(String accessToken, String refreshToken, ClientInfo info)
	{
		this.accessToken = accessToken;  
		this.refreshToken = refreshToken;
		this.info = info;
	}
	
	/**
	 * getter for access token
	 * @return access token
	 */
	public String getAccessToken() {
		return this.accessToken;
	}
	

	
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
	        return accessToken;
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
	          info.getLoginUrl(), info.getClientId(), refreshToken);
	      
	      accessToken = tr.authToken;
	    } catch (Exception e) {
	      Log.e("TokenClient", "Error in obtaining refresh token", e);
	      accessToken = "";
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
	    return accessToken;

	  }
	  
	/**
	 * Represents information about this token information
	 * @return token information
	 */
	public String toString(){
		StringBuilder token= new StringBuilder();
		token.append("{Token:");
		token.append("    {Access Token:" + accessToken + 
				          //",Refresh Token: " + refreshToken + 
				          "}");
		token.append("}");
		
		return toString().toString();
	}

}

