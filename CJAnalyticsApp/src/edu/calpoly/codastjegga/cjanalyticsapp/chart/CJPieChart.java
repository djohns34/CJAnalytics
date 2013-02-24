package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;

import android.content.Context;
import android.graphics.Color;

public class CJPieChart implements ChartProvider{
  public GraphicalView getGraphicalView(Context context, List<Event> events) {
    CategorySeries cs = new CategorySeries("my chart!");
    DefaultRenderer ren = new DefaultRenderer();
    SimpleSeriesRenderer ssr;
    
    cs.add(5);
    ssr = new SimpleSeriesRenderer();
    ssr.setColor(Color.GREEN);
    ren.addSeriesRenderer(ssr);
    
    cs.add(20);
    ssr = new SimpleSeriesRenderer();
    ssr.setColor(Color.BLUE);
    ren.addSeriesRenderer(ssr);
    
    return ChartFactory.getPieChartView(context, cs, ren);
  }
}
