package com.example.bulkapitestapp;

import java.net.URI;
import java.net.URISyntaxException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import edu.calpoly.codastjegga.auth.ClientInfo;
import edu.calpoly.codastjegga.bulk.DataLoader;


public class MainActivity extends Activity {

	String csvFile = "TrackedEventsCusTime.csv";
	String sObj = "codastjegga__TrackedEvents__c";
	
	static String instanceUrl = "https://na9.salesforce.com";
	static String loginUrl = "";
	static String refreshToken = "5Aep861rEpScxnNE64IogmmXsb72ONfi9xneXYd7423HJqVESjk2lAGpq2CtCH9GCb2rjIR7E0EKg==";
	static String clientId = "3MVG9y6x0357HlefAMuoGiOstBQg3r5rHpEodDBNQ6GIjd5Fh.RPRX4YA6Ax_sg69Jq8tpuFU76NlE0z5LTbc";
	//static String appName ="MyApp";
	ClientInfo client;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	       //Create new URI for login instance
        URI loginInstanceURI;
        URI salesforceLoginURI;
		try {
			loginInstanceURI = new URI(instanceUrl);
        //Create new URI for salesforce login (used for refreshing tokens)
        salesforceLoginURI = new URI("https://login.salesforce.com");
        
        client = new ClientInfo(clientId,loginInstanceURI, salesforceLoginURI, refreshToken); 
        		 
     
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendOnClick(View view) {
		new SendData().execute(client);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private class SendData extends AsyncTask<ClientInfo, Void, Void> {

		@Override
		protected Void doInBackground(ClientInfo... params) {
			try {
			DataLoader loader = new DataLoader(params[0], sObj);
			loader.sendCSV(getAssets().open(csvFile));
			loader.closeUploadJob();
//			Toast.makeText(getApplicationContext(), 
//					"Exception: data is loading", Toast.LENGTH_LONG);
			int i;
			}catch (Exception ex) {
//				Toast.makeText(getApplicationContext(), 
//						"Exception: " + ex, Toast.LENGTH_LONG);
				int i;
			}
			return null;
		}
		
	}

}
