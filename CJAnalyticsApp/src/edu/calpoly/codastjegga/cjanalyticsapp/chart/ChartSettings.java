package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import android.content.Intent;
import android.os.Bundle;

public class ChartSettings {
  
  
  private ChartType chartType;
  
  /**
   * Default settings
   */
  public ChartSettings(){
    chartType=ChartType.Pie;
  }
  
  public void setType(ChartType chartType){
    this.chartType=chartType;
  }
  
  public ChartType getType(){
    return chartType;
  }
  
  public void save(Intent i) {
    i.putExtra(ChartType.class.getName(), getType());
    
  }

  public static ChartSettings load(Intent i){
    ChartSettings s=new ChartSettings();
    s.setType((ChartType) i.getExtras().get(ChartType.class.getName()));
    return s;
  }
  
}
