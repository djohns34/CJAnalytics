/**
 * 
 */
package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.math.BigDecimal;

import junit.framework.TestCase;

/**
 * @author Gagandeep S. Kohli
 *
 */
public class NumberEventTest extends TestCase {

	String eventName; String deviceId; String timestamp; String databaseName;
	Integer integerV;
	NumberEvent numEvent;
	protected void setUp() throws Exception {
		super.setUp();
		eventName = "EventTest";
		deviceId = "deviceid09876";
		timestamp = "2013-01-29T08:00:00.000+0000";
		databaseName = "dbName";
		integerV= new Integer(100);
		numEvent = new NumberEvent(eventName, deviceId, timestamp, databaseName, integerV);
	}
	
	public void testGetterSetter() {
		assertEquals(integerV, numEvent.getValue());
		Integer newNumVal = new Integer(-100);
		numEvent.setNumberValue(newNumVal);
		assertEquals(newNumVal, numEvent.getValue());
		
		numEvent.setNumberValue(null);
		assertNull(numEvent.getValue());
	}

}
