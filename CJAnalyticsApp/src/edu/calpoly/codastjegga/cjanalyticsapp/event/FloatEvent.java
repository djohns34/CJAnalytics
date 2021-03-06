package edu.calpoly.codastjegga.cjanalyticsapp.event;

/**
 * A FloatEvent corresponds to event whose value type is float
 * 
 * @author Gagandeep S. Kohli
 * 
 */
public class FloatEvent extends Event<Float> {
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
   * @param databaseName @see {@link Event}
   * @param floatValue
   *            value of type float
   */
  public FloatEvent(String eventName, String deviceId, String timestamp, String databaseName, Float floatValue) {
    super(eventName, deviceId, timestamp, databaseName, floatValue);
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
  public Float getValue() {
    return this.floatValue;
  }
}
