package edu.calpoly.codastjegga.sdk;

import java.io.IOException;
import java.net.URISyntaxException;

import android.test.AndroidTestCase;
import edu.calpoly.codastjegga.auth.RestRequest;
import edu.calpoly.codastjegga.auth.RestResponse;



public class Integerate extends AndroidTestCase{
    SalesforceConnector connector;

    @Override
    protected void setUp() throws Exception {
        TestRestClientAdapter trca=new TestRestClientAdapter();
        trca.setUp();
        connector =new SalesforceConnector(trca.adapt.getRestClient(),"Angry Birds", TestRestClientAdapter.api, getContext());
    }
   
    public void testLogin() throws URISyntaxException{
        
//        RestRequest r=RestRequest.getRequestForDescribeGlobal(TestRestClientAdapter.api);
//        try{
//            RestResponse response=connector.getClient().sendSync(r);
//            assertEquals("Test OAuth flow"+response.asString(),200,response.getStatusCode());
//        }catch(IOException e){
//            fail(e+"testLogin error");
//        }

    }


}
