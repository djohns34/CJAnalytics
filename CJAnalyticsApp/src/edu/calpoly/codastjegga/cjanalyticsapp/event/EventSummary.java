package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.util.Map;

public class EventSummary {

  private Map<String, Double> summarized;
  Map<String, Integer> categorical;

  public Map<String, Double> getSummarized() {
    return summarized;
  }
  
  public Map<String, Integer> getCategorical() {
    return categorical;
  }

}