package edu.calpoly.codastjegga.cjanalyticsapp.event;

public class NumberEvent extends Event {
  //number value of the event
  private Integer numberValue;
  
  /**
   * Constructor for NumberEvent
   * @param eventName
   *            @see {@link Event} 
   * @param deviceid
   *            @see {@link Event}
   *           
   * @param timestamp @see {@link Event}
   * @param numberValue value of type integer (number)
   */
  public NumberEvent (String eventName, String deviceId, String timestamp, Integer numberValue) {
    super(eventName, deviceId, timestamp);
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
