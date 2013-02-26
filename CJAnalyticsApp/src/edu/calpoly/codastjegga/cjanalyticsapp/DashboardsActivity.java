package edu.calpoly.codastjegga.cjanalyticsapp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import edu.calpoly.codastjegga.cjanalyticsapp.dashboard.Dashboard;
import edu.calpoly.codastjegga.cjanalyticsapp.datafetcher.DataFetcher;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventType;

public class DashboardsActivity extends ListActivity {

  private ArrayAdapter<Dashboard> adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboards);
    // Show the Up button in the action bar.
    getActionBar().setDisplayHomeAsUpEnabled(true);
    
    new DashboardTask().execute();
  }

  private List<Dashboard> getDashboards() {
    // TODO:Implement this method Jeremy : call datafetch.getDashboards...
    // THE REST OF THE CODE IS HARDCODED FOR TESTING
    List<Dashboard> db = new LinkedList<Dashboard>();
    db.add(new Dashboard("Temple Run"));

    return db;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_dashboards, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case android.R.id.home:
      // This ID represents the Home or Up button. In the case of this
      // activity, the Up button is shown. Use NavUtils to allow users
      // to navigate up one level in the application structure. For
      // more details, see the Navigation pattern on Android Design:
      //
      // http://developer.android.com/design/patterns/navigation.html#up-vs-back
      //
      NavUtils.navigateUpFromSameTask(this);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onListItemClick(ListView listView, View view, int position,
      long id) {
    Dashboard db = (Dashboard) listView.getItemAtPosition(position);

    Intent intent = new Intent(this, StoredChartsActivity.class);
    intent.putExtra(Dashboard.class.toString(), db);
    startActivity(intent);
  }

  /**
   * A helper method that returns a async task to fetch metrics from Salesforce
   * 
   * @return asyncTask to fetch metrics from Salesforce
   */
  private class DashboardTask extends AsyncTask<Void, Void, List<Dashboard>> {
    private View loading;
    private View list;
    @Override
    protected List<Dashboard> doInBackground(Void... params) {
      CJAnalyticsApp cjAnalyApp = (CJAnalyticsApp) getApplicationContext();
      try {
        return DataFetcher.getDatabasesName(getString(R.string.api_version),
            cjAnalyApp.getRestClient());
      } catch (Exception e) {
        Log.e("Dashboard Activity", "Could not load dashboard names", e);
        // if an error occurs, return empty list
        return null;
      }
    }

    protected void onPreExecute() {
      loading = findViewById(R.id.loading);
      list = findViewById(R.id.view);
      
      loading.setVisibility(View.VISIBLE);
      list.setVisibility(View.GONE);
    }

    @Override
    protected void onPostExecute(List<Dashboard> result) {
      List<Dashboard> dashboards;
      if (result != null) {
        dashboards = new ArrayList<Dashboard>(result);
      } else {
        Toast.makeText(getApplicationContext(), "Unable to load dashboards",
            Toast.LENGTH_SHORT).show();
        dashboards = new ArrayList<Dashboard>();
      }
      adapter = new ArrayAdapter<Dashboard>(DashboardsActivity.this,
          R.layout.activity_dashboard_item, dashboards);
      setListAdapter(adapter);
      
      loading.setVisibility(View.GONE);
      list.setVisibility(View.VISIBLE);
    }
  }
}
