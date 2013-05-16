package edu.calpoly.codastjegga.cjanalyticsapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.achartengine.GraphicalView;
import org.achartengine.tools.PanListener;

import com.salesforce.androidsdk.app.ForceApp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartProvider;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.datafetcher.DataFetcher;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventSummary;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Events;

public class ChartActivity extends Activity {
  ChartSettings chartSettings;
  ProgressBar spinner;
  ViewGroup layoutChart;
  ViewGroup activityChart;
  ChartProvider provider;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chart);
    getActionBar().setDisplayHomeAsUpEnabled(true);

    layoutChart = (ViewGroup) findViewById(R.id.chart);
    activityChart =(ViewGroup) findViewById(R.id.activity_chart);
    spinner = (ProgressBar) findViewById(R.id.idProgressBar);

    
    renderSettings(getIntent(),savedInstanceState);

  };
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(ChartProvider.class.getName(), provider);
  }

  /**
   * Parses the chart settings from the intent and executes the render of the chart
   * @param intent
   */
  private void renderSettings(Intent intent,Bundle savedInstanceState) {
    chartSettings = ChartSettings.load(intent);
    setTitle(chartSettings.getChartName());
    
    /*To avoid another render check to see if the provider(which already has everything parsed) exists*/
    if(savedInstanceState!=null && savedInstanceState.containsKey(ChartProvider.class.getName())){
      provider=(ChartProvider) savedInstanceState.getSerializable(ChartProvider.class.getName());
      setGraphicalView();
      
    }else{
      provider = chartSettings.getType().getProvider();
      getRenderTask().execute();
    }
    
  }

  private AsyncTask<Void, Void, Boolean> getRenderTask() {
    return new AsyncTask<Void, Void, Boolean>() {
      @Override
      protected void onPreExecute() {
        super.onPreExecute();
        hideAllViews();
        spinner.setVisibility(View.VISIBLE);
      }

      @Override
      protected Boolean doInBackground(Void... params) {

        if (chartSettings != null) {
          try {
            EventSummary records = DataFetcher.getPrimaryDBRecords(getString(R.string.api_version),
                ((CJAnalyticsApp) getApplication()).getRestClient(),chartSettings);
            if (chartSettings.hasSecondaryMetric()) {
              EventSummary records2 = DataFetcher.getSecondaryDBRecords(getString(R.string.api_version),
                  ((CJAnalyticsApp) getApplication()).getRestClient(),chartSettings);
              provider.parseData(chartSettings, records, records2);
            } else {
              provider.parseData(chartSettings, records);
            }
          } catch (Exception e) {
            Log.e(this.getClass().getName(), "Unable to get/render records", e);
            return false;
          }
        }
        return true;
      }

      @Override
      protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        spinner.setVisibility(View.GONE);
        layoutChart.removeAllViews();
        if (success && chartSettings != null) {
          if(provider.hasData()){
            setGraphicalView();
          }else{
            setNoDataView();
          }
        }else{
          setErrorView();
          Toast.makeText(ChartActivity.this, R.string.chartError, Toast.LENGTH_LONG).show();
          
        }
      }
    };
  }
  
  private void hideAllViews(){
    layoutChart.setVisibility(View.GONE);
    spinner.setVisibility(View.GONE);
    activityChart.findViewById(R.id.chartError).setVisibility(View.GONE);
    activityChart.findViewById(R.id.noData).setVisibility(View.GONE);
    
  }
  
  private void setErrorView(){
    hideAllViews();
    activityChart.findViewById(R.id.chartError).setVisibility(View.VISIBLE);
  }
  private void setNoDataView(){
    hideAllViews();
    activityChart.findViewById(R.id.noData).setVisibility(View.VISIBLE);
    
  }
      
  private void setGraphicalView(){
    hideAllViews();
    layoutChart.setVisibility(View.VISIBLE);
    layoutChart.removeAllViews();
    layoutChart.addView(provider.getGraphicalView(ChartActivity.this));
    
  }

  public void onClickEditButton(MenuItem menu) {
    if (chartSettings != null) {
      final Intent intent = new Intent(this, EditActivity.class);
      chartSettings.saveToIntent(intent);
      startActivityForResult(intent, 0);
      overridePendingTransition(R.anim.slide_in_top, R.anim.none);
    }
  }
  
  public void onClickRefreshButton(MenuItem menu) {
    if (chartSettings != null) {
      // render the chart with the specified chart setting
      getRenderTask().execute();
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
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case android.R.id.home:
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (resultCode) {
    case RESULT_OK:
      renderSettings(data,null);
      break;
    case RESULT_CANCELED:
      break;
    }
  }
  
  //Required in any activity that requires authentication
  @Override
  protected void onPause() {
    super.onPause();
    PasscodeProtected.onPause(this);
  }
  @Override
  protected void onResume() {
    super.onResume();
    PasscodeProtected.onResume(this);
  }
  @Override
  public void onUserInteraction() {
    super.onUserInteraction();
    PasscodeProtected.onUserInteraction();
  }
  //End required sections
  
  public void onLogoutClick(MenuItem menu) {
    ForceApp.APP.logout(this);
  }
  
  @Override
  public void finish() {
    super.finish();
    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
  }
}
