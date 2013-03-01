package edu.calpoly.codastjegga.cjanalyticsapp.event;

/**
 * NumberEvent
 * @author Gagandeep S. Kohli
 *
 */
public class NumberEvent extends Event<Integer> {
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
   * @param database @see {@link Event}
   * @param numberValue value of type integer (number)
   */
  public NumberEvent (String eventName, String deviceId, String timestamp, String databaseName, Integer numberValue) {
    super(eventName, deviceId, timestamp, databaseName, numberValue);
    this.numberValue = numberValue;
  }
  
  /**
   * Setter for NumberValue
   * @param numberValue Integer (Number) value of this event
   */
  public void setNumberValue (Integer numberValue) {
    this.numberValue = numberValue;
  }
  
  /**
   * Getter for NumberValue
   * @return integer (number) value of this event
   */
  public Integer getValue () {
    return this.numberValue;
  }
}
