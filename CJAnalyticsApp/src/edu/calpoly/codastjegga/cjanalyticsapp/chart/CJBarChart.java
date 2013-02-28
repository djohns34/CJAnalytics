package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;

import android.content.Context;
import android.graphics.Color;

public class CJBarChart implements ChartProvider {
  XYMultipleSeriesDataset data;
  XYMultipleSeriesRenderer ren;

  public void parseData(ChartSettings chartSettings, List<Event> events) {
    data = new XYMultipleSeriesDataset();
    ren = new XYMultipleSeriesRenderer();
    XYSeries xySeries;
    SimpleSeriesRenderer ssr;
    Random rand = new Random();
    HashMap<String, Integer> values = new HashMap<String, Integer>();
    int xIndex = 1;

    for (Event e : events) {
      String curr = e.getValue().toString();
      if (values.containsKey(curr)) {
        values.put(curr, values.get(curr) + 1);
      } else {
        values.put(curr, 1);
      }
    }

    for (Map.Entry<String, Integer> entry : values.entrySet()) {
      xySeries = new XYSeries(entry.getKey());
      xySeries.add(xIndex, entry.getValue());
      data.addSeries(xySeries);
      ren.addXTextLabel(xIndex, xySeries.getTitle());

      ssr = new SimpleSeriesRenderer();
      ssr.setColor(Color.rgb(rand.nextInt(256), rand.nextInt(256),
          rand.nextInt(256)));
      ren.addSeriesRenderer(ssr);

      xIndex += 1;
    }

    ren.setXLabels(0);
    ren.setBarSpacing(.5);
    ren.setYAxisMin(0);
    ren.setXAxisMin(0);

    ren.setMarginsColor(Color.argb(0, 255, 255, 255));
  }

  public GraphicalView getGraphicalView(Context context) {
    return ChartFactory.getBarChartView(context, data, ren, Type.DEFAULT);
  }
}
