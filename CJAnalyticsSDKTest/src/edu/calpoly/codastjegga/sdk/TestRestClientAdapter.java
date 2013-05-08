package edu.calpoly.codastjegga.sdk;

import java.net.URI;

import junit.framework.TestCase;
import edu.calpoly.codastjegga.auth.ClientInfo;

public class TestRestClientAdapter extends TestCase{

    
    static String refreshToken="5Aep861rEpScxnNE64IogmmXsb72ONfi9xneXYd7423HJqVESjYEdBh18zHICrBLfyYEg2Yrw9jAg==";
    static String clientId="3MVG9y6x0357HlefAMuoGiOstBQg3r5rHpEodDBNQ6GIjd5Fh.RPRX4YA6Ax_sg69Jq8tpuFU76NlE0z5LTbc";
    static String accountName="codast@jegga.com (SDK Test)";
    static String username="codast@jegga.com";
    static String userId="005E0000001nMXgIAM";
    static String orgId="00DE0000000dgAFMAY";
    


    static String api="v23.0";
    

    String instanceServer="https://na9.salesforce.com";
    String loginServer="https://login.salesforce.com";
    String idUrl="http://login.salesforce.com/id/00DE0000000dgAFMAY/005E0000001nMXgIAM";
    
    ClientInfo info;
    @Override
    protected void setUp() {
    	try {
        info = new ClientInfo(clientId, new URI(instanceServer),
                new URI(loginServer), refreshToken);
    	}catch (Exception ex) {
    		fail("initializing client info should not fail");
    	}

    }
    
    public void testGetRestClient() {
        assertNotNull(info);
    }

}
