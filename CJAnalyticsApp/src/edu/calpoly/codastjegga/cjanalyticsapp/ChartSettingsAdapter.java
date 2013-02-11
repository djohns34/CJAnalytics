package edu.calpoly.codastjegga.cjanalyticsapp;

import java.util.List;

import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartSettings;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChartSettingsAdapter extends ArrayAdapter<ChartSettings> {
  String response;
  
//  Context context;
  
  //Initialize adapter
  public ChartSettingsAdapter(Context context, List<ChartSettings> items) {
      super(context, android.R.layout.simple_list_item_1, items);

  }
   
   
  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    View settingsRow=convertView;
      //Get the current settings object
    ChartSettings settings = getItem(position);
       
      if(settingsRow==null)
      {
          LayoutInflater vi;
          vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          settingsRow= vi.inflate(R.layout.activity_graph_item, parent, false);
      }
      
      //Get the text boxes from the listitem.xml file
      TextView graphName =(TextView)settingsRow.findViewById(R.id.graphname);
      TextView graphMetric =(TextView)settingsRow.findViewById(R.id.graphmetric);
      ImageView image=(ImageView) settingsRow.findViewById(R.id.graphImage);
       
      //Assign the appropriate data from our alert object above
      graphName.setText(settings.getChartName());
      //TODO fix this
      graphMetric.setText(settings.getMetrics().toString());
      image.setImageDrawable(settings.getType().getIcon());
       
      return settingsRow;
  }

  
}
