package edu.calpoly.codastjegga.cjanalyticsapp;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettingsProvider;

public class RecentGraphsActivity extends GraphsActivity{

  @Override
  int getContentView() {
    return R.layout.activity_graphs_no_add;
  }

  @Override
  void init() {
    ((TextView)findViewById(android.R.id.empty)).setText(R.string.no_recent_graphs);
  }

  @Override
  Loader<Cursor> createLoader(int id, Bundle args) {
    //TODO change this so it gets a loader for recent graphs
    return ChartSettingsProvider.getFavoriteCursorLoader(this);
  }
}
