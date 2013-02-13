package edu.calpoly.codastjegga.cjanalyticsapp.chart.settings;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;



/**
 * My goal for this class was to be the interface by which the application accessed/modified the persisted chart settings.
 * The rest of the app doesn't know that these are stored in a database
 * 
 */
public class ChartSettingsProvider extends ContentProvider {

  // database
  private SQLiteOpenHelper database;

  static final String AUTHORITY = "edu.calpoly.codastjegga.cjanalyticsapp.chart.contentprovider";

  static final String BASE_PATH = "settings";

  static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
      + "/" + BASE_PATH);

  @Override
  public boolean onCreate() {
    database = new ChartSettingsDB.DatabaseHelper(getContext());
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    // Uisng SQLiteQueryBuilder instead of query() method
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    // Set the table
    queryBuilder.setTables(ChartSettingsDB.DATABASE_TABLE);

    SQLiteDatabase db = database.getWritableDatabase();
    Cursor cursor = queryBuilder.query(db, ChartSettingsDB.allColumns, selection,
        selectionArgs, null, null, sortOrder);

    // Make sure that potential listeners are getting notified
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    return cursor;
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    long id = 0;

    id = sqlDB.insert(ChartSettingsDB.DATABASE_TABLE, null, values);

    if(id>-1){
      //Things need to update
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return Uri.parse(BASE_PATH + "/" + id);
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {

    
    SQLiteDatabase sqlDB = database.getWritableDatabase();

    //delete the specified row
    int rowsDeleted = sqlDB.delete(ChartSettingsDB.DATABASE_TABLE,selection, selectionArgs);

    if(rowsDeleted>0){
      //Things need to update
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {

    SQLiteDatabase sqlDB = database.getWritableDatabase();

    int rowsUpdated = sqlDB.update(ChartSettingsDB.DATABASE_TABLE, values,
        selection, selectionArgs);

    getContext().getContentResolver().notifyChange(uri, null);

    return rowsUpdated;
  }
  
  /**
   * Helper method to call the contentprovider insert method, also sets the resulting androidId on the setting object,pass by ref.
   * @param resolver
   * @param id
   */
  public static final Uri insert(ContentResolver resolver,ChartSettings s){
    ContentValues values = ChartSettingsDB.buildQuerryValues(s);
    
    Uri res= resolver.insert(ChartSettingsProvider.CONTENT_URI, values);
    s.setAndroidID(Long.parseLong(res.getLastPathSegment()));
    
    return res;
    
  }
  
  /**
   * Helper method to call the contentprovider delete method
   * @param resolver
   * @param id
   */
  public static final int delete(ContentResolver resolver, Integer id){
    return resolver.delete(CONTENT_URI, ChartSettingsDB.KEY_ROWID+"= ? ", new String[]{id.toString()});
  }
  
  /**
   * Helper method to call the contentprovider update method
   * @param resolver
   * @param id
   */
  public static final int update(ContentResolver resolver,ChartSettings s){
    ContentValues values = ChartSettingsDB.buildQuerryValues(s);
    return resolver.update(ChartSettingsProvider.CONTENT_URI, values,ChartSettingsDB.KEY_ROWID+"= ? ", new String[]{s.getAndroidID().toString()});
  }

  /**
   * Helper method to create a CursorLoader
   * @param activity
   * @return
   */
  public static final CursorLoader getCursorLoader(Context activity) {
    return  new CursorLoader(activity,ChartSettingsProvider.CONTENT_URI, ChartSettingsDB.allColumns, null, null, null);
  }
  
  /**
   * Converts the items in the cursor at the current positon into a chart settings object
   * @param c
   * @return settings
   */
  public static final  ChartSettings getChartSettings(Cursor c) {
    return ChartSettingsDB.getChartSettings(c);
  }
}