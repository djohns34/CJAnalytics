package edu.calpoly.codastjegga.sdk;

import org.junit.Test;


import junit.framework.TestCase;

import edu.calpoly.codastjegga.sdk.CurrencyEvent;
import edu.calpoly.codastjegga.sdk.Event;
import edu.calpoly.codastjegga.sdk.EventFactory;
import edu.calpoly.codastjegga.sdk.EventType;
import edu.calpoly.codastjegga.sdk.FloatEvent;
import edu.calpoly.codastjegga.sdk.IntegerEvent;
import edu.calpoly.codastjegga.sdk.LocaleEvent;
import edu.calpoly.codastjegga.sdk.TextEvent;

/**
 * Test cases for Token
 */

public class EventFactoryTest extends TestCase {
    
    
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

        checkResult(CurrencyEvent.class, currencyTestEvent);
        
    }
    
    @Test
    public void testCreateFloatEvent(){
        
        
        Event<?> floatTestEvent=EventFactory.createEvent(EventType.Float, floatKey, floatValue);
        
        checkResult(FloatEvent.class, floatTestEvent);
    }
    
    @Test
    public void testCreateIntEvent(){
        
        Event<?> intTestEvent=EventFactory.createEvent(EventType.Integer, intKey, intValue);
        checkResult(IntegerEvent.class, intTestEvent);
    }
    
    @Test
    public void testCreateLocaleEvent(){
        
        Event<?> localeTestEvent=EventFactory.createEvent(EventType.Locale, localeKey, localeValue);
        checkResult(LocaleEvent.class, localeTestEvent);
    }
    
    @Test
    public void testCreateTextEvent(){
        Event<?> textTestEvent=EventFactory.createEvent(EventType.Text, textKey, textValue);
        checkResult(TextEvent.class, textTestEvent);
    }
    
    @Test
    public void testDifferentKeyType(){
        boolean caught=false;
        try{
            Event<?> badEvent=EventFactory.createEvent(EventType.Integer, textKey, textValue);
        }catch(IllegalArgumentException e){
            caught=true;
        }
        assertTrue("Didn't throw Exception", caught);

    }
    
    
    @Test
    public void testWrongTypeForEvent(){
        boolean caught=false;
        try{
            Event<?> badEvent=EventFactory.createEvent(EventType.Text, textKey, intValue);
        }catch(IllegalArgumentException e){
            caught=true;
        }
        assertTrue("Didn't throw Exception", caught);
        
        
    }
    
    private void checkResult(Class<?> expected,Event<?> result){
        assertEquals(expected, result.getClass());
    }
    
}
