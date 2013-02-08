package edu.calpoly.codastjegga.cjanalyticsapp.chart;


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
    settings.save(i);
    
    assertEquals(2, i.getExtras().size());
  }

  public void testLoad(){
    Intent i=new Intent();
    settings.save(i);
    
    ChartSettings loaded=ChartSettings.load(i);
    
    assertEquals(settings.getType(), loaded.getType());
    
  }
}
