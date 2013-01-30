package edu.calpoly.codastjegga.sdk;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import org.junit.Test;

import android.test.AndroidTestCase;

import com.salesforce.androidsdk.rest.RestClient.ClientInfo;

public class TestSalesforceConnector extends AndroidTestCase {

    static String authToken="00DE0000000dgAF!AQwAQLXAi6dXEEidTK583Bd3pMG7_C.jz8z.6fK_VgTjNoxCG6sI5FDNR4nXdWRcpcwCIti.kUhYgvkpO6DvwTTIrd8p.1w8";
    static String refreshToken="5Aep861rEpScxnNE64IogmmXsb72ONfi9xneXYd7423HJqVEShp3f7NXwSlRc5zNxdKU5AncxAo5A==";
    static String clientId="3MVG9y6x0357HlefAMuoGiOstBXbpy4LNe2zAL8eeWFHc_JizJACxdM7pz92vvDH2mtLJKk4z7XKSX0vndYl9";
    static String accountName="codast@jegga.com (SDK Test)";
    static String username="codast@jegga.com";
    static String userId="005E0000001nMXgIAM";
    static String orgId="00DE0000000dgAFMAY";

    static String api="v23.0";
    
    SalesforceConnector connector;
    
    @Test
    public SalesforceConnector getConnector() throws URISyntaxException{
        URI instanceServer=new URI("https://na9.salesforce.com");
        URI loginServer=new URI("https://login.salesforce.com");;
        URI idUrl=new URI("http://login.salesforce.com/id/00DE0000000dgAFMAY/005E0000001nMXgIAM");


        ClientInfo info=new ClientInfo(clientId, instanceServer, loginServer, idUrl, accountName, username, userId, orgId);
        Token t=new Token(authToken, refreshToken);

        RestClientAdapter clientAdapt=new RestClientAdapter(null,info, t);
        SalesforceConnector c=new SalesforceConnector(clientAdapt.getRestClient(),api,this.getContext());
        return c;
        
    }

    
    @Test
    protected void setUp() throws Exception {
        connector=getConnector();
    };
    
    
    public void testSendDummyData(){
        Random r=new Random();
        connector.sendEvents();
        for(int i=0;i<30;i++){
            double doub=20*r.nextDouble();
            float floa=20*r.nextFloat();
            int in=20*r.nextInt();
            
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
        
    }
    public void sendTestData() {

        connector.sendEvents();

    }
}
