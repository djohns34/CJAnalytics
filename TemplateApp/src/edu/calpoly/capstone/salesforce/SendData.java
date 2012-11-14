package edu.calpoly.capstone.salesforce;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import com.salesforce.androidsdk.app.ForceApp;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SendData extends Activity {
	private RestClient client;
	private static String objectType = "stuff__c";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.senddata);
	}
	
	/**
	 * Called when "Back" is clicked
	 * 
	 * @param v
	 * 
	 */
	public void addBackClick(View v) throws UnsupportedEncodingException {
		Intent myIntent = new Intent(v.getContext(), MainActivity.class);
		startActivityForResult(myIntent, 0);
	}
	
	public void addAddContactClick(View v) throws UnsupportedEncodingException {
		Map<String, Object> fields = new HashMap<String, Object>();
		
	 	EditText name = (EditText)findViewById(R.id.nameTextField);
	 	EditText phoneNum = (EditText)findViewById(R.id.phoneTextField);
	 	fields.put("Name", name.getText().toString());
	 	fields.put("mynum__c", phoneNum.getText().toString());
	 	
	 	Log.d("restRequet result", name.getText().toString());
	 	Log.d("restRequet result", phoneNum.getText().toString());
	 	String id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
	 	Log.d("Device id", id);
	
	 	try {
	 		RestRequest request = RestRequest.getRequestForCreate(
	 				getString(R.string.api_version), 
	 				objectType, 
	 				fields);
	 		sendRequest(request);
	 		
	 	}catch (Exception e)
	 	{
	 		
	 	}
	}
	
	private void sendRequest(RestRequest restRequest) throws UnsupportedEncodingException {
		

		RestHelper.getRestClient().sendAsync(restRequest, new AsyncRequestCallback() {
			//@Override
			public void onSuccess(RestRequest request, RestResponse result) {
				try {
					System.out.println(result);
					Log.d("restRequet result", result.asString());
					Toast.makeText(SendData.this, 
							"Contact added",
							Toast.LENGTH_LONG).show();
						
				} catch (Exception e) {
					onError(e);
				}
			}
			
		//	@Override
			public void onError(Exception exception) {
                Toast.makeText(SendData.this,
                               SendData.this.getString(ForceApp.APP.getSalesforceR().stringGenericError(), exception.toString()),
                               Toast.LENGTH_LONG).show();
			}
		});
	}
}
