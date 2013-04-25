package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;

import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class CJBarChart implements ChartProvider {
  XYMultipleSeriesRenderer renderer;
  XYMultipleSeriesDataset dataset;

  @Override
  public void parseData(ChartSettings chartSettings, List<Event> events) {
    int[] colors = new int[] { Color.parseColor("#77c4d3") };
    this.buildBarRenderer(colors);
    this.buildBarDataset(events);

    renderer.setOrientation(Orientation.HORIZONTAL);
    renderer.setXLabels(0);
    renderer.setYLabels(10);
    int length = renderer.getSeriesRendererCount();
    for (int i = 0; i < length; ++i) {
      SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
      seriesRenderer.setDisplayChartValues(true);
    }
  }

  protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
    renderer = new XYMultipleSeriesRenderer();

    renderer.setAxisTitleTextSize(16);
    renderer.setLabelsTextSize(15);

    renderer.setBarSpacing(1);

    renderer.setShowGridX(true);
    renderer.setGridColor(Color.argb(50, 200, 200, 200));
    renderer.setAxesColor(Color.argb(100, 200, 200, 200));

    renderer.setShowLegend(false);

    renderer.setMargins(new int[] { 0, 30, 15, 0 });
    renderer.setMarginsColor(Color.parseColor("#d7f2fb"));

    renderer.setXLabelsColor(Color.BLACK);
    renderer.setXLabelsAngle(90);
    renderer.setXLabelsAlign(Align.RIGHT);

    renderer.setYLabelsColor(0, Color.BLACK);
    renderer.setYLabelsAlign(Align.RIGHT);

    renderer.setApplyBackgroundColor(true);
    renderer.setBackgroundColor(Color.parseColor("#FBFBFC"));

    int length = colors.length;
    for (int i = 0; i < length; ++i) {
      SimpleSeriesRenderer r = new SimpleSeriesRenderer();
      r.setColor(colors[i]);
      renderer.addSeriesRenderer(r);
    }
    return renderer;
  }

  protected void buildBarDataset(List<Event> events) {
    HashMap<String, Integer> values = new HashMap<String, Integer>();
    dataset = new XYMultipleSeriesDataset();
    XYSeries series = new XYSeries("");
    int xIndex = 1;
    double highest = 0;

    // Count data from events
    for (Event e : events) {
      String curr = e.getValue().toString();
      if (values.containsKey(curr)) {
        values.put(curr, values.get(curr) + 1);
        if (values.get(curr) > highest) {
          highest = values.get(curr);
        }
      } else {
        values.put(curr, 1);
      }
    }

    // Add data to graph
    for (Map.Entry<String, Integer> entry : values.entrySet()) {
      renderer.addXTextLabel(xIndex, entry.getKey());
      series.add(xIndex++, entry.getValue());
    }

    dataset.addSeries(series);

    this.renderer.setXAxisMax(values.size() + 1);
    this.renderer.setYAxisMax(highest + (highest * 0.25));
    this.renderer.setXAxisMin(0);
    this.renderer.setYAxisMin(0);

    this.renderer.setPanLimits(new double[] { 0, values.size() + 1, 0,
        highest + (highest * 0.25) });
  }

  public GraphicalView getGraphicalView(Context context) {
    return ChartFactory.getBarChartView(context, dataset, renderer,
        Type.DEFAULT);
  }
}
