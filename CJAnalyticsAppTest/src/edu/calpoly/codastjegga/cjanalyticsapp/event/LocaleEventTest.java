/**
 * 
 */
package edu.calpoly.codastjegga.cjanalyticsapp.event;

import junit.framework.TestCase;

/**
 * Test Class for LocaleEvent
 *  @author Gagandeep S. Kohli
 *
 */
public class LocaleEventTest extends TestCase {

	String eventName; String deviceId; String timestamp; String databaseName;
	String localeV;
	LocaleEvent localeEvent;
	protected void setUp() throws Exception {
		super.setUp();
		eventName = "EventTest";
		deviceId = "deviceid09876";
		timestamp = "2013-01-29T08:00:00.000+0000";
		localeV = "US";
		databaseName = "dbName";
		localeEvent = new LocaleEvent(eventName, deviceId, timestamp, databaseName, localeV);
	}

	public void testGetterSetter() {
		assertEquals(localeV, localeEvent.getValue());
		String newLocaleVal = "EU";
		localeEvent.setLocaleValue(newLocaleVal);
		assertEquals(newLocaleVal, localeEvent.getValue());

		localeEvent.setLocaleValue(null);
		assertNull(localeEvent.getValue());
	}

}
