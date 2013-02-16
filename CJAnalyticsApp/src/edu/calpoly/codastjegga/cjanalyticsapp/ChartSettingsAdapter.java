package edu.calpoly.codastjegga.cjanalyticsapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettingsProvider;

public class ChartSettingsAdapter extends CursorAdapter  {
  String response;
  
  private  LayoutInflater inflater;
  
  //Initialize adapter
  public ChartSettingsAdapter(Context context,Cursor c) {
    super(context, c, 0);
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return inflater.inflate(R.layout.activity_graph_item, null);
  }


  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    
    ChartSettings settings =ChartSettingsProvider.getChartSettings(cursor);
    
    //Get the text boxes from the listitem.xml file
    TextView graphName =(TextView)view.findViewById(R.id.graphname);
    TextView graphMetric =(TextView)view.findViewById(R.id.graphmetric);
    ImageView image=(ImageView) view.findViewById(R.id.graphImage);

    //Assign the appropriate data from our alert object above
    graphName.setText(settings.getChartName());
    graphMetric.setText(settings.getMetric());
    image.setImageResource(settings.getType().getIcon());
    
  }

  
}
