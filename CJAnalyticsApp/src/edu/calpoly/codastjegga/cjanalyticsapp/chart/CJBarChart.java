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
import org.achartengine.renderer.XYSeriesRenderer;

import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class CJBarChart implements ChartProvider {
  XYMultipleSeriesDataset data;
  XYMultipleSeriesRenderer ren;

  public void parseData(ChartSettings chartSettings, List<Event> events) {
    data = new XYMultipleSeriesDataset();
    ren = new XYMultipleSeriesRenderer();
    double panLimits[] = {0, Double.MAX_VALUE, 0, Double.MAX_VALUE};
    XYSeries xySeries;
    XYSeriesRenderer xysr;
    Random rand = new Random();
    HashMap<String, Integer> values = new HashMap<String, Integer>();
    int xIndex = 1;
    
    ChartRendererSetting.appendCustomRendererSetting(ren);
    ren.setPanLimits(panLimits);
    
    // Axes
    ren.setAxesColor(Color.BLACK);
    
    // X
    ren.setXLabels(0);
    
    // Y
    ren.setYLabelsColor(0, Color.BLACK);
    ren.setYLabelsAlign(Align.LEFT);
    ren.setYTitle("Number of occurences");
    ren.setAxisTitleTextSize(ChartRendererSetting.TEXT_SIZE);
    
    // Grid
    ren.setShowGridX(true);
    ren.setGridColor(Color.argb(255, 200, 200, 200));
    
    // Margin
    ren.setMarginsColor(Color.argb(0, 255, 255, 255));
    
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

      xysr = new XYSeriesRenderer();
      xysr.setColor(Color.rgb(rand.nextInt(256), rand.nextInt(256),
          rand.nextInt(256)));
      xysr.setLineWidth(20f);
      ren.addSeriesRenderer(xysr);

      xIndex += 1;
    }
  }

  public GraphicalView getGraphicalView(Context context) {
    return ChartFactory.getBarChartView(context, data, ren, Type.DEFAULT);
  }
}
