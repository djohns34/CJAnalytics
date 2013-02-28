package edu.calpoly.codastjegga.cjanalyticsapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartProvider;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.datafetcher.DataFetcher;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Events;

public class ChartActivity extends Activity {
  ChartSettings chartSettings;
  ProgressBar spinner;
  ViewGroup layoutChart;
  AsyncTask<Intent, Void, ChartProvider> task;
  ChartProvider provider;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chart);
    getActionBar().setDisplayHomeAsUpEnabled(true);

    layoutChart = (ViewGroup) findViewById(R.id.chart);

    spinner = new ProgressBar(this);
    spinner.setIndeterminate(true);
    // load in the chart setting info
    chartSettings = ChartSettings.load(getIntent());

    provider = chartSettings.getType().getProvider();

    // render the chart with the specified chart setting
    getRenderTask().execute();
  };

  private AsyncTask<Void, Void, List<Event>> getRenderTask() {
    return new AsyncTask<Void, Void, List<Event>>() {
      @Override
      protected void onPreExecute() {
        super.onPreExecute();

        // update provider in case they switched types
        provider = chartSettings.getType().getProvider();

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

        if (chartSettings != null) {
          try {
            Events records = DataFetcher.getDatabaseRecords(
                getString(R.string.api_version),
                ((CJAnalyticsApp) getApplication()).getRestClient(),
                chartSettings.getDatabase(), chartSettings.getEventName(),
                chartSettings.getEventType());
            events = records.getEvents();
          } catch (Exception e) {
            Log.e(this.getClass().getName(), "Unable to get records", e);
            return null;
          }

          provider.parseData(chartSettings, events);
        }
        return events;
      }

      @Override
      protected void onPostExecute(List<Event> result) {
        super.onPostExecute(result);
        spinner.setVisibility(View.GONE);
        layoutChart.removeAllViews();
        if (result != null && chartSettings != null) {
          layoutChart.addView(provider.getGraphicalView(ChartActivity.this));
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

  public void onClickShareButton(MenuItem menu) {
    boolean shared = false;

    try {
      for (int i = 0; i < layoutChart.getChildCount(); i++) {
        if (layoutChart.getChildAt(i) instanceof GraphicalView) {
          // Get the achartengine view object
          GraphicalView view = (GraphicalView) layoutChart.getChildAt(i);

          // Convert it to a bmp
          Bitmap bmp = view.toBitmap();

          File extStorage = Environment.getExternalStorageDirectory();
          // Save the image to a folder on the sd card
          File cj = new File(extStorage, "CodastJegga");
          cj.mkdir();
          File file = new File(cj, "Chart.png");

          FileOutputStream outStream = new FileOutputStream(file);
          bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
          outStream.flush();
          outStream.close();

          // Get the path of the image
          Uri uri = Uri.parse("file://" + file.getAbsolutePath());

          // Create the intent to share the image
          if (uri != null) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/png");

            share.putExtra(Intent.EXTRA_STREAM, uri);

            shared = true;

            startActivity(Intent.createChooser(share, "Share"));
          }

          break;

        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (!shared) {
      Toast.makeText(this, "Error Sharing Chart", Toast.LENGTH_LONG).show();
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
    switch (resultCode) {
    case RESULT_OK:
      chartSettings = ChartSettings.load(data);
      getRenderTask().execute();
      break;

    case RESULT_CANCELED:
      break;
    }
  }
}