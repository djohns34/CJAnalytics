package edu.calpoly.codastjegga.cjanalyticsapp;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;

public class GraphsActivity extends ListActivity {
  
  private ArrayAdapter<ChartSettings> adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_graphs);
    // Show the Up button in the action bar.
    getActionBar().setDisplayHomeAsUpEnabled(true);
    
    List<ChartSettings> items=new ArrayList<ChartSettings>();
    int i=0;
    while(i++<20){
      //TODO: fix this
      ChartSettings s=new ChartSettings(null, null, null, null, null);
      if(i%3==0){
        s.setType(ChartType.Bar);
      }else if(i%3==1){
        s.setType(ChartType.Line);
      }else{
        s.setType(ChartType.Pie);
      }
      items.add(s);
    }
    // Set up the list adapter.
    adapter=new ChartSettingsAdapter(this, items);

    setListAdapter(adapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_graphs, menu);
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
  protected void onListItemClick(ListView l, View v, int position, long id) {
    final Intent i = new Intent(this, ChartActivity.class);
    AlertDialog dialog;

    final ArrayList<String> items=new ArrayList<String>();
    for(ChartType t:ChartType.values()){
      items.add(t.toString());
    }
    
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Which Chart Type?");
    
    builder.setItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int pos) {
        
        //TODO: fix this
        ChartSettings s=new ChartSettings(null, null, items, null, null);
        s.setType(ChartType.valueOf(items.get(pos)));
        
        s.save(i);
        startActivity(i);
        
      }});
    
    
    dialog=builder.create();
    dialog.show();
  }
  
  
}

