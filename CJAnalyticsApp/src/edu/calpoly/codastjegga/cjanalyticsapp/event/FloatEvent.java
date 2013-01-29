package edu.calpoly.codastjegga.cjanalyticsapp.event;

/**
 * A FloatEvent corresponds to event whose value type is float
 * 
 * @author gagandeep
 * 
 */
public class FloatEvent extends Event {
	// float value of the event
	private Float floatValue;

	/**
	 * Constructor for FloatEvent
	 * 
	 * @param id
	 *            @see Event id
	 * @param eventName
	 *            @see Event eventName
	 * @param floatValue
	 *            value of type float
	 */
	public FloatEvent(String id, String eventName, Float floatValue) {
		super(id, eventName);
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
