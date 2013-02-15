package edu.calpoly.codastjegga.cjanalyticsapp.chart.settings;


import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.utils.DateUtils;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.test.ActivityTestCase;
import android.test.AndroidTestCase;
import android.test.mock.MockCursor;
import junit.framework.TestCase;


public class ChartSettingsDBTest extends AndroidTestCase{
  ChartSettings testSetting;
  
  
  protected void setUp() throws Exception {
    super.setUp();
    testSetting=new ChartSettings(ChartType.Pie, "Test", "testMetric", null, null, null);
  }

  
  public void testBuildQuerryValues(){
    testSetting.setAndroidID(3l);
    
    ContentValues values = ChartSettingsDB.buildQuerryValues(testSetting);
    
    assertEquals("3", values.get(ChartSettingsDB.KEY_ROWID));
    assertEquals(testSetting.getType().toString(),values.get(ChartSettingsDB.chartType));
    assertEquals(testSetting.getChartName(),values.get(ChartSettingsDB.chartName));
    assertEquals(testSetting.getDatabase(),values.get(ChartSettingsDB.database));
    assertEquals(testSetting.getMetric(),values.get(ChartSettingsDB.metric));
    assertEquals(DateUtils.format(testSetting.getStartDate()),values.get(ChartSettingsDB.startDate));
    assertEquals(DateUtils.format(testSetting.getEndDate()),values.get(ChartSettingsDB.endDate));
    
  }
}
