package edu.calpoly.codastjegga.cjanalyticsapp.chart.settings;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventType;
import edu.calpoly.codastjegga.cjanalyticsapp.utils.DateUtils;

/**
 * Simple database access helper class. Allows persistent storage of ChartSettings objects
 * Defines operations to add, list, update, and remove ChartSettings.
 * 
 */
 class ChartSettingsDB {
   
   /*General DB things*/
   public static final String DESC="DESC";


  /*Row names*/
  static final String KEY_ROWID = BaseColumns._ID;
  static final  String CHART_TYPE ="ChartType";
  static final  String CHART_NAME="ChartName"; 
  static final  String DATABASE="database";
  static final  String METRIC="metric"; 
  static final  String START_DATE="startDate";
  static final  String END_DATE="endDate";
  
  /*New Row v2*/
  static final String FAVORITE="favorite";
  
  /*New Row v3*/
  static final String LAST_VIEWED="lastViewed";
  public static final String LAST_VIEWED_NOT_NULL = LAST_VIEWED +" is not null";
  
  /*New Row v4*/
  public static String EVENT_TYPE = "eventType";
  
  
  
  /*Does not include the Last viewed because that is only used in SQL calls*/
  static final String[] allSettingsColumns={KEY_ROWID,CHART_TYPE,CHART_NAME,DATABASE,METRIC,START_DATE,END_DATE,FAVORITE,EVENT_TYPE};

  /*Default visibility for test cases*/
  static final String DATABASE_NAME = "CJAnalytics";

  static final String DATABASE_TABLE = "ChartSetttings";

  /**
   * Database creation sql statement
   */
  
  private static final String DATABASE_CREATE =
      "create table "+DATABASE_TABLE+" ("+KEY_ROWID+" integer primary key autoincrement, "
          + CHART_TYPE+" text not null,"
          + CHART_NAME+" text not null,"
          + EVENT_TYPE+ " text not null,"
          + DATABASE+" text not null,"
          + METRIC+" text not null,"
          + START_DATE+" text,"
          + END_DATE+" text,"
          + FAVORITE+ " text not null,"
          + LAST_VIEWED + " INTEGER)";
  
  
  /*Any databases before version 4 didn't hold all of the information required, I decided to wipe it all out and start clean*/
  private static final int DATABASE_VERSION = 4;




  static class DatabaseHelper extends SQLiteOpenHelper {

    DatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

      db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
      db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
      onCreate(db);
    }
  }

  
  /**
   * Converts the items in the cursor at the current position into a chart settings object
   * @param cursor the cursor that contains the settings values
   * @return an equivalent {@link ChartSettings} object 
   */
  static ChartSettings getChartSettings(Cursor cursor) {
    ChartSettings settings=new ChartSettings();
    settings.setAndroidID(cursor.getInt(cursor.getColumnIndex(KEY_ROWID)));
    settings.setType(ChartType.valueOf(cursor.getString(cursor.getColumnIndex(CHART_TYPE))));
    settings.setChartName(cursor.getString(cursor.getColumnIndex(CHART_NAME)));
    settings.setDatabase(cursor.getString(cursor.getColumnIndex(DATABASE)));
    settings.setMetric(cursor.getString(cursor.getColumnIndex(METRIC)));
    settings.setStartDate(DateUtils.parse(cursor.getString(cursor.getColumnIndex(START_DATE))));
    settings.setEndDate(DateUtils.parse(cursor.getString(cursor.getColumnIndex(END_DATE))));
    settings.setFavorite(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(FAVORITE))));
    settings.setEventType(EventType.valueOf(cursor.getString(cursor.getColumnIndex(EVENT_TYPE))));
    
    return settings;
  }
  
  /**
   * Converts the {@link ChartSettings} object into something that can be added to a db
   * @param setting the setting to convert
   * @return
   */
  static ContentValues buildQueryValues(ChartSettings setting){
    ContentValues values=new ContentValues();
    if(!setting.getAndroidID().equals(ChartSettings.NOT_PERSISTED)){
      values.put(KEY_ROWID, setting.getAndroidID().toString());
    }
    values.put(CHART_TYPE, setting.getType().toString());
    values.put(CHART_NAME, setting.getChartName());
    values.put(DATABASE, setting.getDatabase());
    values.put(METRIC, setting.getMetric());
    values.put(START_DATE, DateUtils.format(setting.getStartDate()));
    values.put(END_DATE, DateUtils.format(setting.getEndDate()));
    values.put(FAVORITE, setting.getFavorite().toString());
    values.put(EVENT_TYPE, setting.getEventType().name());
    return values;
  }
}