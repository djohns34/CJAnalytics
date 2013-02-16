package edu.calpoly.codastjegga.cjanalyticsapp.chart.settings;


import android.content.ContentValues;
import android.test.AndroidTestCase;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
import edu.calpoly.codastjegga.cjanalyticsapp.utils.DateUtils;


public class ChartSettingsDBTest extends AndroidTestCase{
  ChartSettings testSetting;
  
  
  protected void setUp() throws Exception {
    super.setUp();
    testSetting=new ChartSettings(ChartType.Pie, "Test", "testMetric", null, null, null);
  }

  
  public void testBuildQueryValues(){
    testSetting.setAndroidID(3);
    
    ContentValues values = ChartSettingsDB.buildQueryValues(testSetting);
    
    assertEquals("3", values.get(ChartSettingsDB.KEY_ROWID));
    assertEquals(testSetting.getType().toString(),values.get(ChartSettingsDB.CHART_TYPE));
    assertEquals(testSetting.getChartName(),values.get(ChartSettingsDB.CHART_NAME));
    assertEquals(testSetting.getDatabase(),values.get(ChartSettingsDB.DATABASE));
    assertEquals(testSetting.getMetric(),values.get(ChartSettingsDB.METRIC));
    assertEquals(DateUtils.format(testSetting.getStartDate()),values.get(ChartSettingsDB.START_DATE));
    assertEquals(DateUtils.format(testSetting.getEndDate()),values.get(ChartSettingsDB.END_DATE));
    assertEquals(testSetting.getFavorite(), values.getAsBoolean(ChartSettingsDB.FAVORITE));
    
  }
}
