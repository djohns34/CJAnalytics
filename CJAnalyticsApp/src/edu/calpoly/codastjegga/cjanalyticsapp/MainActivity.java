/*
 * Copyright (c) 2012, salesforce.com, inc.
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
package edu.calpoly.codastjegga.cjanalyticsapp;

import java.util.zip.Inflater;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.salesforce.androidsdk.app.ForceApp;
import com.salesforce.androidsdk.rest.ClientManager.LoginOptions;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.NativeMainActivity;

import edu.calpoly.codastjegga.cjanalyticsapp.adapter.MainScreenIcons;


/**
 * Main activity
 */
public class MainActivity extends NativeMainActivity implements OnClickListener {

  //Resource path to alger font
  private static String APP_NAME_FONT_PATH = "fonts/ALGER.TTF";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Setup view
    setContentView(R.layout.main);
    //load Alger font
    Typeface type = Typeface.createFromAsset(getAssets(), APP_NAME_FONT_PATH); 
    //find appName
    TextView appName = (TextView)findViewById(R.id.appName); 
    //SET the font to appName
    appName.setTypeface(type);
    //add the set of main screen icons
    addIcons();
  }
  
  private void addIcons() {
    //Find the icon panel in the view
    LinearLayout iconPanel = (LinearLayout)findViewById(R.id.icon_panel);
    iconPanel.setHapticFeedbackEnabled(true);
    
    //FOR each main screen icon
    for (MainScreenIcons icon : MainScreenIcons.values()) {
      //Create a new relative layout from activity_main_icon_item
      View iconView = RelativeLayout.inflate(this, R.layout.activity_main_icon_item, null);
      //find the image view in the iconHolder
      ImageView image = (ImageView) iconView.findViewById(R.id.icon_item_image);
      //set the image
      image.setImageResource(icon.image);
      //find the image label in the iconHolder
      TextView label = (TextView)iconView.findViewById(R.id.icon_item_label);
      //set the icon label
      label.setText(getString(icon.name));
      //set the view to be the same as icon name id
      iconView.setId(icon.name);
      iconView.setOnClickListener(this);
     
      
      //add the iconView to iconPanel
      iconPanel.addView(iconView);
    }
  }

  @Override 
  public void onResume() {
    // Hide everything until we are logged in
    findViewById(R.id.root).setVisibility(View.INVISIBLE);

    // Create list adapter
    //listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
    //((ListView) findViewById(R.id.contacts_list)).setAdapter(listAdapter);				

    super.onResume();
  }		

  @Override
  protected LoginOptions getLoginOptions() {
    LoginOptions loginOptions = new LoginOptions(
        null, // login host is chosen by user through the server picker 
        ForceApp.APP.getPasscodeHash(),
        getString(R.string.oauth_callback_url),
        getString(R.string.oauth_client_id),
        new String[] {"api"});
    return loginOptions;
  }

  @Override
  public void onResume(RestClient client) {
    //set apps application
    CJAnalyticsApp cjAnalyApp = (CJAnalyticsApp) getApplicationContext();
    //store the rest client for global use
    cjAnalyApp.setRestClient(client);
    // Show everything
    findViewById(R.id.root).setVisibility(View.VISIBLE);
  }



  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.logout:
      ForceApp.APP.logout(this);
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }


  /**
   * Called when "Logout" option is clicked. 
   * 
   * @param v
   */
  public void onLogoutClick(MenuItem menu) {
    ForceApp.APP.logout(this);
  }

  public void onDashboardClick(View v) {
  }	
  public void onFavoritesClick(View v) {
    Intent intent = new Intent(this, FavoriteChartsActivity.class);
    startActivity(intent);
  } 

  public void onRecentClick(View v) {
    Intent intent = new Intent(this, RecentChartsActivity.class);
    startActivity(intent);
  }

  @Override
  public void onClick(View view) {
    Intent intent = null;
    switch(view.getId()) {
    
    case R.string.dashboards:
       intent = new Intent(this, DashboardsActivity.class);
      break;
    case R.string.favorites:
       intent = new Intent(this, FavoriteChartsActivity.class);
      break;
      
    case R.string.recents:
       intent = new Intent(this, RecentChartsActivity.class);
      startActivity(intent);
      break;
      
    }
    if (intent != null)
    startActivity(intent);
    
  } 
}
