package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import edu.calpoly.codastjegga.cjanalyticsapp.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public enum ChartType {
  Pie(CJPieChart.class,R.drawable.pie_chart), 
  Bar(CJBarChart.class,R.drawable.bar_chart), 
  Line(CJPieChart.class,R.drawable.line_chart);


  private static Resources res;
  
  private Class<? extends ChartProvider> provider;
  private int iconID;
  private Drawable icon;
  
  private ChartType(Class<? extends ChartProvider> provider,int iconID) {
    this.provider = (Class<ChartProvider>) provider;
    this.iconID=iconID;
  }

  public ChartProvider getProvider() {
    ChartProvider p = null;
    try {
      p = provider.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return p;
  }
  
  public Drawable getIcon(){
    if(icon==null){
      icon=res.getDrawable(iconID);
    }
    return icon;
  }

  public static void setResources(Resources resources) {
    res=resources;
  }
}