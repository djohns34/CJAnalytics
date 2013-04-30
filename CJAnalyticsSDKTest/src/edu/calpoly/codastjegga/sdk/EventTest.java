package edu.calpoly.codastjegga.sdk;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

import junit.framework.TestCase;

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
    
    public void testCreateCurrencyEvent(){
        
        Event<?> currencyTestEvent=EventFactory.createEvent(EventType.Currency, currencyKey, currencyValue);

        checkResult(currencyKey, currencyValue,"25.00",currencyTestEvent);
        
        assertEquals("25.00", currencyTestEvent.getRESTValue());
    }
    
    public void testFloatEvent(){
        
        Event<?> floatTestEvent=EventFactory.createEvent(EventType.Float, floatKey, floatValue);
        
        checkResult(floatKey, floatValue, "35.40",floatTestEvent);
    }
    
    public void testCreateIntEvent(){
        
        Event<?> intTestEvent=EventFactory.createEvent(EventType.Integer, intKey, intValue);
        checkResult(intKey, intValue, "5",intTestEvent);
    }
    
    public void testCreateLocaleEvent(){
        
        Event<?> localeTestEvent=EventFactory.createEvent(EventType.Locale, localeKey, localeValue);
        checkResult(localeKey, localeValue, "EN_US",localeTestEvent);
    }
    
    public void testCreateTextEvent(){
        Event<?> textTestEvent=EventFactory.createEvent(EventType.Text, textKey, textValue);
        checkResult(textKey, textValue, "Test",textTestEvent);
    }
    
    public void testTimestamp() throws ParseException{

    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        TextEvent e=new TextEvent("Test","Test");
        Date d=new Date();
        e.timeStamp=d;
        
        assertEquals(df.format(d), e.getTimeStamp());
        
    }

    
    private void checkResult(String key,Object value,String restVal,Event<?> result){
        assertEquals(key, result.getKey());
        assertEquals(value, result.getValue());
        assertEquals(restVal, result.getRESTValue());
    }
    
}
