package edu.calpoly.codastjegga.cjanalyticsapp;

import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettingsProvider;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class RecentChartsActivity extends ChartsActivity{
  
  /*Maybe a setting at some time*/
  int howMany=5;

  @Override
  int getContentView() {
    return R.layout.activity_charts_no_add;
  }

  @Override
  void init() {
    ((TextView)findViewById(android.R.id.empty)).setText(R.string.no_recent_charts);
  }

  @Override
  Loader<Cursor> createLoader(int id, Bundle args) {
    return ChartSettingsProvider.getRecentCursorLoader(this,howMany);
  }
}
