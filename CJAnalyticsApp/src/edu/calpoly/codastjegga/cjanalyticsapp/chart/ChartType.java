package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import edu.calpoly.codastjegga.cjanalyticsapp.R;

public enum ChartType {
  Pie(CJPieChart.class,R.drawable.pie_chart), 
  Bar(CJBarChart.class,R.drawable.bar_chart), 
  Line(CJLineChart.class,R.drawable.line_chart);


  private Class<? extends ChartProvider> provider;
  private int iconID;

  private ChartType(Class<? extends ChartProvider> provider,int iconID) {
    this.provider = (Class<? extends ChartProvider>) provider;
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
  
  public int getIcon(){
    return iconID;
  }

}