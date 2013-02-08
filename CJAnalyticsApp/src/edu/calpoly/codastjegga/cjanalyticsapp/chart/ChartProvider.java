package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import org.achartengine.GraphicalView;

import android.content.Context;

public interface ChartProvider {
  public GraphicalView getView(Context context);
}
