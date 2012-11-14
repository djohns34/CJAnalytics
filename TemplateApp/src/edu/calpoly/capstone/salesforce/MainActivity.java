/*
 * Copyright (c) 2011, salesforce.com, inc.
 * All rights reserved.
 * Redistribution and use of this software in source and binary forms, with or
 * without modification, are permitted provided that the following conditions
 * are met:
 * - Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * - Neither the name of salesforce.com, inc. nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission of salesforce.com, inc.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.calpoly.capstone.salesforce;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.salesforce.androidsdk.app.ForceApp;
import com.salesforce.androidsdk.rest.ClientManager;
import com.salesforce.androidsdk.rest.ClientManager.LoginOptions;
import com.salesforce.androidsdk.rest.ClientManager.RestClientCallback;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.security.PasscodeManager;
import com.salesforce.androidsdk.util.EventsObservable;
import com.salesforce.androidsdk.util.EventsObservable.EventType;

/**
 * Main activity
 */
public class MainActivity extends Activity {

	private PasscodeManager passcodeManager;
    
    private ArrayAdapter<String> listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Passcode manager
		passcodeManager = ForceApp.APP.getPasscodeManager();		
		
		// Setup view
		setContentView(R.layout.main);

		// Let observers know
		EventsObservable.get().notifyEvent(EventType.MainActivityCreateComplete, this);
	}
	
	@Override 
	public void onResume() {
		super.onResume();
		
		// Hide everything until we are logged in
		findViewById(R.id.root).setVisibility(View.INVISIBLE);
		
		// Create list adapter
		listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
		((ListView) findViewById(R.id.contacts_list)).setAdapter(listAdapter);				
		
		// Bring up passcode screen if needed
		if (passcodeManager.onResume(this)) {
		
			// Login options
			String accountType = ForceApp.APP.getAccountType();
	    	LoginOptions loginOptions = new LoginOptions(
	    			null, // login host is chosen by user through the server picker 
	    			ForceApp.APP.getPasscodeHash(),
	    			getString(R.string.oauth_callback_url),
	    			getString(R.string.oauth_client_id),
	    			new String[] {"api"});
			
			// Get a rest client
			new ClientManager(this, accountType, loginOptions).getRestClient(this, new RestClientCallback() {
				@Override
				public void authenticatedRestClient(RestClient client) {
					if (client == null) {
						ForceApp.APP.logout(MainActivity.this);
						return;
					}

                    // Keeping reference to rest client
                   RestHelper.setRestClient(client); 

					// Show everything
					findViewById(R.id.root).setVisibility(View.VISIBLE);
					
					// Let observers know
					EventsObservable.get().notifyEvent(EventType.RenditionComplete);
				}
			});
		}
	}

	@Override
	public void onUserInteraction() {
		passcodeManager.recordUserInteraction();
	}
	
    @Override
    public void onPause() {
    	passcodeManager.onPause(this);
        super.onPause();
    }
	

	/**
	 * Called when "Logout" button is clicked. 
	 * 
	 * @param v
	 */
	public void onLogoutClick(View v) {
		ForceApp.APP.logout(this);
	}
	
	/**
	 * Called when "Clear" button is clicked. 
	 * 
	 * @param v
	 */
	public void onClearClick(View v) {
		listAdapter.clear();
	}	

	/**
	 * Called when "Fetch Contacts" button is clicked
	 * 
	 * @param v
	 * @throws UnsupportedEncodingException 
	 */
	public void onFetchContactsClick(View v) throws UnsupportedEncodingException {
        sendRequest("SELECT Name, mynum__c FROM stuff__c");
	}
	
	/**
	 * Called when "Add Contact" is clicked
	 * 
	 * @param v
	 * 
	 */
	public void addContactClick(View v) throws UnsupportedEncodingException {
		Intent myIntent = new Intent(v.getContext(), SendData.class);
		startActivityForResult(myIntent, 0);
	}
	

	/**
	 * Called when "Fetch Accounts" button is clicked
	 * 
	 * @param v
	 * @throws UnsupportedEncodingException 
	 */
	public void onFetchAccountsClick(View v) throws UnsupportedEncodingException {
		sendRequest("SELECT Name FROM Account");
	}	
	
	private void sendRequest(String soql) throws UnsupportedEncodingException {
		RestRequest restRequest = RestRequest.getRequestForQuery(getString(R.string.api_version), soql);

		RestHelper.getRestClient().sendAsync(restRequest, new AsyncRequestCallback() {
			//@Override
			public void onSuccess(RestRequest request, RestResponse result) {
				try {
					System.out.println(result);
					Log.d("restRequet result", result.asString());
					listAdapter.clear();
					JSONArray records = result.asJSONObject().getJSONArray("records");
					for (int i = 0; i < records.length(); i++) {
						listAdapter.add(records.getJSONObject(i).getString("Name"));
					}					
				} catch (Exception e) {
					onError(e);
				}
			}
			
		//	@Override
			public void onError(Exception exception) {
                Toast.makeText(MainActivity.this,
                               MainActivity.this.getString(ForceApp.APP.getSalesforceR().stringGenericError(), exception.toString()),
                               Toast.LENGTH_LONG).show();
			}
		});
	}
	
	public void pieChart(View view) {
		Intent intent = new Intent(this, PieChartActivity.class);
		startActivity(intent);
	}

	public void changeValues(View view){
		Intent intent = new Intent(this, ChangeValue.class);
		startActivity(intent);

	}	
	
}
