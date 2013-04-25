package edu.calpoly.codastjegga.cjanalyticsapp.datafetcher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ParseException;
import android.widget.Toast;

import com.google.gson.Gson;
import com.salesforce.androidsdk.app.ForceApp;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestRequest.RestMethod;
import com.salesforce.androidsdk.rest.RestResponse;

import edu.calpoly.codastjegga.cjanalyticsapp.CJAnalyticsApp;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.TimeInterval;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventType;
import edu.calpoly.codastjegga.cjanalyticsapp.utils.DateUtils;

public class ApexFetcher {

  public static void doPost(final CJAnalyticsApp cjAnalyApp) {
    RestClient client = cjAnalyApp.getRestClient();

    Map<String, String> fields = new HashMap<String, String>();
    fields.put("appName", "Temple Run");
    fields.put("eventName", "Name");
    fields.put("eventField", EventType.Text.getEventField().getColumnId());
    fields.put("startTime", DateUtils.format(new Date(1,1,1)));
    fields.put("endTime", DateUtils.format(new Date(2888,1,1)));
    fields.put("timeInterval", TimeInterval.Monthly.name());
    
    StringEntity entity;
    try {
      entity = new StringEntity(new JSONObject(fields).toString(), HTTP.UTF_8);
    } catch (UnsupportedEncodingException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
      return;
    }
    entity.setContentType("application/json"); 

    String path = "services/apexrest/codastjegga/DataSummarizer";
    
    RestRequest req = new RestRequest(RestMethod.POST, path, entity);


    client.sendAsync(req, new AsyncRequestCallback() {

      @Override
      public void onSuccess(RestRequest request, RestResponse response) {
        try {
//          if(response.getStatusCode() == 0){
            // All of the quotations are escaped, we don't want that.
            String responseString = response.asString().replace("\\", "");
            //The String also starts and ends with quotes.
            responseString=responseString.substring(1,responseString.length()-1);
            Toast.makeText(cjAnalyApp, responseString, Toast.LENGTH_LONG)
                .show();

            ApexResponse resp = new Gson().fromJson(responseString,ApexResponse.class);

            System.out.println(resp);
//        }
            
        } catch (org.apache.http.ParseException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }

      }

      @Override
      public void onError(Exception exception) {
        Toast.makeText(cjAnalyApp,cjAnalyApp.getString(ForceApp.APP.getSalesforceR()
                    .stringGenericError(), exception.toString()),
                Toast.LENGTH_LONG).show();

      }
    });
  }
  
  private class ApexResponse{
    
    Map<String,Double> summarized;
    Map<String,Integer> categorical;
    
  }
  
  
  
}
