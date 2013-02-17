package edu.calpoly.codastjegga.cjanalyticsapp.chart.settings;


import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
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
  }

  public void testSetGetType(){
    settings.setType(ChartType.Line);
    
    assertEquals(ChartType.Line, settings.getType());

  }

  public void testSave(){
    Intent i=new Intent();
    settings.saveToIntent(i);
    ChartType actual = (ChartType)i.getExtras().get(ChartType.class.getName());
    assertEquals(ChartType.Pie, actual);
  }

  public void testLoad(){
    Intent i=new Intent();
    settings.saveToIntent(i);
    
    ChartSettings loaded=ChartSettings.load(i);
    
    assertEquals(settings.getType(), loaded.getType());
    
  }
}
