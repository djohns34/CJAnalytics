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
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventSummary;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

public class CJPieChart implements ChartProvider {
  private DefaultRenderer ren;
  private CategorySeries cs;

  // Name of the category series
  private final String seriesName = "My Events";

  /**
   * Parse the data from the events
   * 
   * @param chartSettings
   *          Settings for the chart
   * @param events
   *          Events to be parsed
   */
  public void parseData(ChartSettings chartSettings, EventSummary records) {
   /* SimpleSeriesRenderer ssr;
    HashMap<String, Integer> values = new HashMap<String, Integer>();

    cs = new CategorySeries(seriesName);
    ren = new DefaultRenderer();

    ChartRendererSetting.appendCustomRendererSetting(ren);

    ren.setShowLegend(false);
    ren.setLabelsTextSize(14f);
    ren.setLabelsColor(Color.BLACK);

    for (Event e : events) {
      String curr = e.getValue().toString();
      if (values.containsKey(curr)) {
        values.put(curr, values.get(curr) + 1);
      } else {
        values.put(curr, 1);
      }
    }

    for (Map.Entry<String, Integer> entry : values.entrySet()) {
      cs.add(entry.getKey(), entry.getValue());
      ssr = new SimpleSeriesRenderer();
      ssr.setColor(getRandomColor());
      ren.addSeriesRenderer(ssr);
    }*/
  }

  /**
   * Generates a random color
   * 
   * @return Random color (in the form of an integer)
   */
  private int getRandomColor() {
    Random rand = new Random();
    return Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
  }

  /**
   * Return the final, graphical view of the chart
   */
  public GraphicalView getGraphicalView(Context context) {
    return ChartFactory.getPieChartView(context, cs, ren);
  }
}