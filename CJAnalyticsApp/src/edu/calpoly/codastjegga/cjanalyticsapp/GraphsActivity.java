package edu.calpoly.codastjegga.cjanalyticsapp;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettingsProvider;


/**
 * A parent class of stored/recent/favorite graphs that handles the list of the activity.
 * @author Daniel
 *
 */
public abstract class GraphsActivity extends ListActivity implements LoaderCallbacks<Cursor>{
  
  private ChartSettingsAdapter adapter;

  /**
   * 
   * @return The android layout resource id for this activity 
   */
  abstract int getContentView();
  
  
  /**
   * Performs any subclass specific initialization, ie getting values from the
   * intent that started the activity 
   */
  abstract void init();
  
  /**
   * subclass implementation of {@link #onCreateLoader(int, Bundle)} called
   * after {@link #init()} so anything required by the loader can be gathered
   * there
   * 
   * @return a loader that will load data to be viewed in the subclass list
   */
  abstract Loader<Cursor> createLoader(int id, Bundle args);
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getContentView());
    
    // Show the Up button in the action bar.
    getActionBar().setDisplayHomeAsUpEnabled(true);
    
    //Perform any subclass specific initialization 
    init();
    
    /*Initialize the loader that will call the database for ChartSettings*/
    getLoaderManager().initLoader(0, null, this);     
    adapter = new ChartSettingsAdapter(this, null);
    
    setListAdapter(adapter);
    registerForContextMenu(getListView());
  }
  
  

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    final Intent i = new Intent(this, ChartActivity.class);
    
    ChartSettings s=ChartSettingsProvider.getChartSettings((Cursor) adapter.getItem(position));
    s.saveToIntent(i);
    
    startActivity(i);
  }
  
  
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return createLoader(id, args);
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
}

