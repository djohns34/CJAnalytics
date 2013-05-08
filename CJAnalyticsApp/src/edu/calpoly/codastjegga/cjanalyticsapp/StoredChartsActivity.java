package edu.calpoly.codastjegga.cjanalyticsapp;

import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettingsProvider;
import edu.calpoly.codastjegga.cjanalyticsapp.dashboard.Dashboard;

public class StoredChartsActivity extends ChartsActivity{
  
  private Dashboard dashboard;

  
  @Override
  int getContentView() {
    return R.layout.activity_charts;
  }
  @Override
  Loader<Cursor> createLoader(int id, Bundle args) {
    //TODO Specify database name
    Dashboard dashboard = (Dashboard) args.get(Dashboard.class.toString());
    return  ChartSettingsProvider.getCursorLoader(this, dashboard.getDashboardName());
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
    getMenuInflater().inflate(R.menu.activity_charts, menu);
    return true;
  }

 
  public void onClickChartAdd(MenuItem item) {
    Intent intent = new Intent(this, EditActivity.class);
    ChartSettings chartSetting = new ChartSettings();
    chartSetting.setDatabase(dashboard.getDashboardName());
    chartSetting.saveToIntent(intent);
    intent.putExtra(ChartSettings.class.getName(), chartSetting);
    startActivity(intent);
    overridePendingTransition(R.anim.slide_in_top, R.anim.none);
  }
  @Override
  public void finish() {
    super.finish();/*slide back to the dashboard activity, instead of down to the main screen(unlike the parent class)*/
    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
  }
}
