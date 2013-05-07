package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import edu.calpoly.codastjegga.cjanalyticsapp.R;

public enum ChartType {
  Pie(CJPieChart.class,R.drawable.pie_chart, R.id.pie), 
  Bar(CJBarChart.class,R.drawable.bar_chart, R.id.bar), 
  Line(CJLineChart.class,R.drawable.line_chart, R.id.line);


  private Class<? extends ChartProvider> provider;
  private int iconID;
  private int chartId;

  private ChartType(Class<? extends ChartProvider> provider,int iconID, int chartId) {
    this.provider = (Class<? extends ChartProvider>) provider;
    this.iconID=iconID;
    this.chartId = chartId;
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
  
  public static ChartType getTypeById(int id){
    for (ChartType type: ChartType.values()) {
      if(type.chartId == id)
        return type;
    }
    return null;
  }

}