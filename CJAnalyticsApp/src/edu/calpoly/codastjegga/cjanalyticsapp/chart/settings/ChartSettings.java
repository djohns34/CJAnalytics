package edu.calpoly.codastjegga.cjanalyticsapp.chart.settings;

import java.util.Date;

import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;

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
  //Database that this chart belongs
  private String database;
  //name of the chart, which this setting belongs to
  private String chartName; 
  //metrics that this chart displays
  private String metric; 
  //start date
  private Date startDate;
  //end date
  private Date endDate;
  //persistance id
  private Long androidID;

  /**
   * Default constructor for chart setting 
   */
  public ChartSettings () {
    setType(ChartType.Pie);
    setChartName("");
    setMetric("");
    setDatabase("");
  };
  
  /**
   * Constructs a chart setting with chart type, chart's name, list
   * of metrics, and start and end date. 
   */
  public ChartSettings (ChartType chartType, String database, String chartName, 
      String metric, Date start, Date end){
    setType(chartType);
    setDatabase(database);
    setChartName(chartName);
    setMetric(metric);
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
   * Setter for chart name
   * @param metric the metric name
   */
  public void setMetric(String metric) {
    this.metric=metric;
  }

  /**
   * Getter for the metrics
   * @return the metric name
   */
  public String getMetric() {
    return this.metric;
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

  /**
   * getter for the database name
   * @return database name
   */
  public String getDatabase() {
    return database;
  }
  
  /**
   * setter for the database name
   * @param database the name
   */
  public void setDatabase(String database) {
    this.database = database;
  }
  
  /**getter for the AndroidDB id
   * @return the id corresponding to the Database row id*/
  public Long getAndroidID() {
    if(androidID==null){
      return -1l;
    }else{
      return androidID;
    }
  }

  /**setter for the androidDB id
   * @param id the id corresponding to the Database row id*/
  public void setAndroidID(long id) {
    this.androidID=id;
  }
  
  
  
  public void saveToIntent(Intent i) {
    i.putExtra(ChartType.class.getName(), getType());
    
  }
  
  public static ChartSettings load(Intent i){
    ChartSettings chartSetting = new ChartSettings(null, null, null, null, null, null);
    chartSetting.setType((ChartType) i.getExtras().get(ChartType.class.getName()));
    return chartSetting;
  }


}
