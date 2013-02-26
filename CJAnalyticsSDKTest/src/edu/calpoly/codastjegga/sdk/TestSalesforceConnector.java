package edu.calpoly.codastjegga.sdk;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import junit.framework.TestCase;

import android.test.ActivityTestCase;
import android.test.AndroidTestCase;

import com.salesforce.androidsdk.rest.RestClient.ClientInfo;

public class TestSalesforceConnector extends AndroidTestCase {


    
    private SalesforceConnector connector;
    

    
    protected void setUp() throws Exception {
        TestRestClientAdapter trca=new TestRestClientAdapter();
        trca.setUp();
        connector =new SalesforceConnector(trca.adapt.getRestClient(),"Angry Birds", TestRestClientAdapter.api, getContext());
    };
    
    
    @SuppressWarnings("unused")
    public void testSendDummyData() throws URISyntaxException{
        connector.db.clear();
        
        if(false){
            Random r=new Random();
            for(int i=0;i<30;i++){
                double doub=20*r.nextDouble();
                float floa=20*r.nextFloat();
                int in=r.nextInt(25);

                connector.addEvent(EventFactory.createEvent(EventType.Currency, "Wage",doub));
                connector.addEvent(EventFactory.createEvent(EventType.Float, "Age",floa));
                connector.addEvent(EventFactory.createEvent(EventType.Integer, "Level",in));

            }
            for(int i=0;i<5;i++){
                connector.addEvent(EventFactory.createEvent(EventType.Locale, "Location","US"));
                connector.addEvent(EventFactory.createEvent(EventType.Locale, "Location","UK"));
                connector.addEvent(EventFactory.createEvent(EventType.Locale, "Location","TX"));
                connector.addEvent(EventFactory.createEvent(EventType.Text, "Name","Daniel"));
                connector.addEvent(EventFactory.createEvent(EventType.Text, "Name","Corey"));
                connector.addEvent(EventFactory.createEvent(EventType.Text, "Name","Jeremy"));
                connector.addEvent(EventFactory.createEvent(EventType.Text, "Name","Steven"));
                connector.addEvent(EventFactory.createEvent(EventType.Text, "Name","Jeremy"));  
                connector.addEvent(EventFactory.createEvent(EventType.Text, "Name","Gagandeep"));
            }

            connector.sendEvents();
        }
        
    }
    public void sendTestData() {

        connector.sendEvents();

    }
}
