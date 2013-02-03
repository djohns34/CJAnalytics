package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
		try {
			expectedDate = Event.getDataFormater().parse(timestamp).toString();
			assertEquals(expectedDate, event.getTimestamp().toString());
		} catch (ParseException e) {
			fail("Internal error");

		}

	}

	public void testSetter() {
		event.setDeviceId("newDivceId");
		assertEquals("newDivceId", event.getDeviceId());

		event.setEventName("new Event name");
		assertEquals("new Event name", event.getEventName());

		DateFormat dfNew = new SimpleDateFormat("yyyy-mm-dd");
		Event.setDataFormater(dfNew);
		assertEquals(dfNew, Event.getDataFormater());

		String invalidDate = "123234";
		event.setTimestamp(invalidDate);
		assertNull(event.getTimestamp());

	}
	public void testDateSetterGetter() {
		DateFormat dfNew = new SimpleDateFormat("yyyy-mm-dd");
		Event.setDataFormater(dfNew);
		String date = "2013-02-02";
		Date d = null;
		try {
			d = dfNew.parse(date);
		} catch (ParseException e) {
			fail("internal error");
		}

		event.setTimestamp(d);
		assertEquals(d, event.getTimestamp());
	}
	
	public void testToString() {
		String eventStr = event.toString();
		
		assertTrue(eventStr.contains(deviceId));
		assertTrue(eventStr.contains(eventName));
		String expectedDate = "";
		try {
			expectedDate = Event.getDataFormater().parse(timestamp).toString();
		} catch (ParseException e) {
			fail ("internal error");
		}
		
		assertTrue(eventStr.contains(expectedDate));
		
	}
}