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

import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

public class CJPieChart implements ChartProvider {
  public GraphicalView getGraphicalView(Context context, List<Event> events) {
    CategorySeries cs = new CategorySeries("Chart Title Here");
    DefaultRenderer ren = new DefaultRenderer();
    SimpleSeriesRenderer ssr;
    Random rand = new Random();
    HashMap<String, Integer> values = new HashMap<String, Integer>();

    // TODO: Need a way to get
    ren.setChartTitle("Chart Title Here");

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
      ssr.setColor(Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
      ren.addSeriesRenderer(ssr);
    }

    return ChartFactory.getPieChartView(context, cs, ren);
  }
}