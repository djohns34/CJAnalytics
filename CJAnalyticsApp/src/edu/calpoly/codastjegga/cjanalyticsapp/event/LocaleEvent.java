package edu.calpoly.codastjegga.cjanalyticsapp.event;

/**
 * A LocaleEvent corresponds to event whose value type is Locale
 * @author Gagandeep S. Kohli
 *
 */
public class LocaleEvent extends Event<String>{
  //locale value of the event
  private String localeValue;
  
  /**
   * Constructor for LocaleEvent
   * @param eventName
   *            @see {@link Event} 
   * @param deviceid
   *            @see {@link Event}
   *           
   * @param timestamp @see {@link Event}
   * @param databaseName @see {@link Event}
   * @param localeValue value of type locale
   */
  public LocaleEvent (String eventName, String deviceId, String timestamp, String databaseName, String localeValue) {
    super(eventName, deviceId, timestamp, databaseName, localeValue);
    this.localeValue = localeValue;
  }
  
  /**
   * Setter for LocaleValue
   * @param localeValue locale value of this event
   */
  public void setLocaleValue (String localeValue) {
    this.localeValue = localeValue;
  }
  
  /**
   * Getter for localeValue
   * @return float value of this event
   */
  public String getValue () {
    return this.localeValue;
  }
  
}
