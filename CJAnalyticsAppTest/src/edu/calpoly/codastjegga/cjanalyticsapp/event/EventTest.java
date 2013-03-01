package edu.calpoly.codastjegga.cjanalyticsapp.event;

import edu.calpoly.codastjegga.cjanalyticsapp.utils.DateUtils;

import junit.framework.TestCase;

public class EventTest extends TestCase {


<<<<<<< Updated upstream
	String eventName; String deviceId; String timestamp; String databaseName; String value;
	Event event;
=======
	String eventName; String deviceId; String timestamp; String databaseName;
	NumberEvent event;
>>>>>>> Stashed changes
	protected void setUp() throws Exception {
		super.setUp();
		eventName = "EventTest";
		deviceId = "deviceid09876";
		timestamp = "2013-01-29T08:00:00.000+0000";
		databaseName = "dbName";
<<<<<<< Updated upstream
		value = "thisIsAValue";
		event = new Event(eventName, deviceId, timestamp, databaseName, value);
=======
		event = new NumberEvent(eventName, deviceId, timestamp, databaseName, null);
>>>>>>> Stashed changes
	}


	 public void testGetter(){
     assertEquals(deviceId, event.getDeviceId());
     assertEquals(eventName, event.getName());
     String expectedDate;
     expectedDate = DateUtils.parse(timestamp).toString();
     assertEquals(expectedDate, event.getTimestamp().toString());
     assertEquals(value, event.getValue().toString());

}

	public void testSetter() {
		event.setDeviceId("newDivceId");
		assertEquals("newDivceId", event.getDeviceId());

		event.setName("new Event name");
		assertEquals("new Event name", event.getName());
		
		String invalidDate = "123234";
		event.setTimestamp(invalidDate);
		assertNull(event.getTimestamp());
		
		String newDB = "newDB Name";
		event.setDatabaseName(newDB);
		assertEquals(newDB, event.getDatabaseName());
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