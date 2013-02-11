package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;

/**
 * Contains setting information about a chart.
 * @author Daniel 
 * @author Gagandeep rv 8d8a192
 *
 */
public class ChartSettings {

  //chart type this setting renders
  private ChartType chartType;
  //name of the chart, which this setting belongs to
  private String chartName; 
  //list of metrics the chart's event tracks
  private List<String> metrics; 
  //start date
  private Date startDate;
  //end date
  private Date endDate;

  /**
   * Constructs a chart setting with chart type, chart's name, list
   * of metrics, and start and end date. 
   */
  public ChartSettings (ChartType chartType, String chartName, 
      List<String> metrics, Date start, Date end){
    setType(chartType);
    setChartName(chartName);
    this.metrics = new LinkedList<String>();
    addMetrics(metrics);
    setStartDate(start);
    setEndDate(end);
  }

  /**
   * Setter for chart type
   * @param chartType type of chart
   */
  public void setType(ChartType chartType){
    this.chartType = chartType;
  }

  /**
   * Getter for chart type
   * @return chart type this setting is associated with
   */
  public ChartType getType(){
    return chartType;
  }

  /**
   * Setter for chart name
   * @param chartName name of the chart
   */
  public void setChartName(String chartName) {
    this.chartName = chartName;
  }

  /**
   * Getter for chart name
   * @return name of the chart
   */
  public String getChartName () {
    return this.chartName;
  }

  /**
   * adds the list of metrics to the chart setting
   * @param metrics list of metrics (EventNames)
   */
  public void addMetrics (List<String> metrics) {
    if (metrics != null)
      this.metrics.addAll(metrics);
  }

  /**
   * Getter for list of metrics
   * @return list of metrics (EventNames)
   */
  public List<String> getMetrics() {
    return this.metrics;
  }

  /**
   * Setter for start date
   * @param start start date
   */
  public void setStartDate(Date start) {
    this.startDate = start;
  }

  /**
   * getter for start date
   * @return start date
   */
  public Date getStartDate () {
    return this.startDate;
  }

  /**
   * Setter for end date
   * @param end end date
   */
  public void setEndDate(Date end) {
    this.endDate = end;
  }

  /**
   * getter for end date
   * @return end date
   */
  public Date getEndDate () {
    return this.endDate;
  }


  public void save(Intent i) {
    i.putExtra(ChartType.class.getName(), getType());

  }

  public static ChartSettings load(Intent i){
    ChartSettings chartSetting = new ChartSettings(null, null, null, null, null);
    chartSetting.setType((ChartType) i.getExtras().get(ChartType.class.getName()));
    return chartSetting;
  }

}
