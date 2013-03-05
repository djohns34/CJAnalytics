package edu.calpoly.codastjegga.cjanalyticsapp;

import android.app.Activity;
import android.view.View;

import com.salesforce.androidsdk.app.ForceApp;
import com.salesforce.androidsdk.rest.ClientManager;
import com.salesforce.androidsdk.rest.ClientManager.LoginOptions;
import com.salesforce.androidsdk.rest.ClientManager.RestClientCallback;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.util.EventsObservable;
import com.salesforce.androidsdk.util.EventsObservable.EventType;

public abstract class PasscodeProtected{


  static void onUserInteraction() {
    ForceApp.APP.getPasscodeManager().recordUserInteraction();
  }

  static void onPause(Activity pausedActivity) {
    ForceApp.APP.getPasscodeManager().onPause(pausedActivity);
  }

  static void onResume(Activity resumedActivity) {
    checkPasscodeRequired(resumedActivity);
  }
    
  /**
   * Checks if the users is logged out from salesforce, and if so
   * redirects the to the login page.
   */
  private static void checkPasscodeRequired(final Activity resumedActivity){

    // Hide everything until we are logged in
    resumedActivity.setVisible(false);
    
    if (ForceApp.APP.getPasscodeManager().onResume(resumedActivity)) {
      
      // Login options
      String accountType = ForceApp.APP.getAccountType();
        LoginOptions loginOptions = new LoginOptions(
            null, // login host is chosen by user through the server picker 
            ForceApp.APP.getPasscodeHash(),
            resumedActivity.getString(R.string.oauth_callback_url),
            resumedActivity.getString(R.string.oauth_client_id),
            new String[] {"api"});
      
      // Get a rest client
      new ClientManager(resumedActivity, accountType, loginOptions).getRestClient(resumedActivity, new RestClientCallback() {
        @Override
        public void authenticatedRestClient(RestClient client) {
          if (client == null) {
            ForceApp.APP.logout(resumedActivity);
            return;
          }
          //set apps application
          CJAnalyticsApp cjAnalyApp = (CJAnalyticsApp) resumedActivity.getApplicationContext();
          //store the rest client for global use
          cjAnalyApp.setRestClient(client);

          // Show everything
          resumedActivity.setVisible(true);
          
          // Let observers know
          EventsObservable.get().notifyEvent(EventType.RenditionComplete);
        }
      });
    }
  }
}
