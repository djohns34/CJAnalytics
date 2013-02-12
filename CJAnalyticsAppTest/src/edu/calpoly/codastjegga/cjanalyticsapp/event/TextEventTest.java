/**
 * 
 */
package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.math.BigDecimal;

import junit.framework.TestCase;

/**
 * Test class for TextEvent
 * @author Gagandeep S. Kohli
 *
 */
public class TextEventTest extends TestCase {

  String eventName; String deviceId; String timestamp; String databaseName;
  String textV;
  TextEvent textEvent;
  protected void setUp() throws Exception {
    super.setUp();
    eventName = "EventTest";
    deviceId = "deviceid09876";
    timestamp = "2013-01-29T08:00:00.000+0000";
    textV = "Text";
    databaseName = "dbName";
    textEvent = new TextEvent(eventName, deviceId, timestamp, databaseName,  textV);
  }

  public void testGetterSetter() {
    assertEquals(textV, textEvent.getTextValue());
    String newTextVal = "new_text_value";
    textEvent.setTextValue(newTextVal);
    assertEquals(newTextVal, textEvent.getTextValue());

    textEvent.setTextValue(null);
    assertNull(textEvent.getTextValue());
  }

}
