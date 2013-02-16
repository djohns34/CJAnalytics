package edu.calpoly.codastjegga.cjanalyticsapp;

import java.util.LinkedList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.calpoly.codastjegga.cjanalyticsapp.dashboard.Dashboard;

public class DashboardsActivity extends ListActivity {

  private ArrayAdapter<Dashboard> adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboards);
    // Show the Up button in the action bar.
    getActionBar().setDisplayHomeAsUpEnabled(true);
    
    //get the list of dashboards
    List<Dashboard> dashboards = getDashboards();
    // Set up the list adapter.
    adapter = new ArrayAdapter<Dashboard>(this, 
        R.layout.activity_dashboard_item,
        dashboards);
   
    setListAdapter(adapter);
  }
  
  private List<Dashboard> getDashboards() {
    //TODO:Implement this method Jeremy : call datafetch.getDashboards...
    // THE REST OF THE CODE IS HARDCODED FOR TESTING
    List<Dashboard> db = new LinkedList<Dashboard> ();
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
  protected void onListItemClick(ListView listView, View view, int position, long id) {
    Dashboard db = (Dashboard)listView.getItemAtPosition(position);
    
    Intent intent = new Intent(this, GraphsActivity.class);
    intent.putExtra(Dashboard.class.toString(), db);
    startActivity(intent);
  }
}
