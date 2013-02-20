package edu.calpoly.codastjegga.cjanalyticsapp;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettingsProvider;


/**
 * A parent class of stored/recent/favorite graphs that handles the list of the activity, and common components such as the context menu
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
    final Intent intent = new Intent(this, ChartActivity.class);
    startActivityWithSettings(intent, position);
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
    boolean handled=false;
    AdapterContextMenuInfo info=(AdapterContextMenuInfo) item.getMenuInfo();   
    if(item.getItemId()==R.id.context_delete){
      /*delete the row specified by the id*/
      ChartSettingsProvider.delete(getContentResolver(),(int)(info.id));
      
      handled= true;
    }else if(item.getItemId()==R.id.context_edit){
      Intent intent = new Intent(this, EditActivity.class);
      startActivityWithSettings(intent, info.position);
      handled=true;
    }
    return handled;
  };
  
  /**
   * Starts the activity specified by the intent after adding the settings at the position specified to the intent
   * @param intent the intent to start the activity
   * @param position the position of the settings in the list
   */
  
  private void startActivityWithSettings(Intent intent,int position){
    ChartSettings settings=ChartSettingsProvider.getChartSettings((Cursor) adapter.getItem(position));
    settings.saveToIntent(intent);
    startActivity(intent);
  }
}

