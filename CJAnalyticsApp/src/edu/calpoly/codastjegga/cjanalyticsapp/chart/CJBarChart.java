package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;

import android.content.Context;
import android.graphics.Color;

public class CJBarChart implements ChartProvider{
  public GraphicalView getGraphicalView(Context context, List<Event> events) {
    
    
    XYMultipleSeriesDataset data=new XYMultipleSeriesDataset();
    XYMultipleSeriesRenderer ren=new XYMultipleSeriesRenderer();
    
    
    XYSeries s=new XYSeries("Blah 1");
    s.add(1, 10);
    data.addSeries(s);
    ren.addXTextLabel(1, s.getTitle());
    
    SimpleSeriesRenderer ssr = new SimpleSeriesRenderer();
    ssr.setColor(Color.GREEN);
    ren.addSeriesRenderer(ssr);
    
    s=new XYSeries("Blah 2");
    s.add(2, 15);
    data.addSeries(s);
    ren.addXTextLabel(2, s.getTitle());
    
    ssr = new SimpleSeriesRenderer();
    ssr.setColor(Color.BLUE);
    ren.addSeriesRenderer(ssr);
    ren.setXLabels(0);
    ren.setBarSpacing(.5);
    ren.setYAxisMin(0);
    ren.setXAxisMin(0);
    
    ren.setMarginsColor(Color.argb(0, 255, 255, 255));
    
    return ChartFactory.getBarChartView(context, data, ren, Type.DEFAULT);
  }
}
