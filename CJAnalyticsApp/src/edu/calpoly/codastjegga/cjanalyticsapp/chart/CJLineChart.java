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

  XYMultipleSeriesDataset data;
  XYMultipleSeriesRenderer ren;

  public void parseData(ChartSettings chartSettings, List<Event> events) {
    XYSeriesRenderer xysr;
    TimeSeries timeSeries = new TimeSeries("My Events");
    HashMap<Date, LinkedList<Double>> values = new HashMap<Date, LinkedList<Double>>();

    data = new XYMultipleSeriesDataset();
    ren = new XYMultipleSeriesRenderer();

    // If there's an event that occurs at the same time as another event,
    // average the values

    // map from timestamp to a list of all values at that time
    for (Event event : events) {
      Double value = Double.parseDouble(event.getValue().toString()); // TODO: this is a hack
      Date time = event.getTimestamp();

      if (values.containsKey(time)) {
        values.get(time).add(value);
      } else {
        values.put(time, new LinkedList<Double>());
        values.get(time).add(value);
      }
    }

    // calculate the average value for that timestamp, add to the time series
    for (Map.Entry<Date, LinkedList<Double>> entries : values.entrySet()) {
      Double sum = Double.valueOf(0);

      for (Double entry : entries.getValue()) {
        sum += entry;
      }
      sum /= entries.getValue().size();

      timeSeries.add(entries.getKey(), sum);
    }

    data.addSeries(timeSeries);

    xysr = new XYSeriesRenderer();
    xysr.setColor(Color.GREEN);
    ren.addSeriesRenderer(xysr);
  }

  public GraphicalView getGraphicalView(Context context) {
    return ChartFactory.getTimeChartView(context, data, ren, "MM/dd/yyyy");
  }
}
