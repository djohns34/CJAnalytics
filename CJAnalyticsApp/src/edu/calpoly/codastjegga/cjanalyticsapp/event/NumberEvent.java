package edu.calpoly.codastjegga.cjanalyticsapp.event;

public class NumberEvent extends Event {
  //number value of the event
  private Integer numberValue;
  
  /**
   * Constructor for NumberEvent
   * @param id @see Event id
   * @param eventName @see Event eventName
   * @param numberValue value of type integer (number)
   */
  public NumberEvent (String id, String eventName, Integer numberValue) {
    super (id, eventName);
    this.numberValue = numberValue;
  }
  
  /**
   * Setter for NumberValue
   * @param numberValue Integer (Number) value of this event
   */
  public void setNumbertValue (Integer numberValue) {
    this.numberValue = numberValue;
  }
  
  /**
   * Getter for NumberValue
   * @return integer (number) value of this event
   */
  public Integer getNumberValue () {
    return this.numberValue;
  }
}
