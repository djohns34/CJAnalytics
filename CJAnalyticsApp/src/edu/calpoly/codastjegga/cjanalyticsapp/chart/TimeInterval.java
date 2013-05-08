package edu.calpoly.codastjegga.cjanalyticsapp.chart;

public enum TimeInterval {
  Daily(86400l),
  Monthly(86400*30l),/*Considering monthly as 30 days*/
  Yearly(86400*365l);
  
  private Long seconds;
  
  TimeInterval(Long seconds){
    this.seconds=seconds;
  }
  
  public Long getMilliSeconds(){
    return seconds*1000;
  }

}
