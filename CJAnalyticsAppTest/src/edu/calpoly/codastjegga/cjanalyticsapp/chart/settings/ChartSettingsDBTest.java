package edu.calpoly.codastjegga.cjanalyticsapp.chart.settings;


import android.content.ContentValues;
import android.test.AndroidTestCase;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventType;
import edu.calpoly.codastjegga.cjanalyticsapp.utils.DateUtils;


public class ChartSettingsDBTest extends AndroidTestCase{
  ChartSettings testSetting;
  
  
  protected void setUp() throws Exception {
    super.setUp();
    testSetting=new ChartSettings();
    testSetting.setType(ChartType.Pie);
    testSetting.setDatabase("TestDB");
    testSetting.setChartName("Test Name");
    testSetting.setEventName("testMetric");
    testSetting.setEventType(EventType.Text);
  }

  
  public void testBuildQueryValues(){
    testSetting.setAndroidID(3);
    
    ContentValues values = ChartSettingsDB.buildQueryValues(testSetting);
    
    //To make the test fail if buildQueryValues is updated
    assertEquals(10, values.size());
    
    assertEquals("3", values.get(ChartSettingsDB.KEY_ROWID));
    assertEquals(testSetting.getType().toString(),values.get(ChartSettingsDB.CHART_TYPE));
    assertEquals(testSetting.getChartName(),values.get(ChartSettingsDB.CHART_NAME));
    assertEquals(testSetting.getDatabase(),values.get(ChartSettingsDB.DATABASE));
    assertEquals(testSetting.getEventName(),values.get(ChartSettingsDB.METRIC));
    assertEquals(DateUtils.format(testSetting.getStartDate()),values.get(ChartSettingsDB.START_DATE));
    assertEquals(DateUtils.format(testSetting.getEndDate()),values.get(ChartSettingsDB.END_DATE));
    assertEquals(testSetting.getFavorite(), values.getAsBoolean(ChartSettingsDB.FAVORITE));
    assertEquals(testSetting.getEventType().name(), values.getAsString(ChartSettingsDB.EVENT_TYPE));
    assertEquals(testSetting.getTimeInterval().name(), values.getAsString(ChartSettingsDB.TIME_INTERVAL));
    
  }
}
