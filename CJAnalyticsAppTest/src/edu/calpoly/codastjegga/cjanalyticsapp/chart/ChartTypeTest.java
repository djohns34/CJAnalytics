package edu.calpoly.codastjegga.cjanalyticsapp.chart;


import junit.framework.TestCase;


public class ChartTypeTest extends TestCase{
  

  public void testPieChart(){
    assertEquals(CJPieChart.class, ChartType.Pie.getProvider().getClass());
  }
  public void testLineChart(){
    assertEquals(CJLineChart.class, ChartType.Line.getProvider().getClass());
  }
  public void testBarChart(){
    assertEquals(CJBarChart.class, ChartType.Bar.getProvider().getClass());
  }

}
