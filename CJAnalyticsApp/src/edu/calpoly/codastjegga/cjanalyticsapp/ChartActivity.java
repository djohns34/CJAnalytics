package edu.calpoly.codastjegga.cjanalyticsapp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartProvider;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.datafetcher.DataFetcher;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Records;

public class ChartActivity extends Activity {
  ChartSettings chartSettings;
  ProgressBar spinner;
  ViewGroup layoutChart;
  AsyncTask<Intent, Void, ChartProvider> task;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chart);
    getActionBar().setDisplayHomeAsUpEnabled(true);

    layoutChart = (ViewGroup) findViewById(R.id.chart);

    spinner = new ProgressBar(this);
    spinner.setIndeterminate(true);
    //load in the chart setting info
    chartSettings = ChartSettings.load(getIntent());
    //render the chart with the specified chart setting
    getRenderTask().execute();
  };

  private AsyncTask<Void, Void, List<Event>> getRenderTask() {
    return new AsyncTask<Void, Void, List<Event>>() {
      @Override
      protected void onPreExecute() {
        super.onPreExecute();

        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        layoutChart.removeAllViews();
        layoutChart.addView(spinner, p);
        spinner.setVisibility(View.VISIBLE);
      }

      @Override
      protected List<Event> doInBackground(Void... params) {
        List<Event> events = null;

        if(chartSettings != null) {

          try {
            Records records = DataFetcher.getDatabaseRecords(getString(R.string.api_version), 
                ((CJAnalyticsApp)getApplication()).getRestClient() , 
                chartSettings.getDatabase(), chartSettings.getMetric(), 
                chartSettings.getEventType());
            events = records.getEvents();
          } catch (Exception e) {
            Log.e(this.getClass().getName(), "Unable to get records", e);
            return null;
          }
        }
        return events;
      }


      @Override
      protected void onPostExecute(List<Event> result) {
        super.onPostExecute(result);
        spinner.setVisibility(View.GONE);
        layoutChart.removeAllViews();
        if (result != null && chartSettings != null) {
          ChartProvider provider = chartSettings.getType().getProvider();
          layoutChart.addView(provider.getGraphicalView(ChartActivity.this, result));
        }
      }
    };
  }



  public void onClickEditButton(MenuItem menu) {
    if (chartSettings != null) {
      final Intent intent = new Intent(this, EditActivity.class);
      chartSettings.saveToIntent(intent);
      startActivityForResult(intent, 0);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_chart, menu);
    return true;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    chartSettings = ChartSettings.load(data);
    if (resultCode == RESULT_OK) {
      getRenderTask().execute();
    }
  }
}