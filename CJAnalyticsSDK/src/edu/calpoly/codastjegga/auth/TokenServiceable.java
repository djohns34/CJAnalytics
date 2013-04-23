package edu.calpoly.codastjegga.auth;



/** 
 * Moved from Salesforce Android SDK RestClient class
 * 
 * AuthTokenProvider interface.
 * RestClient will call its authTokenProvider to refresh its authToken once it has expired. 
 */
public interface TokenServiceable {
	String getAccessToken();
	String getNewAccessToken();
}

