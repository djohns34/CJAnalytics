package edu.calpoly.codastjegga.cjanalyticsapp.event;


/**
 * A LocaleEvent corresponds to event whose value type is Locale
 * @author gagandeep
 *
 */
public class LocaleEvent extends Event{
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
   * @param localeValue value of type locale
   */
  public LocaleEvent (String eventName, String deviceId, String timestamp, String localeValue) {
    super(eventName, deviceId, timestamp);
    this.localeValue = localeValue;
  }
  
  /**
   * Setter for LocaleValue
   * @param localeValue locale value of this event
   */
  public void setCurrencyValue (String localeValue) {
    this.localeValue = localeValue;
  }
  
  /**
   * Getter for localeValue
   * @return float value of this event
   */
  public String getLocaleValue () {
    return this.localeValue;
  }
  
}
