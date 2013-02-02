package edu.calpoly.codastjegga.sdk;

import static org.junit.Assert.*;

import java.net.URI;

import junit.framework.TestCase;

import org.junit.Test;

import com.salesforce.androidsdk.rest.RestClient.ClientInfo;

public class TestRestClientAdapter extends TestCase{

    static String authToken="00DE0000000dgAF!AQwAQJAhzzeg2dU2cXXqI2g_OdgekNC81rC79Dnd7ba7S1ySz4LZaHgt1kCfyzAKcwXHJPKPSJ_WsaHMQ4h1vX.JUxoP5TFW";
    static String refreshToken="5Aep861rEpScxnNE64IogmmXsb72ONfi9xneXYd7423HJqVESjYEdBh18zHICrBLfyYEg2Yrw9jAg==";
    static String clientId="3MVG9y6x0357HlefAMuoGiOstBXbpy4LNe2zAL8eeWFHc_JizJACxdM7pz92vvDH2mtLJKk4z7XKSX0vndYl9";
    static String accountName="codast@jegga.com (SDK Test)";
    static String username="codast@jegga.com";
    static String userId="005E0000001nMXgIAM";
    static String orgId="00DE0000000dgAFMAY";
    


    static String api="v23.0";
    

    String instanceServer="https://na9.salesforce.com";
    String loginServer="https://login.salesforce.com";
    String idUrl="http://login.salesforce.com/id/00DE0000000dgAFMAY/005E0000001nMXgIAM";
    
    
    Token t;
    ClientInfo info;
    RestClientAdapter adapt;
    @Override
    protected void setUp() throws Exception {

        info = new ClientInfo(clientId, new URI(instanceServer),
                new URI(loginServer), new URI(idUrl), accountName, username,
                userId, orgId);

        t = new Token(authToken, refreshToken);
        adapt = new RestClientAdapter(null, info, t);
    }

    @Test
    public void testGetToken() {
        
        assertEquals(authToken, adapt.getToken().getAccessToken());
        assertEquals(refreshToken, adapt.getToken().getRefreshToken());
    }

    @Test
    public void testGetClientInfo() {
        assertEquals(info, adapt.getClientInfo());
    }

    @Test
    public void testGetRestClient() {
        assertNotNull(adapt.getRestClient());
    }

}
