package edu.calpoly.codastjegga.cjanalyticsapp.event;

/**
 * A TextEvent corresponds to event whose value type is text
 * @author gagandeep
 *
 */
public class TextEvent extends Event {
  //text value of the event
  private String textVaue;
  
  /**
   * Constructor for TextEvent
   * @param id @see Event id
   * @param eventName @see Event eventName
   * @param textValue value of type text
   */
  public TextEvent (String id, String eventName, String textValue) {
    super (id, eventName);
    this.textVaue = textValue;
  }
  
  /**
   * Setter for TextValue
   * @param textValue text value of this event
   */
  public void setTextValue (String textValue) {
    this.textVaue = textValue;
  }
  
  /**
   * Getter for TextValue
   * @return text value of this event
   */
  public String getTextValue () {
    return this.textVaue;
  }
}
