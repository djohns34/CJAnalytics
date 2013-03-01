/**
 * 
 */
package edu.calpoly.codastjegga.cjanalyticsapp.event;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

/**
 * Integration test for EventFactory.
 * @author Gagandeep S. Kohli
 *
 */
public class EventFactoryTest extends TestCase {

	private static final String EVENT_NAME = "Event Name:";
	private static final String DEVICE_ID = "device----id";
	private static final String TIMESTAMP = "2013-01-29T08:00:00.000+0000";
	private static final String DATABASENAME = "DBNAME";
	//data formatter
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
	

	private static final String CURRV = "12.99";
	private static final String FLOATV = "1.99";
	private static final String LOCALEV = "US";
	private static final String NUMV = "12";
	private static final String TEXTV = "TEXTV";
	private static Date timestamp;
	
	private static final String currEventName = EVENT_NAME + EventFields.CurrencyV;
	private static final String textEventName = EVENT_NAME + EventFields.TextV;
	private static final String localeEventName = EVENT_NAME + EventFields.LocaleV;
	private static final String numberEventName = EVENT_NAME + EventFields.NumberV;
	private static final String floatEventName = EVENT_NAME + EventFields.FloatV;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		try {
		timestamp = df.parse(TIMESTAMP);
		}catch (Exception ex) {
			timestamp = null;
		}
	}

	public void testCreateEvent() {
		CurrencyEvent currEvent = null;
		TextEvent textEvent = null;
		LocaleEvent localeEvent = null;
		NumberEvent numberEvent = null;
		FloatEvent floatEvent = null;
		
		try {
			currEvent = (CurrencyEvent) EventFactory.createEvent(EventType.Currency.toString(), 
					currEventName, DEVICE_ID, TIMESTAMP, DATABASENAME, CURRV);
		}
		catch (Exception exp) 
		{
			fail("Failed to create a valid currency event");
		}
		validateCurrencyEvent(currEvent);
		
		try {
			textEvent = (TextEvent) EventFactory.createEvent(EventType.Text.toString(), 
					textEventName, DEVICE_ID, TIMESTAMP, DATABASENAME, TEXTV);
		}
		catch (Exception exp) 
		{
			fail("Failed to create a valid text event");
		}
		validateTextEvent(textEvent);
		
		try {
			localeEvent = (LocaleEvent) EventFactory.createEvent(EventType.Locale.toString(), 
					localeEventName, DEVICE_ID, TIMESTAMP, DATABASENAME, LOCALEV);
		}
		catch (Exception exp) 
		{
			fail("Failed to create a valid locale event");
		}
		validateLocaleEvent(localeEvent);
		
		try {
			numberEvent = (NumberEvent) EventFactory.createEvent(EventType.Number.toString(), 
					numberEventName, DEVICE_ID, TIMESTAMP, DATABASENAME, NUMV);
		}
		catch (Exception exp) 
		{
			fail("Failed to create a valid number event");
		}
		validateNumberEvent(numberEvent);
		
		try {
			floatEvent = (FloatEvent) EventFactory.createEvent(EventType.Float.toString(), 
					floatEventName, DEVICE_ID, TIMESTAMP, DATABASENAME, FLOATV);
		}
		catch (Exception exp) 
		{
			fail("Failed to create a valid float event");
		}
		validateFloatEvent(floatEvent);
		
		FloatEvent invalidEvent = null;
		try {
			invalidEvent = (FloatEvent) EventFactory.createEvent("invalidType", 
					floatEventName, DEVICE_ID, TIMESTAMP, DATABASENAME, FLOATV);
			fail("Invaid type event was created..EventFactory.createEvent failed to throw an" +
					"exception");
		}
		catch (Exception exp) 
		{
			//passed
		}
		 
	}
	
	
	public void testCreateFloatEvent() {
		FloatEvent floatEvent = null;
		try {
			floatEvent = (FloatEvent) EventFactory.createEvent(EventType.Float.toString(), 
					floatEventName, DEVICE_ID, TIMESTAMP, DATABASENAME, FLOATV);
		}
		catch (Exception exp) 
		{
			fail("Failed to create a valid float event");
		}
		validateFloatEvent(floatEvent);
	}
	
	public void testCreateLocaleEvent(){
		LocaleEvent localeEvent = null;
		try {
			localeEvent = (LocaleEvent) EventFactory.createEvent(EventType.Locale.toString(), 
					localeEventName, DEVICE_ID, TIMESTAMP, DATABASENAME, LOCALEV);
		}
		catch (Exception exp) 
		{
			fail("Failed to create a valid locale event");
		}
		validateLocaleEvent(localeEvent);
	}

	public void testCreateNumberEvent()
	{
		NumberEvent numberEvent = null;
		try {
			numberEvent = (NumberEvent) EventFactory.createEvent(EventType.Number.toString(), 
					numberEventName, DEVICE_ID, TIMESTAMP, DATABASENAME, NUMV);
		}
		catch (Exception exp) 
		{
			fail("Failed to create a valid number event");
		}
		validateNumberEvent(numberEvent);
	}
	
	public void testCreateTextEvent(){
		TextEvent textEvent = null;
		try {
			textEvent = (TextEvent) EventFactory.createEvent(EventType.Text.toString(), 
					textEventName, DEVICE_ID, TIMESTAMP, DATABASENAME, TEXTV);
		}
		catch (Exception exp) 
		{
			fail("Failed to create a valid text event");
		}
		validateTextEvent(textEvent);
	}
	
	public void testCreateCurrencyEvent() {
		CurrencyEvent currEvent = null;
		try {
			currEvent = (CurrencyEvent) EventFactory.createEvent(EventType.Currency.toString(), 
					currEventName, DEVICE_ID, TIMESTAMP, DATABASENAME, CURRV);
		}
		catch (Exception exp) 
		{
			fail("Failed to create a valid currency event");
		}
		validateCurrencyEvent(currEvent);
	}
	
	private void validateCurrencyEvent(CurrencyEvent currEvent) {
		assertNotNull(currEvent);
		assertEquals(currEventName, currEvent.getName());
		assertEquals(DEVICE_ID, currEvent.getDeviceId());
		assertNotNull(currEvent.getTimestamp());
		assertEquals(timestamp, currEvent.getTimestamp());
		assertEquals(new BigDecimal(CURRV), currEvent.getValue());
	}
	
	private void validateTextEvent(TextEvent textEvent) {
		assertNotNull(textEvent);
		assertEquals(textEventName, textEvent.getName());
		assertEquals(DEVICE_ID, textEvent.getDeviceId());
		assertNotNull(textEvent.getTimestamp());
		assertEquals(timestamp, textEvent.getTimestamp());
		assertEquals(TEXTV, textEvent.getValue());
	}
	
	private void validateLocaleEvent(LocaleEvent localeEvent) {
		assertNotNull(localeEvent);
		assertEquals(localeEventName, localeEvent.getName());
		assertEquals(DEVICE_ID, localeEvent.getDeviceId());
		assertNotNull(localeEvent.getTimestamp());
		assertEquals(timestamp, localeEvent.getTimestamp());
		assertEquals(LOCALEV, localeEvent.getValue());
	}
	
	private void validateNumberEvent(NumberEvent numberEvent) {
		assertNotNull(numberEvent);
		assertEquals(numberEventName, numberEvent.getName());
		assertEquals(DEVICE_ID, numberEvent.getDeviceId());
		assertNotNull(numberEvent.getTimestamp());
		assertEquals(timestamp, numberEvent.getTimestamp());
		assertEquals(new Integer(NUMV), numberEvent.getValue());
	}
	
	private void validateFloatEvent(FloatEvent floatEvent) {
		assertNotNull(floatEvent);
		assertEquals(floatEventName, floatEvent.getName());
		assertEquals(DEVICE_ID, floatEvent.getDeviceId());
		assertNotNull(floatEvent.getTimestamp());
		assertEquals(timestamp, floatEvent.getTimestamp());
		assertEquals(new Float(FLOATV), floatEvent.getValue());
	}



}
