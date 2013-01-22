package edu.calpoly.codastjegga.sdk;

/**
 * A class that holds tokens information, mainly refresh and access Token. Note
 * that refresh token cannot be changed once initialized.
 * 
 * @author Gagandeep Kohli
 * 
 */
public class Token{
	//Refresh token
	private final String refreshToken;
	//access token
	private String accessToken;	
	
	/**
	 * Constructor for creating Token
	 * @param accessToken accessToken
	 * @param refreshToken refreshToken
	 */
	public Token(String accessToken, String refreshToken)
	{
		this.accessToken = accessToken;  
		this.refreshToken = refreshToken;
	}
	
	/**
	 * getter for refresh token
	 * @return refresh token
	 */
	public String getRefreshToken(){
		return refreshToken;
	}
	
	
	/**
	 * setter for access token
	 * @param accessToken accessToken
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	/**
	 * getter for access token
	 * @return access token
	 */
	public String getAccessToken() {
		return this.accessToken;
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

