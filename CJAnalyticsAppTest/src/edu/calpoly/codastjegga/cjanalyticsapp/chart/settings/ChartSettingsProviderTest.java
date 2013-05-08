package edu.calpoly.codastjegga.cjanalyticsapp.chart.settings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentProvider;
import android.test.mock.MockContentResolver;
import edu.calpoly.codastjegga.cjanalyticsapp.CJAnalyticsApp;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.TimeInterval;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventType;

public class ChartSettingsProviderTest extends
    ProviderTestCase2<ChartSettingsProvider> {

  public ChartSettingsProviderTest() {
    super(ChartSettingsProvider.class, ChartSettingsProvider.AUTHORITY);// edu.calpoly.codastjegga.cjanalyticsapp.chart.contentprovider
  }

  ChartSettings testSetting;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    testSetting=new ChartSettings();
    testSetting.setType(ChartType.Pie);
    testSetting.setDatabase("TestDB");
    testSetting.setChartName("Test Name");
    testSetting.setEventName("testMetric");
    testSetting.setEventType(EventType.Text);
    testSetting.setEventType(EventType.Number);
    testSetting.setEventType2(EventType.None);
    testSetting.setTimeInterval(TimeInterval.Yearly);

  }

  public void testQuery() {
    ContentProvider provider = getProvider();
    Cursor cursor = provider.query(ChartSettingsProvider.CONTENT_URI,
        ChartSettingsDB.allSettingsColumns, null, null, null);
    assertNotNull(cursor);
  }
  


  public void testAdd() {

    ContentValues values = ChartSettingsDB.buildQueryValues(testSetting);

    ContentProvider provider = getProvider();
    Uri res = provider.insert(ChartSettingsProvider.CONTENT_URI, values);

    assertNotSame("", res.getLastPathSegment());

    Cursor cursor = provider.query(ChartSettingsProvider.CONTENT_URI,
        ChartSettingsDB.allSettingsColumns, null, null, null);
    assertNotNull(cursor);
    assertEquals(1, cursor.getCount());
  }
  
  
  
  public void testWhere(){
    ContentValues values = ChartSettingsDB.buildQueryValues(testSetting);

    ContentProvider provider = getProvider();
    Uri res = provider.insert(ChartSettingsProvider.CONTENT_URI, values);

    assertNotSame("", res.getLastPathSegment());

    Cursor cursor = provider.query(ChartSettingsProvider.CONTENT_URI,
        ChartSettingsDB.allSettingsColumns, null, null, null);
    assertNotNull(cursor);
    assertEquals(1, cursor.getCount());
    
    
    //New for this one
    cursor = provider.query(ChartSettingsProvider.CONTENT_URI,
        ChartSettingsDB.allSettingsColumns, ChartSettingsProvider.DB_EQUALS, new String[]{"hola"}, null);
    assertNotNull(cursor);
    assertEquals(0, cursor.getCount());
    
  }
  

  public void testUpdate() {
    ContentValues values = ChartSettingsDB.buildQueryValues(testSetting);

    ContentProvider provider = getProvider();
    Uri res = provider.insert(ChartSettingsProvider.CONTENT_URI, values);

    assertNotSame("", res.getLastPathSegment());
    Integer rowId = Integer.parseInt(res.getLastPathSegment());

    // Is it in there
    Cursor cursor = provider.query(ChartSettingsProvider.CONTENT_URI,
        ChartSettingsDB.allSettingsColumns, null, null, null);
    assertNotNull(cursor);
    assertEquals(1, cursor.getCount());

    // Update
    testSetting.setType(ChartType.Bar);
    values = ChartSettingsDB.buildQueryValues(testSetting);

    int updated = provider.update(ChartSettingsProvider.CONTENT_URI, values,
        ChartSettingsProvider.ROWID_EQUALS, new String[] { rowId.toString() });

    assertEquals(1, updated);

    // updated the one in there not added one
    cursor = provider.query(ChartSettingsProvider.CONTENT_URI,
        ChartSettingsDB.allSettingsColumns, null, null, null);
    assertNotNull(cursor);
    assertEquals(1, cursor.getCount());

  }

  public void testDelete() {
    ContentValues values = ChartSettingsDB.buildQueryValues(testSetting);

    ContentProvider provider = getProvider();
    Uri res = provider.insert(ChartSettingsProvider.CONTENT_URI, values);

    assertNotSame("", res.getLastPathSegment());
    Integer rowId = Integer.parseInt(res.getLastPathSegment());

    // Is it in there
    Cursor cursor = provider.query(ChartSettingsProvider.CONTENT_URI,
        ChartSettingsDB.allSettingsColumns, null, null, null);
    assertNotNull(cursor);
    assertEquals(1, cursor.getCount());

    Uri uri = Uri.parse(ChartSettingsProvider.CONTENT_URI + "/" + rowId);
    provider.delete(uri, null, null);

    // Is it gone?
    cursor = provider.query(ChartSettingsProvider.CONTENT_URI,
        ChartSettingsDB.allSettingsColumns, null, null, null);
    assertNotNull(cursor);
    assertEquals(0, cursor.getCount());
  }

  
  static Integer helperRowToDelete=3;

  boolean calledUpdate=false;
  boolean calledInsert=false;
  boolean calledDelete=false;
  
  boolean updatingTime=false;
  
  
  public void testHelpers() {
    MockContentResolver mock = new MockContentResolver();
    mock.addProvider(ChartSettingsProvider.AUTHORITY,
        new ChartSettingsMockContentProvider());

    ChartSettingsProvider.saveSettings(mock, testSetting);
    assertTrue(calledInsert);
    
    // The helper should have updated the id
    assertNotSame(-1, testSetting.getAndroidID());

    //Should be calling the update method
    ChartSettingsProvider.saveSettings(mock, testSetting);
    assertTrue(calledUpdate);
    
    calledUpdate=false;
    updatingTime=true;
    
    //Should also call the update method
    ChartSettingsProvider.chartViewed(mock,testSetting);
    
    ChartSettingsProvider.delete(mock, helperRowToDelete);
    assertTrue(calledDelete);
  }
   
  public void testCursorLoaders(){
	CJAnalyticsApp mockContext = new CJAnalyticsApp() {
		@Override
		public android.content.Context getApplicationContext() {
			return this;
		};
		@Override
		public String getCurrentUserName() {
			// TODO Auto-generated method stub
			return "test@test.test";
		}
	};
	//Charts selection
	CursorLoader loader=ChartSettingsProvider.getCursorLoader(mockContext, null);
    assertNull(loader.getSelection());
    assertNull(loader.getSelectionArgs());
    
    loader=ChartSettingsProvider.getCursorLoader(mockContext, "DATA");
    assertEquals(ChartSettingsProvider.DB_EQUALS
    		+ ChartSettingsProvider.AND
    		+ ChartSettingsProvider.USER_EQUALS, loader.getSelection());
    assertEquals(Arrays.asList("DATA","test@test.test"),Arrays.asList(loader.getSelectionArgs()));
    
    
    //Favorites
    loader=ChartSettingsProvider.getFavoriteCursorLoader(mockContext);
    assertEquals(ChartSettingsProvider.FAVORITE_EQUALS 
    		+ ChartSettingsProvider.AND
    		+ ChartSettingsProvider.USER_EQUALS
    		,loader.getSelection());
    assertEquals(Arrays.asList(Boolean.TRUE.toString(), "test@test.test"),Arrays.asList(loader.getSelectionArgs()));
    
    //Recent
    loader=ChartSettingsProvider.getRecentCursorLoader(mockContext,5);
    assertEquals(ChartSettingsDB.LAST_VIEWED+" "+ChartSettingsDB.DESC, loader.getSortOrder());
    assertEquals(ChartSettingsDB.LAST_VIEWED_NOT_NULL
    		+ ChartSettingsProvider.AND
    		+ ChartSettingsProvider.USER_EQUALS, loader.getSelection());
    assertEquals(Arrays.asList("test@test.test"),Arrays.asList(loader.getSelectionArgs()));
    assertEquals("5", loader.getUri().getQueryParameter(ChartSettingsDB.LAST_VIEWED));
        
  }
  
  public void testLimitQuery(){
	    ContentValues values = ChartSettingsDB.buildQueryValues(testSetting);

	    ContentProvider provider = getProvider();
	    Uri res = provider.insert(ChartSettingsProvider.CONTENT_URI, values);
	    res = provider.insert(ChartSettingsProvider.CONTENT_URI, values);
	    res = provider.insert(ChartSettingsProvider.CONTENT_URI, values);
	    res = provider.insert(ChartSettingsProvider.CONTENT_URI, values);
	    res = provider.insert(ChartSettingsProvider.CONTENT_URI, values);
	    

	    Cursor cursor = provider.query(ChartSettingsProvider.CONTENT_URI,
	        ChartSettingsDB.allSettingsColumns, null, null, null);
	    assertEquals(5, cursor.getCount());
	    
	    
	    Uri limited=ChartSettingsProvider.CONTENT_URI.buildUpon().appendQueryParameter(ChartSettingsDB.LAST_VIEWED, "2").build();
	    cursor = provider.query(limited,ChartSettingsDB.allSettingsColumns, null, null, null);
	  
	    assertEquals(2, cursor.getCount());
  }

  class ChartSettingsMockContentProvider extends MockContentProvider {

    public Uri insert(Uri uri, ContentValues values) {
      assertEquals(ChartSettingsProvider.CONTENT_URI, uri);
      

      ContentValues expected=ChartSettingsDB.buildQueryValues(testSetting);
      expected.remove(ChartSettingsDB.KEY_ROWID);
      assertEquals(expected, values);
      calledInsert=true;
      return Uri.parse(ChartSettingsProvider.BASE_PATH + "/1");
    }

    public int update(Uri uri, ContentValues values, String selection,
        String[] selectionArgs) {
      assertEquals(ChartSettingsProvider.CONTENT_URI, uri);
      
      //Depends on the type of update
      if(!updatingTime){
    	  assertEquals(ChartSettingsDB.buildQueryValues(testSetting), values);
      }else{
    	  //only the timestamp
    	  assertEquals(1,values.size());
    	  //Give a 30 second window
    	  if(new Date().getTime()-values.getAsInteger(ChartSettingsDB.LAST_VIEWED)<30){
    		  fail("last viewed time for update is incorrect");
    	  }

      }
      
      assertEquals(selection, ChartSettingsDB.KEY_ROWID + "= ? ");

      assertEquals(Arrays.asList(testSetting.getAndroidID().toString()),
    			  Arrays.asList(selectionArgs));
  
      calledUpdate=true;
      return 1;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
      assertEquals(ChartSettingsProvider.CONTENT_URI, uri);
      assertEquals(selection, ChartSettingsDB.KEY_ROWID + "= ? ");
      assertEquals(Arrays.asList(helperRowToDelete.toString()),
          Arrays.asList(selectionArgs));
      
      calledDelete=true;
      return 1;
    }

  }
}