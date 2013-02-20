package edu.calpoly.codastjegga.cjanalyticsapp;

import java.util.Random;

import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettingsProvider;
import edu.calpoly.codastjegga.cjanalyticsapp.dashboard.Dashboard;

public class StoredGraphsActivity extends GraphsActivity{
  
  private Dashboard dashboard;

  
  @Override
  int getContentView() {
    return R.layout.activity_graphs;
  }
  @Override
  Loader<Cursor> createLoader(int id, Bundle args) {
    //TODO Specify database name
    return  ChartSettingsProvider.getCursorLoader(this,null);
  }
  
  @Override
  void init() {
    //get the extras from the intent
    Bundle extras = getIntent().getExtras();
    //get the dashboard, it should never be null
    assert(dashboard != null);
    dashboard = (Dashboard) extras.get(Dashboard.class.toString());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_graphs, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    testAdd();
    testAdd();
    switch (item.getItemId()) {
    case android.R.id.home:
      // This ID represents the Home or Up button. In the case of this
      // activity, the Up button is shown. Use NavUtils to allow users
      // to navigate up one level in the application structure. For
      // more details, see the Navigation pattern on Android Design:
      //
      // http://developer.android.com/design/patterns/navigation.html#up-vs-back
      //
      NavUtils.navigateUpFromSameTask(this);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
    
  //TODO Remove Me!!
  public void testAdd(){
    ChartSettings testSetting=new ChartSettings(ChartType.values()[new Random().nextInt(3)], "Temple Run","Graph Name", "Wage", null, null);
    
    /*Insert the test setting*/
    
    ChartSettingsProvider.saveSettings(getContentResolver(), testSetting);
  }

 
  public void onClickChartAdd(MenuItem item) {
    Intent intent = new Intent(this, EditActivity.class);
    ChartSettings chartSetting = new ChartSettings();
    chartSetting.setDatabase(dashboard.getDashboardName());
    chartSetting.saveToIntent(intent);
    intent.putExtra(ChartSettings.class.getName(), chartSetting);
    startActivity(intent);
  }
}
