package edu.calpoly.codastjegga.cjanalyticsapp.chart;
public enum ChartType {
  Pie(CJPieChart.class), 
  Bar(CJBarChart.class), 
  Line(CJPieChart.class);

  private Class<? extends ChartProvider> provider;

  private ChartType(Class<? extends ChartProvider> provider) {
    this.provider = (Class<ChartProvider>) provider;
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
}