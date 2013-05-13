package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import java.util.Date;
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
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventSummary;

public class CJLineChart implements ChartProvider {
  private XYMultipleSeriesDataset data;
  private XYMultipleSeriesRenderer ren;

  /**
   * Parse the data from the events
   * 
   * @param chartSettings
   *          Settings for the chart
   * @param events
   *          Events to be parsed
   */
  @Override
  public void parseData(ChartSettings chartSettings, EventSummary... records) {
    Map<Long, Double> values = records[0].getSummarized();
    XYSeriesRenderer xysr = new XYSeriesRenderer();
    TimeSeries timeSeries = new TimeSeries("");

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

    for (Map.Entry<Long, Double> record : values.entrySet()) {
      Date date = new Date(record.getKey());
      timeSeries.add(date, record.getValue());
    }

    data.addSeries(timeSeries);
    ren.addSeriesRenderer(xysr);

    if (records.length == 2) {
      xysr = new XYSeriesRenderer();
      timeSeries = new TimeSeries("");

      values = records[1].getSummarized();
      for (Map.Entry<Long, Double> record : values.entrySet()) {
        Date date = new Date(record.getKey());
        timeSeries.add(date, record.getValue());
      }

      data.addSeries(timeSeries);
      ren.addSeriesRenderer(xysr);
    }
  }

  /**
   * Return the final, graphical view of the chart
   */
  @Override
  public GraphicalView getGraphicalView(Context context) {
    return ChartFactory.getTimeChartView(context, data, ren, "MM/dd/yyyy");
  }
}