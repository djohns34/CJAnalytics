package edu.calpoly.codastjegga.auth;

import junit.framework.TestCase;



/**
 * Test cases for Token
 */

public class TokenTester extends TestCase {
	Token token;
	final String accessToken = "Access-Token";
	final String newAccessToken = "New Access Token";
	
	protected void setUp() throws Exception {
		super.setUp();
		token = new Token(accessToken, "", null) {
			@Override
			public String getNewAccessToken() {
				return newAccessToken;
			}
		};
	}
	
	public void testGetter(){
	   assertEquals(accessToken, token.getAccessToken());
	   assertEquals(newAccessToken, token.getNewAccessToken());
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
