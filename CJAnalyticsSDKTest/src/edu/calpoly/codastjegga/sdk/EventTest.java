package edu.calpoly.codastjegga.sdk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Test cases for Token
 */

public class EventTest extends TestCase {
    
    
    String currencyKey="Wage";
    Double currencyValue=25.0;
    
    String floatKey="Height";
    Float floatValue=35.4f;

    String intKey="Rating";
    Integer intValue=5;
    
    String localeKey="Location";
    String localeValue="EN_US";
    
    String textKey="Notes";
    String textValue="Test";
    
    @Test
    public void testCreateCurrencyEvent(){
        
        Event<?> currencyTestEvent=EventFactory.createEvent(EventType.Currency, currencyKey, currencyValue);

        checkResult(currencyKey, currencyValue,"25.00",currencyTestEvent);
        
        assertEquals("25.00", currencyTestEvent.getRESTValue());
    }
    
    @Test
    public void testFloatEvent(){
        
        Event<?> floatTestEvent=EventFactory.createEvent(EventType.Float, floatKey, floatValue);
        
        checkResult(floatKey, floatValue, "35.40",floatTestEvent);
    }
    
    @Test
    public void testCreateIntEvent(){
        
        Event<?> intTestEvent=EventFactory.createEvent(EventType.Integer, intKey, intValue);
        checkResult(intKey, intValue, "5",intTestEvent);
    }
    
    @Test
    public void testCreateLocaleEvent(){
        
        Event<?> localeTestEvent=EventFactory.createEvent(EventType.Locale, localeKey, localeValue);
        checkResult(localeKey, localeValue, "EN_US",localeTestEvent);
    }
    
    @Test
    public void testCreateTextEvent(){
        Event<?> textTestEvent=EventFactory.createEvent(EventType.Text, textKey, textValue);
        checkResult(textKey, textValue, "Test",textTestEvent);
    }
    
    @Test
    public void testTimestamp() throws ParseException{

        SimpleDateFormat f=new SimpleDateFormat("MM/dd/yy h:mmaa",Locale.US);
        TextEvent e=new TextEvent("Test","Test");
        e.timeStamp=f.parse("01/01/00 12:00pm");
        
        assertEquals("2000-01-01T12:00:00Z", e.getTimeStamp());
        
    }

    
    private void checkResult(String key,Object value,String restVal,Event<?> result){
        assertEquals(key, result.getKey());
        assertEquals(value, result.getValue());
        assertEquals(restVal, result.getRESTValue());
    }
    
}
