package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import java.util.List;

import org.achartengine.GraphicalView;

import android.content.Context;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;

public interface ChartProvider {
  public GraphicalView getGraphicalView(Context context, List<Event> events);
}
