package edu.calpoly.codastjegga.cjanalyticsapp;

import java.util.Random;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettingsProvider;
import edu.calpoly.codastjegga.cjanalyticsapp.dashboard.Dashboard;

public class GraphsActivity extends ListActivity implements LoaderCallbacks<Cursor>{
  
  private ChartSettingsAdapter adapter;
  private Dashboard dashboard;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_graphs);
    
    //initializes the GraphActivity such as dashboard and adapter
  }
  
  private void init() {
    //get the extras from the intent
    Bundle extras = getIntent().getExtras();
    //get the dashboard, it should never be null
    assert(dashboard != null);
    dashboard = (Dashboard) extras.get(Dashboard.class.toString());
    
    // Show the Up button in the action bar.
    getActionBar().setDisplayHomeAsUpEnabled(true);
    
    /*Initialize the loader that will call the database for ChartSettings*/
    getLoaderManager().initLoader(0, null, this);
     
    adapter = new ChartSettingsAdapter(this, null);
    
    setListAdapter(adapter);
    registerForContextMenu(getListView());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_graphs, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    testAdd();
    testAdd();
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
  
  
  //TODO Remove Me!!
  public void testAdd(){
    ChartSettings testSetting=new ChartSettings(ChartType.values()[new Random().nextInt(3)], "DB","NAME", "testMetric", null, null);
    
    /*Insert the test setting*/
    
    ChartSettingsProvider.saveSettings(getContentResolver(), testSetting);
  }

  /*Context Menu*/
  @Override
  public void onCreateContextMenu(ContextMenu menu, View v,
      ContextMenuInfo menuInfo) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.activity_graphs_context, menu);
    super.onCreateContextMenu(menu, v, menuInfo);
    
  }
  @Override
  public boolean onContextItemSelected(MenuItem item) {
    if(item.getItemId()==R.id.context_delete){
      AdapterContextMenuInfo info=(AdapterContextMenuInfo) item.getMenuInfo();
      

      /*delete the row specified by the id*/
      ChartSettingsProvider.delete(getContentResolver(),(int)(info.id));
      
      return true;
    }
    return false;
  };
  /*End Context Menu*/
  


  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    final Intent i = new Intent(this, ChartActivity.class);
    
    ChartSettings s=ChartSettingsProvider.getChartSettings((Cursor) adapter.getItem(position));
    
    s.saveToIntent(i);
    startActivity(i);
  }
  
  
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    //TODO Specify database name
    CursorLoader cursorLoader = ChartSettingsProvider.getCursorLoader(this,null);
    
    return cursorLoader;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    adapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    // data is not available anymore, delete reference
    adapter.swapCursor(null);
    setProgressBarIndeterminateVisibility(false); 
  }
  
  public void onClickChartAdd(View view) {
    Intent intent = new Intent(this, EditActivity.class);
    startActivity(intent);
  }
}

