package edu.calpoly.codastjegga.cjanalyticsapp.chart.settings;

import java.util.Arrays;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
        ChartSettingsDB.KEY_ROWID + "= ? ", new String[] { rowId.toString() });

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

}