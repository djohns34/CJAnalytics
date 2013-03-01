package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import org.achartengine.renderer.DefaultRenderer;

/**
 * A renderer setting class to append setting to all charts
 * @author gagandeep
 *
 */
class ChartRendererSetting {
  //SIZE of text (legend and labels) ...might depend on the screen
  static float TEXT_SIZE = 24;
  
  /**
   * Appends basic CJ chart renderer setting show that legends of the charts are
   * shown on the screen, and text size is readable.
   * @param ren
   */
  static void appendCustomRendererSetting (DefaultRenderer ren) {
    ren.setFitLegend(true);
    ren.setLabelsTextSize(TEXT_SIZE);
    ren.setLegendTextSize(TEXT_SIZE);
    ren.setInScroll(false);
    ren.setShowLabels(true);
    
  }
}
