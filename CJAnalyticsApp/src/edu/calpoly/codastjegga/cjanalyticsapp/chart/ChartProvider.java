package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import java.io.Serializable;
import java.util.List;

import org.achartengine.GraphicalView;

import android.content.Context;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;

public interface ChartProvider extends Serializable{
  public void parseData(ChartSettings chartSettings, List<Event> events);
  public GraphicalView getGraphicalView(Context context);
}
