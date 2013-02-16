package edu.calpoly.codastjegga.cjanalyticsapp.chart.settings;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentProvider;
import android.test.mock.MockContentResolver;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
import edu.calpoly.codastjegga.cjanalyticsapp.utils.DateUtils;

public class ChartSettingsProviderTest extends
    ProviderTestCase2<ChartSettingsProvider> {

  public ChartSettingsProviderTest() {
    super(ChartSettingsProvider.class, ChartSettingsProvider.AUTHORITY);// edu.calpoly.codastjegga.cjanalyticsapp.chart.contentprovider
  }

  ChartSettings testSetting;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    testSetting = new ChartSettings(ChartType.Pie, "DB", "Test", "testMetric",
        null, null);

  }

  public void testQuery() {
    ContentProvider provider = getProvider();
    Cursor cursor = provider.query(ChartSettingsProvider.CONTENT_URI,
        ChartSettingsDB.allColumns, null, null, null);
    assertNotNull(cursor);
  }

  public void testAdd() {

    ContentValues values = ChartSettingsDB.buildQueryValues(testSetting);

    ContentProvider provider = getProvider();
    Uri res = provider.insert(ChartSettingsProvider.CONTENT_URI, values);

    assertNotSame("", res.getLastPathSegment());

    Cursor cursor = provider.query(ChartSettingsProvider.CONTENT_URI,
        ChartSettingsDB.allColumns, null, null, null);
    assertNotNull(cursor);
    assertEquals(1, cursor.getCount());
  }
  
  public void testWhere(){
    ContentValues values = ChartSettingsDB.buildQueryValues(testSetting);

    ContentProvider provider = getProvider();
    Uri res = provider.insert(ChartSettingsProvider.CONTENT_URI, values);

    assertNotSame("", res.getLastPathSegment());

    Cursor cursor = provider.query(ChartSettingsProvider.CONTENT_URI,
        ChartSettingsDB.allColumns, null, null, null);
    assertNotNull(cursor);
    assertEquals(1, cursor.getCount());
    
    
    //New for this one
    cursor = provider.query(ChartSettingsProvider.CONTENT_URI,
        ChartSettingsDB.allColumns, ChartSettingsProvider.DB_EQUALS, new String[]{"hola"}, null);
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
        ChartSettingsDB.allColumns, null, null, null);
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
        ChartSettingsDB.allColumns, null, null, null);
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
        ChartSettingsDB.allColumns, null, null, null);
    assertNotNull(cursor);
    assertEquals(1, cursor.getCount());

    Uri uri = Uri.parse(ChartSettingsProvider.CONTENT_URI + "/" + rowId);
    provider.delete(uri, null, null);

    // Is it gone?
    cursor = provider.query(ChartSettingsProvider.CONTENT_URI,
        ChartSettingsDB.allColumns, null, null, null);
    assertNotNull(cursor);
    assertEquals(0, cursor.getCount());
  }

  
  static Integer helperRowToDelete=3;

  boolean calledUpdate=false;
  boolean calledInsert=false;
  boolean calledDelete=false;
  
  
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
    
    ChartSettingsProvider.delete(mock, helperRowToDelete);
    assertTrue(calledDelete);
    
    
    CursorLoader loader=ChartSettingsProvider.getCursorLoader(getContext(), null);
    assertNull(loader.getSelection());
    assertNull(loader.getSelectionArgs());
    
    loader=ChartSettingsProvider.getCursorLoader(getContext(), "DATA");
    assertEquals(ChartSettingsProvider.DB_EQUALS,loader.getSelection());
    assertEquals(Arrays.asList("DATA"),Arrays.asList(loader.getSelectionArgs()));
    
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
      assertEquals(ChartSettingsDB.buildQueryValues(testSetting), values);
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

  


  public void testDBUpgradefromv1() {
    int dbVersion=-1;
    try {


      Field DATABASE_VERSION = ChartSettingsDB.class
          .getDeclaredField("DATABASE_VERSION");

      DATABASE_VERSION.setAccessible(true);

      dbVersion = DATABASE_VERSION.getInt(null);

      
      //Set it to the initial version and only add things that were there then
      changeDBVersion(1);

      ContentValues values = ChartSettingsDB.buildQueryValues(testSetting);
      values.remove(ChartSettingsDB.FAVORITE);
      
      
      List<String> allButLast=new ArrayList<String>(Arrays.asList(ChartSettingsDB.allColumns));
      allButLast.remove(ChartSettingsDB.allColumns.length-1);
      
      Cursor cursor = addForUpgrade(values,allButLast.toArray(new String[0]), 1);

      //Change to version 2
      changeDBVersion(dbVersion);

      testSetting.setAndroidID(-1);
      
      testSetting.setFavorite(true);
      
      values = ChartSettingsDB.buildQueryValues(testSetting);
      // the orig should still be there
      Cursor cursor2 = addForUpgrade(values,ChartSettingsDB.allColumns, 2);
      
      cursor2.moveToFirst();
      assertEquals("false",cursor2.getString(cursor2.getColumnIndex(ChartSettingsDB.FAVORITE)));
      cursor2.moveToNext();
      assertEquals("true",cursor2.getString(cursor2.getColumnIndex(ChartSettingsDB.FAVORITE)));
      
    } catch (Exception e) {
      e.printStackTrace();
      //So the following don't fail
      if(dbVersion!=-1){
        try {
          changeDBVersion(dbVersion);
        } catch (Exception e2) {
          //DANG
        } 
      }
      throw new RuntimeException(e.getMessage());
    }
  }
  
  private Cursor addForUpgrade(ContentValues values,String[] cols, int expectedCount){


    ContentProvider provider = getProvider();
    Uri res = provider.insert(ChartSettingsProvider.CONTENT_URI, values);

    assertNotSame("", res.getLastPathSegment());

    Cursor cursor = provider.query(ChartSettingsProvider.CONTENT_URI,
        cols, null, null, null);
    
    assertNotNull(cursor);
    assertEquals(expectedCount, cursor.getCount());
    
    return cursor;
  
}
  
  /**Closing the SQLiteOpenHelper (database) in ChartSettingsProvider
   * and updating the versionNumber in the object fores onUpgrade to get called
   * @throws NoSuchFieldException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
  private void changeDBVersion(int newVersion) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
    Field database=getProvider().getClass().getDeclaredField("database");
    database.setAccessible(true);
    
    SQLiteOpenHelper dbObj=(SQLiteOpenHelper) database.get(getProvider());
    dbObj.close();

      // Change the version
    Field version=dbObj.getClass().getSuperclass().getDeclaredField("mNewVersion");
    version.setAccessible(true);
    version.set(dbObj, newVersion);
    
    
    //Change the database create string
    Field createString=dbObj.getClass().getDeclaringClass().getDeclaredField("DATABASE_CREATE");
    createString.setAccessible(true);
    
    Field createField=dbObj.getClass().getDeclaringClass().getDeclaredField("DATABASE_CREATE_V"+newVersion);
    createField.setAccessible(true);
    String newCreate=(String) createField.get(null);
    newCreate+=");";
    
    createString.set(null, newCreate);
    
  }
}