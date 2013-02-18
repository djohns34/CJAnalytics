package edu.calpoly.codastjegga.cjanalyticsapp;

import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettingsProvider;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

public class FavoriteGraphsActivity extends GraphsActivity{

  @Override
  int getContentView() {
    // TODO Auto-generated method stub
    return R.layout.activity_favorite_graphs;
  }

  @Override
  void init() {
    // Nothing
  }

  @Override
  Loader<Cursor> createLoader(int id, Bundle args) {
    return ChartSettingsProvider.getFavoriteCursorLoader(this);
  }
}
