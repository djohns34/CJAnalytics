package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

public class CJPieChart implements ChartProvider {
  private DefaultRenderer ren;
  private CategorySeries cs;

  public void parseData(ChartSettings chartSettings, List<Event> events) {
    SimpleSeriesRenderer ssr;
    Random rand = new Random();
    HashMap<String, Integer> values = new HashMap<String, Integer>();

    cs = new CategorySeries("My Events");
    ren = new DefaultRenderer();

    for (Event e : events) {
      String curr = e.getValue().toString();
      if (values.containsKey(curr)) {
        values.put(curr, values.get(curr) + 1);
      } else {
        values.put(curr, 1);
      }
    }

    for (Map.Entry<String, Integer> entry : values.entrySet()) {
      Log.d(entry.getKey(), entry.getValue().toString());
      cs.add(entry.getKey(), entry.getValue());
      ssr = new SimpleSeriesRenderer();
      ssr.setColor(Color.rgb(rand.nextInt(256), rand.nextInt(256),
          rand.nextInt(256)));
      ren.addSeriesRenderer(ssr);
    }
  }

  public GraphicalView getGraphicalView(Context context) {
    return ChartFactory.getPieChartView(context, cs, ren);
  }
}