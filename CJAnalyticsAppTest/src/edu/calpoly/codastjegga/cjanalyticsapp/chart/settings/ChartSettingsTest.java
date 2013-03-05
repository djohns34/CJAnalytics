package edu.calpoly.codastjegga.cjanalyticsapp.chart.settings;


import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.TimeInterval;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import android.content.Intent;
import junit.framework.TestCase;


public class ChartSettingsTest extends TestCase{
  ChartSettings settings;
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    settings=new ChartSettings();
  }

  public void testDefaultConstructor(){

    assertEquals(ChartType.Pie, settings.getType());
    assertEquals("", settings.getChartName());
    assertEquals("", settings.getEventName());
    assertEquals("", settings.getDatabase());
    assertEquals(TimeInterval.Daily, settings.getTimeInterval());
    assertEquals(Boolean.FALSE, settings.getFavorite());
  }

  public void testSetGetType(){
    settings.setType(ChartType.Line);
    settings.setChartName("Name");
    settings.setEventName("Event");
    settings.setDatabase("DB");
    settings.setTimeInterval(TimeInterval.Daily);
    settings. setFavorite(true);
    
    assertEquals(ChartType.Line, settings.getType());
    assertEquals("Name", settings.getChartName());
    assertEquals("Event", settings.getEventName());
    assertEquals("DB", settings.getDatabase());
    assertEquals(TimeInterval.Daily, settings.getTimeInterval());
    assertEquals(Boolean.TRUE, settings.getFavorite());

  }

  public void testSave(){
	  settings.setType(ChartType.Line);
	  settings.setChartName("Name");
	  settings.setEventName("Event");
	  settings.setDatabase("DB");
	  settings.setTimeInterval(TimeInterval.Daily);
	  settings. setFavorite(true);

	  Intent i=new Intent();
	  settings.saveToIntent(i);

	  assertEquals(ChartType.Line, (ChartType)i.getExtras().get(ChartType.class.getName()));
	  assertEquals("Name", i.getExtras().get(ChartSettings.CHART_NAME));
	  assertEquals("Event", i.getExtras().get(ChartSettings.EVENT_NAME));
	  assertEquals("DB", i.getExtras().get(ChartSettings.DATABASE));
	  assertEquals(TimeInterval.Daily, i.getExtras().get(ChartSettings.TIME_INTERVAL));
 }

  public void testLoad(){
    Intent i=new Intent();
    settings.saveToIntent(i);
    
    ChartSettings loaded=ChartSettings.load(i);
    
    assertEquals(settings.getType(), loaded.getType());
    
  }
}
