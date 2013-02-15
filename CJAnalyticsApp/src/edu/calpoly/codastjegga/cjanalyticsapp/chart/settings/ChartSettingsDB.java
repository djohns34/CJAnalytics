package edu.calpoly.codastjegga.cjanalyticsapp.chart.settings;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
import edu.calpoly.codastjegga.cjanalyticsapp.utils.DateUtils;

/**
 * Simple database access helper class. Allows persistent storage of ChartSettings objects
 * Defines operations to add, list, update, and remove ChartSettings.
 * 
 */
public class ChartSettingsDB {


  /*Row names*/
  static final String KEY_ROWID = BaseColumns._ID;
  static final  String chartType ="ChartType";
  static final  String chartName="ChartName"; 
  static final  String database="database";
  static final  String metric="metric"; 
  static final  String startDate="startDate";
  static final  String endDate="endDate";

  static final String[] allColumns={KEY_ROWID,chartType,chartName,database,metric,startDate,endDate};

  /*Default visibility for test cases*/
  static final String DATABASE_NAME = "CJAnalytics";

  static final String DATABASE_TABLE = "ChartSetttings";

  /**
   * Database creation sql statement
   */
  private static final String DATABASE_CREATE =
      "create table "+DATABASE_TABLE+" ("+KEY_ROWID+" integer primary key autoincrement, "
          + chartType+" text not null,"
          + chartName+" text not null,"
          + database+" text not null,"
          + metric+" text not null,"
          + startDate+" text,"
          + endDate+" text);";


  private static final int DATABASE_VERSION = 1;


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
      /*Not implemented, for now*/
    }
  }

  
  /**
   * Converts the items in the cursor at the current positon into a chart settings object
   * @param c
   * @return
   */
  static ChartSettings getChartSettings(Cursor c) {
    ChartSettings settings=new ChartSettings();
    settings.setAndroidID(c.getInt(c.getColumnIndex(KEY_ROWID)));
    settings.setType(ChartType.valueOf(c.getString(c.getColumnIndex(chartType))));
    settings.setChartName(c.getString(c.getColumnIndex(chartName)));
    settings.setDatabase(c.getString(c.getColumnIndex(database)));
    settings.setMetric(c.getString(c.getColumnIndex(metric)));
    settings.setStartDate(DateUtils.parse(c.getString(c.getColumnIndex(startDate))));
    settings.setEndDate(DateUtils.parse(c.getString(c.getColumnIndex(endDate))));
    return settings;
  }
  
  /**
   * Converts the chartsettings object into something that can be added to a db
   * @param c
   * @return
   */
  static ContentValues buildQuerryValues(ChartSettings setting){
    ContentValues values=new ContentValues();

    values.put(KEY_ROWID, setting.getAndroidID().toString());
    values.put(chartType, setting.getType().toString());
    values.put(chartName, setting.getChartName());
    values.put(database, setting.getDatabase());
    values.put(metric, setting.getMetric());
    values.put(startDate, DateUtils.format(setting.getStartDate()));
    values.put(endDate, DateUtils.format(setting.getEndDate()));
    return values;
  }
}