package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;

public class CJLineChart implements ChartProvider {
  private XYMultipleSeriesDataset data;
  private XYMultipleSeriesRenderer ren;
  
  /**
   * Parse the data from the events
   * @param chartSettings Settings for the chart
   * @param events Events to be parsed
   */
  public void parseData(ChartSettings chartSettings, List<Event> events) {
    XYSeriesRenderer xysr = new XYSeriesRenderer();
    TimeSeries timeSeries;
    HashMap<Date, LinkedList<Double>> groups;
    
    xysr.setColor(Color.GREEN);
    
    data = new XYMultipleSeriesDataset();
    ren = new XYMultipleSeriesRenderer();

    groups = groupByTimestamp(events);
    timeSeries = getSeriesFromGroups("My Event", groups);

    data.addSeries(timeSeries);
    ren.addSeriesRenderer(xysr);
  }
  
  /**
   * Group a list of events by timestamp
   * @param events Events to be converted to a map
   * @return A map mapping timestamp to a list of all values at that time
   */
  protected HashMap<Date, LinkedList<Double>> groupByTimestamp(List<Event> events) {
    HashMap<Date, LinkedList<Double>> values = new HashMap<Date, LinkedList<Double>>();
    
    for (Event event : events) {
      Double value = Double.parseDouble(event.getValue().toString());
      Date time = event.getTimestamp();

      if (values.containsKey(time)) {
        values.get(time).add(value);
      } else {
        values.put(time, new LinkedList<Double>());
        values.get(time).add(value);
      }
    }
    
    return values;
  }
  
  /**
   * Generate a time series from grouped events
   * @param name Name of the time series
   * @param groups Groups of events (by timestamp)
   * @return TimeSeries for the inputted data
   */
  protected TimeSeries getSeriesFromGroups(String name, HashMap<Date, LinkedList<Double>> groups) {
    TimeSeries timeSeries = new TimeSeries(name);

    // calculate the average value for that timestamp, add to the time series
    for (Map.Entry<Date, LinkedList<Double>> entries : groups.entrySet()) {
      Double sum = Double.valueOf(0);

      for (Double entry : entries.getValue()) {
        sum += entry;
      }
      sum /= entries.getValue().size();

      timeSeries.add(entries.getKey(), sum);
    }
    
    return timeSeries;
  }
  
  protected XYMultipleSeriesDataset getData() {
    return this.data;
  }
  
  protected XYMultipleSeriesRenderer getRenderer() {
    return this.ren;
  }

  /**
   * Return the final, graphical view of the chart
   */
  public GraphicalView getGraphicalView(Context context) {
    if (data == null || ren == null)
      return null;
    return ChartFactory.getTimeChartView(context, data, ren, "MM/dd/yyyy");
  }
}
