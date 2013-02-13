package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.calpoly.codastjegga.cjanalyticsapp.utils.DateUtils;

import junit.framework.TestCase;

public class EventTest extends TestCase {


	String eventName; String deviceId; String timestamp;
	Event event;
	protected void setUp() throws Exception {
		super.setUp();
		eventName = "EventTest";
		deviceId = "deviceid09876";
		timestamp = "2013-01-29T08:00:00.000+0000";
		event = new Event(eventName, deviceId, timestamp);
	}


	public void testGetter(){
		assertEquals(deviceId, event.getDeviceId());
		assertEquals(eventName, event.getEventName());
		String expectedDate;
		expectedDate = DateUtils.parse(timestamp).toString();
		assertEquals(expectedDate, event.getTimestamp().toString());

	}

	public void testSetter() {
		event.setDeviceId("newDivceId");
		assertEquals("newDivceId", event.getDeviceId());

		event.setEventName("new Event name");
		assertEquals("new Event name", event.getEventName());
		
		String invalidDate = "123234";
		event.setTimestamp(invalidDate);
		assertNull(event.getTimestamp());

	}
	
	public void testToString() {
		String eventStr = event.toString();
		
		assertTrue(eventStr.contains(deviceId));
		assertTrue(eventStr.contains(eventName));
		String expectedDate = "";
		expectedDate = DateUtils.parse(timestamp).toString();
		
		assertTrue(eventStr.contains(expectedDate));
		
	}
}