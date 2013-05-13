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
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventSummary;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;

public class CJBarChart implements ChartProvider {
  private static final double TOP_PADDING = 1.25;
  private static final double RIGHT_PADDING = 1;

  XYMultipleSeriesRenderer renderer;
  XYMultipleSeriesDataset dataset;

  @Override
  public void parseData(ChartSettings chartSettings, EventSummary... events) {
    int[] colors;

    dataset = new XYMultipleSeriesDataset();

    if (events.length == 2) {
      colors = new int[] { Color.MAGENTA, Color.GREEN };
      this.buildBarDataset(events[0].getCategorical());
      this.buildBarDataset(events[1].getCategorical());
    } else if (events.length == 1) {
      colors = new int[] { Color.MAGENTA };
      this.buildBarRenderer(colors);
      this.buildBarDataset(events[0].getCategorical());
    }

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

    // TODO: Needs to scale based off screen size
    renderer.setAxisTitleTextSize(16);
    renderer.setLabelsTextSize(15);

    renderer.setBarSpacing(1);

    renderer.setShowGridX(true);
    renderer.setGridColor(Color.argb(50, 200, 200, 200));
    renderer.setAxesColor(Color.argb(100, 200, 200, 200));

    renderer.setShowLegend(false);

    renderer.setMargins(new int[] { 0, 30, 15, 0 });
    renderer.setMarginsColor(Color.parseColor("#eaf8fd"));

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

  protected void buildBarDataset(Map<String, Integer> values) {
    XYSeries series = new XYSeries("");
    int xIndex = 1;
    double highest = 0;

    // Add data to graph
    for (Map.Entry<String, Integer> entry : values.entrySet()) {
      renderer.addXTextLabel(xIndex, entry.getKey());
      series.add(xIndex++, entry.getValue());
      if (entry.getValue() > highest) {
        highest = entry.getValue();
      }
    }

    dataset.addSeries(series);

    this.renderer.setXAxisMax(values.size() + RIGHT_PADDING);
    this.renderer.setYAxisMax(highest * TOP_PADDING);
    this.renderer.setXAxisMin(0);
    this.renderer.setYAxisMin(0);

    this.renderer.setPanLimits(new double[] { 0, values.size() + RIGHT_PADDING,
        0, highest * TOP_PADDING });
  }

  public GraphicalView getGraphicalView(Context context) {
    return ChartFactory.getBarChartView(context, dataset, renderer,
        Type.DEFAULT);
  }
}
