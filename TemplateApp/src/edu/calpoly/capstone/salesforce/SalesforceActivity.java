package edu.calpoly.capstone.salesforce;

import com.salesforce.androidsdk.app.ForceApp;
import com.salesforce.androidsdk.rest.ClientManager;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.ClientManager.LoginOptions;
import com.salesforce.androidsdk.rest.ClientManager.RestClientCallback;
import com.salesforce.androidsdk.security.PasscodeManager;
import com.salesforce.androidsdk.util.EventsObservable;
import com.salesforce.androidsdk.util.EventsObservable.EventType;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public abstract class SalesforceActivity extends Activity {

	private PasscodeManager passcodeManager;
	
	
    abstract int getViewID();

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Passcode manager
		passcodeManager = ForceApp.APP.getPasscodeManager();		
		
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
	
	@Override 
	protected void onResume() {
		checkPasscodeRequired();
		super.onResume();
	}
    
	
	private void checkPasscodeRequired(){

		final int resId =getViewID();
		// Hide everything until we are logged in
		findViewById(resId).setVisibility(View.INVISIBLE);
		
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
						ForceApp.APP.logout(SalesforceActivity.this);
						return;
					}

                    // Keeping reference to rest client
                   RestHelper.setRestClient(client); 

					// Show everything
					findViewById(resId).setVisibility(View.VISIBLE);
					
					// Let observers know
					EventsObservable.get().notifyEvent(EventType.RenditionComplete);
				}
			});
		}
	}
	
	/**
	 * Called when "Logout" button is clicked. 
	 * 
	 * @param v
	 */
	public void onLogoutClick(View v) {
		ForceApp.APP.logout(this);
	}

}
