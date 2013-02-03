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

	String eventName; String deviceId; String timestamp;
	String localeV;
	LocaleEvent localeEvent;
	protected void setUp() throws Exception {
		super.setUp();
		eventName = "EventTest";
		deviceId = "deviceid09876";
		timestamp = "2013-01-29T08:00:00.000+0000";
		localeV = "US";
		localeEvent = new LocaleEvent(eventName, deviceId, timestamp, localeV);
	}

	public void testGetterSetter() {
		assertEquals(localeV, localeEvent.getLocaleValue());
		String newLocaleVal = "EU";
		localeEvent.setLocaleValue(newLocaleVal);
		assertEquals(newLocaleVal, localeEvent.getLocaleValue());

		localeEvent.setLocaleValue(null);
		assertNull(localeEvent.getLocaleValue());
	}

}
