package edu.calpoly.codastjegga.sdk.test;

import junit.framework.TestCase;

import junit.framework.TestCase;

import edu.calpoly.codastjegga.sdk.Token;

/**
 * Test cases for Token
 */

public class TokenTester extends TestCase {
	Token token;
	final String accessToken = "Access-Token";
	final String refreshToken = "Refresh-Token";
	
	protected void setUp() throws Exception {
		super.setUp();
		token = new Token(accessToken, refreshToken);
	}
	
	public void testGetter(){
	   assertEquals(accessToken, token.getAccessToken());
	   assertEquals(refreshToken, token.getRefreshToken());
	}
	
	public void testSetter(){
	   String newAccessToken = "New-Access-Token";
	   token.setAccessToken(newAccessToken);
	   
	    assertEquals(newAccessToken, token.getAccessToken());
	}
	
	public void testToString(){
		StringBuilder token= new StringBuilder();
		token.append("{Token:");
		token.append("    {Access Token:" + accessToken + 
				          //",Refresh Token: " + refreshToken + 
				          "}");
		token.append("}");
		
	    assertEquals(token.toString(), token.toString());
	}
}
