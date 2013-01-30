package edu.calpoly.codastjegga.sdk;

import java.util.Map;

import org.junit.Test;

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

    @Test
    public static void testEmptyList(){
        assertEquals(0,db.fetchAllEvents().size());
    }

    public static void testInsert(){
        Event<?> e=EventFactory.createEvent(EventType.Text, "DB", "DBTEST");

        assertNotSame(-1, db.insertEvent(e));

    }
    public void testList(){
        Event<?> e=EventFactory.createEvent(EventType.Text, "DB", "DBTEST");

        assertNotSame(-1, db.insertEvent(e));
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

    }
    public void testDelete(){
        Event<?> e=EventFactory.createEvent(EventType.Text, "DB", "DBTEST");

        assertNotSame(-1, db.insertEvent(e));
        Map<Integer, Map<String, Object>> map=db.fetchAllEvents();

        assertEquals(1, map.size());

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
