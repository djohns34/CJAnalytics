package edu.calpoly.codastjegga.cjanalyticsapp;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
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
}
