package edu.calpoly.codastjegga.cjanalyticsapp;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.CJPieChart;

public class ChartActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.chart);

    CJPieChart pc = new CJPieChart();
    GraphicalView view = pc.getView(this);

    LinearLayout layout = (LinearLayout)findViewById(R.id.chart);
    layout.addView(view);
  }
}
