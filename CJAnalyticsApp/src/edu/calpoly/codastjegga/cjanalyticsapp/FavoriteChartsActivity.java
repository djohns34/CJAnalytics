package edu.calpoly.codastjegga.cjanalyticsapp;

import com.salesforce.androidsdk.app.ForceApp;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettingsProvider;

public class FavoriteChartsActivity extends ChartsActivity{

  @Override
  int getContentView() {
    return R.layout.activity_charts_no_add;
  }

  @Override
  void init() {
    ((TextView)findViewById(android.R.id.empty)).setText(R.string.no_favorite_charts);
  }

  @Override
  Loader<Cursor> createLoader(int id, Bundle args) {
    return ChartSettingsProvider.getFavoriteCursorLoader(this);
  }
  
  public void onLogoutClick(MenuItem menu) {
    ForceApp.APP.logout(this);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.generic_activity, menu);
    return true;
  }
}
