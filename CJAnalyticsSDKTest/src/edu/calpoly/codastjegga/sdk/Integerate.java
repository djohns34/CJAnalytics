package edu.calpoly.codastjegga.sdk;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import android.test.AndroidTestCase;

import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestClient.ClientInfo;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

public class Integerate extends AndroidTestCase{
    SalesforceConnector connector;

    @Override
    protected void setUp() throws Exception {
        /*Using the other class to avoid dup code*/
        TestSalesforceConnector testConn=new TestSalesforceConnector();
        testConn.setUp();
        
        testConn.setContext(this.getContext());
        connector=testConn.getConnector();
    }
   
    public void testLogin() throws URISyntaxException{
        
        RestRequest r=RestRequest.getRequestForDescribeGlobal(TestSalesforceConnector.api);
        try{
            RestResponse response=connector.getClient().sendSync(r);
            assertEquals("Test OAuth flow"+response.asString(),200,response.getStatusCode());
        }catch(IOException e){
            fail("testLogin error");
        }

    }


}
