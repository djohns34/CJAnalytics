package edu.calpoly.codastjegga.cjanalyticsapp.event;

/**
 * A FloatEvent corresponds to event whose value type is float
 * 
 * @author Gagandeep S. Kohli
 * 
 */
public class FloatEvent extends Event {
  // float value of the event
  private Float floatValue;

  /**
   * Constructor for FloatEvent
   * 
   * @param eventName
   *            @see {@link Event} 
   * @param deviceid
   *            @see {@link Event}
   *           
   * @param timestamp @see {@link Event}
   * @param floatValue
   *            value of type float
   */
  public FloatEvent(String eventName, String deviceId, String timestamp, Float floatValue) {
    super(eventName, deviceId, timestamp);
    this.floatValue = floatValue;
  }

  /**
   * Setter for FloatValue
   * 
   * @param floatValue
   *            float value of this event
   */
  public void setFloatValue(Float floatValue) {
    this.floatValue = floatValue;
  }

  /**
   * Getter for FloatValue
   * 
   * @return float value of this event
   */
  public Float getFloatValue() {
    return this.floatValue;
  }
}
