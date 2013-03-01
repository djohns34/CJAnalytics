package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;

import android.content.Context;
import android.graphics.Color;

public class CJLineChart implements ChartProvider {
  public void parseData(ChartSettings chartSettings, List<Event> events) {

  }

  public GraphicalView getGraphicalView(Context context) {
    XYMultipleSeriesDataset data = new XYMultipleSeriesDataset();
    XYMultipleSeriesRenderer ren = new XYMultipleSeriesRenderer();
    
    ChartRendererSetting.appendCustomRendererSetting(ren);
    
    XYSeriesRenderer xysr;
    XYSeries s;

    s = new XYSeries("Blah 1");
    s.add(1, 10);
    data.addSeries(s);
    ren.addXTextLabel(1, s.getTitle());

    xysr = new XYSeriesRenderer();
    xysr.setColor(Color.GREEN);
    ren.addSeriesRenderer(xysr);

    s = new XYSeries("Blah 2");
    s.add(2, 15);
    data.addSeries(s);
    ren.addXTextLabel(2, s.getTitle());

    xysr = new XYSeriesRenderer();
    xysr.setColor(Color.BLUE);
    ren.addSeriesRenderer(xysr);

    ren.setXLabels(0);
    ren.setBarSpacing(.5);
    ren.setYAxisMin(0);
    ren.setXAxisMin(0);

    ren.setMarginsColor(Color.argb(0, 255, 255, 255));

    return ChartFactory.getLineChartView(context, data, ren);
  }
}
