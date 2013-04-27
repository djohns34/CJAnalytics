package edu.calpoly.codastjegga.sdk;

import java.util.Map;

import junit.framework.TestCase;

import android.provider.Settings.Secure;
import android.test.AndroidTestCase;

public class TestSalesforceDBAdapter extends AndroidTestCase{


    static SalesforceDBAdapter db;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        db=new SalesforceDBAdapter(getContext());
        db.open();
    }

    public static void testEmptyList(){
        assertEquals(0,db.fetchAllEvents().size());
        assertEquals(0,db.fetchAllEventsV2().length());
    }

    public static void testInsert(){
        Event<?> e=EventFactory.createEvent(EventType.Text, "Name", "Bob");
        assertNotSame(-1, db.insertEvent(e,"TESTDB"));

    }
    public void testList(){
        Event<?> e=EventFactory.createEvent(EventType.Text, "Name", "Bob");

        assertNotSame(-1, db.insertEvent(e,"TESTDB"));
        Map<Integer, Map<String, Object>> map=db.fetchAllEvents();

        assertEquals(1, map.size());

        Map<String,Object> eventMap= map.values().iterator().next();

        String name= (String) eventMap.get(SalesforceDBAdapter.eventName);
        String value= (String) eventMap.get(e.getEventType().getField());
        String time= (String) eventMap.get(SalesforceDBAdapter.timeStamp);
        String valueType=(String) eventMap.get(SalesforceDBAdapter.valueType);
        String id=(String) eventMap.get(SalesforceDBAdapter.deviceId);

        assertEquals(e.getKey(), name);
        assertEquals(e.getRESTValue(), value);
        assertEquals(e.getTimeStamp(), time);
        assertEquals(e.getEventType().getFieldType(), valueType);

        assertEquals(Secure.getString(getContext().getContentResolver(), Secure.ANDROID_ID), id);
        

        String csvData=db.fetchAllEventsV2();


        assertTrue(csvData.contains(e.getKey()));
        assertTrue(csvData.contains(e.getRESTValue()));
        assertTrue(csvData.contains(e.getTimeStamp()));
        assertTrue(csvData.contains(e.getEventType().getFieldType()));
        assertTrue(csvData.contains(Secure.getString(getContext().getContentResolver(), Secure.ANDROID_ID)));
        

    }
    public void testDelete(){
        Event<?> e=EventFactory.createEvent(EventType.Text, "Name", "Bob");

        assertNotSame(-1, db.insertEvent(e,"TESTDBNAME"));
        Map<Integer, Map<String, Object>> map=db.fetchAllEvents();
        assertEquals(1, map.size());
        
        String[] lines=db.fetchAllEventsV2().split("\n");
        assertEquals(2,lines.length); //header and actaul data

        Integer key= (Integer) map.keySet().toArray()[0];

        assertTrue(db.deleteEvent(key));

        assertEquals(0, db.fetchAllEvents().size());
    }



    @Override
    protected void tearDown() throws Exception{
        super.tearDown();
        db.close();
        boolean delete= getContext().deleteDatabase(SalesforceDBAdapter.DATABASE_NAME);

        assertTrue(delete);
    }

}
