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
   * @param id @see Event id
   * @param eventName @see Event eventName
   * @param localeValue value of type locale
   */
  public LocaleEvent (String id, String eventName, String localeValue) {
    super (id, eventName);
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
