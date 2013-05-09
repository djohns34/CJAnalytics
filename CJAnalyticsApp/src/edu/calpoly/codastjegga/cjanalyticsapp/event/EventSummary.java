package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.util.Map;

public class EventSummary {

  private Map<Long, Double> summarized;
  Map<String, Integer> categorical;

  public Map<Long, Double> getSummarized() {
    return summarized;
  }
  
  public Map<String, Integer> getCategorical() {
    return categorical;
  }

}