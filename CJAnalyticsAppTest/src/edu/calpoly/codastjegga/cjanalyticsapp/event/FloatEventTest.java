/**
 * 
 */
package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.math.BigDecimal;

import junit.framework.TestCase;

/**
 * Test class for FloatEvent
 * @author Gagandeep S. Kohli
 *
 */
public class FloatEventTest extends TestCase {

	String eventName; String deviceId; String timestamp; String databaseName;
	Float floatV;
	FloatEvent floatEvent;
	protected void setUp() throws Exception {
		super.setUp();
		eventName = "EventTest";
		deviceId = "deviceid09876";
		timestamp = "2013-01-29T08:00:00.000+0000";
		databaseName = "dbName";
		floatV = new Float(33.99);
		floatEvent = new FloatEvent(eventName, deviceId, timestamp, databaseName, floatV);
	}
	
	public void testGetterSetter() {
		assertEquals(floatV, floatEvent.getValue());
		Float newFloatVal = new Float(100.00);
		floatEvent.setFloatValue(newFloatVal);
		assertEquals(newFloatVal, floatEvent.getValue());
		
		floatEvent.setFloatValue(null);
		assertNull(floatEvent.getValue());
   }

}
