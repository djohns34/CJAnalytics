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
public class CurrentEventTest extends TestCase {

	String eventName; String deviceId; String timestamp; String databaseName;
	BigDecimal currency;
	CurrencyEvent currEvent;
	protected void setUp() throws Exception {
		super.setUp();
		eventName = "EventTest";
		deviceId = "deviceid09876";
		timestamp = "2013-01-29T08:00:00.000+0000";
		databaseName = "dbName";
		currency = new BigDecimal(33.99);
		currEvent = new CurrencyEvent(eventName, deviceId, timestamp, databaseName, currency);
	}
	
	public void testGetterSetter() {
		assertEquals(currency, currEvent.getCurrencyValue());
		BigDecimal newCurrVal = new BigDecimal("100.00");
		currEvent.setCurrencyValue(newCurrVal);
		assertEquals(newCurrVal, currEvent.getCurrencyValue());
		
		currEvent.setCurrencyValue(null);
		assertNull(currEvent.getCurrencyValue());
	}

}
