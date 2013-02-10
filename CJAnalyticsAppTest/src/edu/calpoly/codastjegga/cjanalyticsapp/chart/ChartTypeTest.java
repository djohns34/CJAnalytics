package edu.calpoly.codastjegga.cjanalyticsapp.chart;


import junit.framework.TestCase;


public class ChartTypeTest extends TestCase{
  
  public void testDefaultConstructor(){
    ChartSettings settings=new ChartSettings();
    
  }

  public void testPieChart(){
    assertEquals(CJPieChart.class, ChartType.Pie.getProvider().getClass());
  }
  public void testLineChart(){
    assertEquals(CJPieChart.class, ChartType.Line.getProvider().getClass());
  }
  public void testBarChart(){
    assertEquals(CJBarChart.class, ChartType.Bar.getProvider().getClass());
  }

}
