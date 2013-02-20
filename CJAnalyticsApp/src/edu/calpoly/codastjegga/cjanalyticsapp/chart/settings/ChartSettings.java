package edu.calpoly.codastjegga.cjanalyticsapp.chart.settings;

import java.io.Serializable;
import java.util.Date;

import android.content.Intent;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;

/**
 * Contains setting information about a chart.
 * @author Daniel 
 * @author Gagandeep rv 8d8a192
 *
 */
public class ChartSettings implements Serializable {
  
  public static String DATABASE = "DATABASE";
  public static String CHART_NAME = "CHARTNAME";
  public static String START_DATE = "STARTDATE";
  public static String END_DATE = "ENDDATE";
  public static String METRIC = "METRIC";

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
  private Integer androidID;
  
  private Boolean favorite;


  //The settings is not saved to the database
  static final Integer NOT_PERSISTED=-1;
  
  /**
   * Default constructor for chart setting 
   */
  public ChartSettings () {
    setType(ChartType.Pie);
    setChartName("");
    setMetric("");
    setDatabase("");
    setFavorite(false);
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
    setFavorite(false);
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
  public Integer getAndroidID() {
    if(androidID==null){
      return NOT_PERSISTED;
    }else{
      return androidID;
    }
  }

  /**setter for the androidDB id
   * @param id the id corresponding to the Database row id*/
  public void setAndroidID(int id) {
    this.androidID=id;
  }
  
  /**
   * Getter for the favorite variable
   * @return true if this is a user favorite.
   */
  public Boolean getFavorite() {
    return favorite;
  }
  /**
   * Setter for the favorite variable
   * @param favorite the new value.
   */
  public void setFavorite(Boolean favorite) {
    this.favorite = favorite;
  }

  
  
  public void saveToIntent(Intent i) {
    i.putExtra(ChartType.class.getName(), getType());
    i.putExtra(CHART_NAME, getChartName());
    i.putExtra(DATABASE, getDatabase());
    i.putExtra(START_DATE, getStartDate());
    i.putExtra(END_DATE, getEndDate());   
    i.putExtra(METRIC, getMetric());
  }
  
  public static ChartSettings load(Intent i){
    ChartSettings chartSetting = new ChartSettings();
    chartSetting.setType((ChartType) i.getExtras().get(ChartType.class.getName()));
    chartSetting.setChartName((String) i.getExtras().get(CHART_NAME));
    chartSetting.setDatabase((String) i.getExtras().get(DATABASE));
    chartSetting.setStartDate((Date) i.getExtras().get(START_DATE));
    chartSetting.setEndDate((Date) i.getExtras().get(END_DATE));
    chartSetting.setMetric((String)i.getExtras().get(METRIC));
    return chartSetting;
  }



}
