package edu.calpoly.codastjegga.cjanalyticsapp.chart.settings;

import java.io.Serializable;
import java.util.Date;

import android.content.Intent;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.TimeInterval;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventType;

/**
 * Contains setting information about a chart.
 * @author Daniel 
 * @author Gagandeep rv 8d8a192
 *
 */
public class ChartSettings implements Serializable {
  
  public static String DATABASE = "DATABASE";
  public static String USER_NAME = "USERNAME";
  public static String CHART_NAME = "CHARTNAME";
  public static String START_DATE = "STARTDATE";
  public static String END_DATE = "ENDDATE";
  public static String EVENT_NAME = "EVENT_NAME";
  public static String EVENT_NAME2 = "EVENT_NAME2";
  public static String EVENT_TYPE = "EVENT_TYPE";
  public static String EVENT_TYPE2 = "EVENT_TYPE2";
  public static String ANDROID_ID = "ANDROID_ID";
  public static String TIME_INTERVAL = "TIME_INTERVAL";

  //chart type this setting renders
  private ChartType chartType;
  //Database that this chart belongs
  private String database;
  //The user that this chart belongs to
  private String username;
  //name of the chart, which this setting belongs to
  private String chartName; 
  //metrics that this chart displays
  private String eventName; 
  //metrics that this chart displays
  private String eventName2; 
  //start date
  private Date startDate;
  //end date
  private Date endDate;
  //persistance id
  private Integer androidID;
  
  private Boolean favorite;
  
  //type of event associated with the metric
  private EventType eventType;
//type of event associated with the metric
  private EventType eventType2;

  private TimeInterval timeInterval;

  //The settings is not saved to the database
  static final Integer NOT_PERSISTED=-1;
  
  /**
   * Constructor for chart setting, sets default values for fields
   */
  public ChartSettings () {
    setType(ChartType.Pie);
    setChartName("");
    setUsername("");
    setEventName("");
    setDatabase("");
    
    setTimeInterval(TimeInterval.Daily);
    setFavorite(false);
  };
  

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
   * Setter for event type
   * @param eventType the type of event the metric corresponds to
   */
  public void setEventType(EventType eventType){
    this.eventType = eventType;
  }

  /**
   * Setter for event type
   * @param eventType the type of event the metric corresponds to
   */
  public void setEventType2(EventType eventType){
    this.eventType2 = eventType;
  }
  
  /**
   * Setter for event type
   * @return chart type this setting is associated with
   */
  public EventType getEventType(){
    return eventType;
  }
  
  /**
   * Setter for event type
   * @return chart type this setting is associated with
   */
  public EventType getEventType2(){
    return eventType2;
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
  public String getUsername () {
    return this.username;
  }  
  
  /**
   * Setter for user name
   * @param username name of the user
   */
  public void setUsername(String username) {
    this.username = username;
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
   * @param eventName the metric name
   */
  public void setEventName(String eventName) {
    this.eventName=eventName;
  }
  
  /**
   * Setter for chart name
   * @param eventName the metric name
   */
  public void setEventName2(String eventName) {
    this.eventName2=eventName;
  }

  /**
   * Getter for the metrics
   * @return the metric name
   */
  public String getEventName() {
    return this.eventName;
  }

  /**
   * Getter for the metrics
   * @return the metric name
   */
  public String getEventName2() {
    return this.eventName2;
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

  public TimeInterval getTimeInterval() {
    return timeInterval;
  }

  public void setTimeInterval(TimeInterval timeInterval) {
    this.timeInterval = timeInterval;
  }

  public void saveToIntent(Intent i) {
    i.putExtra(ChartType.class.getName(), getType());
    i.putExtra(CHART_NAME, getChartName());
    i.putExtra(USER_NAME, getUsername());
    i.putExtra(DATABASE, getDatabase());
    i.putExtra(START_DATE, getStartDate());
    i.putExtra(END_DATE, getEndDate());   
    i.putExtra(EVENT_NAME, getEventName());
    i.putExtra(EVENT_NAME2, getEventName2());
    i.putExtra(EVENT_TYPE, getEventType());
    i.putExtra(EVENT_TYPE2, getEventType2());
    i.putExtra(ANDROID_ID, getAndroidID());
    i.putExtra(TIME_INTERVAL, getTimeInterval());
  }
  
  public static ChartSettings load(Intent i){
    ChartSettings chartSetting = new ChartSettings();
    chartSetting.setType((ChartType) i.getExtras().get(ChartType.class.getName()));
    chartSetting.setChartName((String) i.getExtras().get(CHART_NAME));
    chartSetting.setUsername((String) i.getExtras().get(USER_NAME));
    chartSetting.setDatabase((String) i.getExtras().get(DATABASE));
    chartSetting.setStartDate((Date) i.getExtras().get(START_DATE));
    chartSetting.setEndDate((Date) i.getExtras().get(END_DATE));
    chartSetting.setEventName((String)i.getExtras().get(EVENT_NAME));
    chartSetting.setEventName2((String)i.getExtras().get(EVENT_NAME2));
    chartSetting.setEventType((EventType)i.getExtras().get(EVENT_TYPE));
    chartSetting.setEventType2((EventType)i.getExtras().get(EVENT_TYPE2));
    chartSetting.setAndroidID(i.getExtras().getInt(ANDROID_ID));
    chartSetting.setTimeInterval((TimeInterval) (i.getExtras().get(TIME_INTERVAL)));
    return chartSetting;
  }
}
