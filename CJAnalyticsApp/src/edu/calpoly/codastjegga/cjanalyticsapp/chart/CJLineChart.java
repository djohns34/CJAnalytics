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
import android.graphics.Paint.Align;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventSummary;

public class CJLineChart implements ChartProvider {
  private XYMultipleSeriesDataset data;
  private XYMultipleSeriesRenderer ren;

  // name of the time series
  private final String seriesName = "My Event";

  /**
   * Parse the data from the events
   * 
   * @param chartSettings
   *          Settings for the chart
   * @param events
   *          Events to be parsed
   */
  public void parseData(ChartSettings chartSettings, EventSummary records) {
   /* XYSeriesRenderer xysr = new XYSeriesRenderer();
    TimeSeries timeSeries;
    HashMap<Date, LinkedList<Double>> groups;

    data = new XYMultipleSeriesDataset();
    ren = new XYMultipleSeriesRenderer();

    ChartRendererSetting.appendCustomRendererSetting(ren);

    // Line
    xysr.setColor(Color.BLUE);
    xysr.setLineWidth(3);

    ren.setLegendTextSize(14);

    // Axes
    ren.setAxesColor(Color.argb(100, 200, 200, 200));

    // Axes titles/abels
    ren.setAxisTitleTextSize(16);
    ren.setLabelsTextSize(12);

    // X
    ren.setXLabelsColor(Color.BLACK);

    // Y
    ren.setYLabelsColor(0, Color.BLACK);
    ren.setYLabelsAlign(Align.RIGHT);

    // Grid
    ren.setShowGrid(true);
    ren.setGridColor(Color.argb(50, 200, 200, 200));

    // Margin
    ren.setMargins(new int[] { 0, 30, 15, 0 });
    ren.setMarginsColor(Color.parseColor("#eaf8fd"));

    ren.setApplyBackgroundColor(true);
    ren.setBackgroundColor(Color.parseColor("#FBFBFC"));

    groups = groupByTimestamp(events);
    timeSeries = getSeriesFromGroups(seriesName, groups);

    data.addSeries(timeSeries);
    ren.addSeriesRenderer(xysr);*/
  }

  /**
   * Group a list of events by timestamp
   * 
   * @param events
   *          Events to be converted to a map
   * @return A map mapping timestamp to a list of all values at that time
   */
  protected HashMap<Date, LinkedList<Double>> groupByTimestamp(
      List<Event> events) {
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
   * 
   * @param name
   *          Name of the time series
   * @param groups
   *          Groups of events (by timestamp)
   * @return TimeSeries for the inputted data
   */
  protected TimeSeries getSeriesFromGroups(String name,
      HashMap<Date, LinkedList<Double>> groups) {
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
