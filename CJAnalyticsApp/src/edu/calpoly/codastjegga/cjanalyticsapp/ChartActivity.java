package edu.calpoly.codastjegga.cjanalyticsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartProvider;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartSettings;

public class ChartActivity extends Activity {

  ChartSettings s;
  ProgressBar spinner;
  ViewGroup layoutChart;

  AsyncTask<Intent, Void, ChartProvider> task;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.chart);
    getActionBar().setDisplayHomeAsUpEnabled(true);


    layoutChart = (ViewGroup) findViewById(R.id.chart);

    spinner =new ProgressBar(this);
    spinner.setIndeterminate(true);

    getRenderTask().execute(getIntent());

  };    


  private AsyncTask<Intent, Void, ChartProvider> getRenderTask(){
    return new AsyncTask<Intent, Void, ChartProvider>() {
      @Override
      protected void onPreExecute() {
        super.onPreExecute();

        LayoutParams p=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        layoutChart.removeAllViews();
        layoutChart.addView(spinner,p);
        spinner.setVisibility(View.VISIBLE);
      }

      @Override
      protected ChartProvider doInBackground(Intent... params) {
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        s = ChartSettings.load(params[0]);

        ChartProvider c = s.getType().getProvider();
        return c;
      }

      @Override
      protected void onPostExecute(ChartProvider result) {
        super.onPostExecute(result);
        spinner.setVisibility(View.GONE);
        layoutChart.removeAllViews();

        layoutChart.addView(result.getView(ChartActivity.this));

      }
    };

  }

  public void onEditButtonClick(View v) {
    if(s!=null){
      final Intent i = new Intent(this, EditActivity.class);
      s.save(i);
      startActivityForResult(i, 0);
    }

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode==RESULT_OK){

      getRenderTask().execute(data);

    }

  }



}
