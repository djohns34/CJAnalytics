package edu.calpoly.codastjegga.cjanalyticsapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
    return inflater.inflate(R.layout.activity_charts_item, null);
  }


  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    
    ChartSettings settings =ChartSettingsProvider.getChartSettings(cursor);
    
    //Get the text boxes from the listitem.xml file
    TextView chartName =(TextView)view.findViewById(R.id.chartname);
    TextView chartMetric =(TextView)view.findViewById(R.id.chartmetric);
    ImageView image=(ImageView) view.findViewById(R.id.chartImage);

    ImageView favorite=(ImageView) view.findViewById(R.id.favorite);

    
    //Assign the appropriate data from our alert object above
    chartName.setText(settings.getChartName());
    chartMetric.setText(settings.getEventName());
    image.setImageResource(settings.getType().getIcon());
    if(settings.getFavorite()){
      favorite.setImageResource(R.drawable.rating_important);
    }else{
      favorite.setImageResource(R.drawable.rating_not_important);
    }
    
    
    view.setOnTouchListener(new FavoriteToggle(context,settings));
    
  }


  class FavoriteToggle implements OnTouchListener{
    
    ChartSettings settings;
    Context context;
    
    public FavoriteToggle(Context context, ChartSettings settings) {
      this.context=context;
      this.settings=settings;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

      boolean consumed = false;

      //Get the xy position of the star icon
      int xy[] = new int[2];
      v.findViewById(R.id.favorite).getLocationOnScreen(xy);

      //If the touch was to the right of the star, assume the meant to hit it
      if (event.getRawX() >= xy[0]) {
        consumed = true;

        //Only respond to the finger lift, not the press/drag/etc.
        
        if (event.getAction() == MotionEvent.ACTION_UP) {
          //Toggle the favorite 
          settings.setFavorite(!settings.getFavorite());
          //Save it
          ChartSettingsProvider.saveSettings(context.getContentResolver(),
              settings);
        }
      }
      return consumed;
    }


    

  }
  
}
