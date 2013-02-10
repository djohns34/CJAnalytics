package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class CJPieChart implements ChartProvider{
  public GraphicalView getView(Context context) {
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
