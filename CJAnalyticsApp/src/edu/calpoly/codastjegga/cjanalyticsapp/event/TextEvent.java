package edu.calpoly.codastjegga.cjanalyticsapp.event;

/**
 * A TextEvent corresponds to event whose value type is text
 * @author gagandeep
 *
 */
public class TextEvent extends Event<String> {
  //text value of the event
  private String textVaue;
  
  /**
   * Constructor for TextEvent
   * @param eventName
   *            @see {@link Event} 
   * @param deviceid
   *            @see {@link Event}
   *           
   * @param timestamp @see {@link Event}
   * @param databaseName @see {@link Event}
   * @param textValue value of type text
   */
  public TextEvent (String eventName, String deviceId, String timestamp, String databaseName, String textValue) {
    super(eventName, deviceId, timestamp, databaseName, textValue);
    this.textVaue = textValue;
  }
  
  /**
   * Setter for TextValue
   * @param textValue text value of this event
   */
  public void setValue (String textValue) {
    this.textVaue = textValue;
  }
  
  /**
   * Getter for TextValue
   * @return text value of this event
   */
  public String getValue () {
    return this.textVaue;
  }
}
