package edu.calpoly.codastjegga.cjanalyticsapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.calpoly.codastjegga.cjanalyticsapp.R;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventType;

/**
 * Adapter for storing/viewing list of events 
 * Custom class to disply event name and event type in edit activity spinner
 * @author gagandeep S. Kohli
 *
 */
public class EventAdapter extends BaseAdapter{

  //List of events
  List<Pair<String, EventType>> eventsList;
  //context to link to the parent activity
  Context context;
  
  /**
   * Constructs an EventAdapter
   * @param context see {@link Context}
   */
  public EventAdapter(Context context) {
    this.context = context;
    eventsList = new ArrayList<Pair<String, EventType>>();
  }
  
  /**
   * Constructs an EventAdapter with the given event list
   * @param context
   * @param eventsList
   */
  public EventAdapter(Context context, List<Pair<String, EventType>> eventsList){
    this.context = context;
    this.eventsList = eventsList;
  }
  
  /**
   * Getter for Events List
   * @return list of events (pair <event name, event type>)
   */
  public List<Pair<String, EventType>> getEventsList() {
    return eventsList;
  }

  /**
   * Setter for event list
   * @param eventsList list of events (pair <event name, event type>)
   */
  public void setEventsList(List<Pair<String, EventType>> eventsList) {
    this.eventsList = eventsList;
  }

  /**
   * Getter for context
   * @return context
   */
  public Context getContext() {
    return context;
  }

  /**
   * Setter for context
   * @param context context
   */
  public void setContext(Context context) {
    this.context = context;
  }

  /**
   * Creates a relative view with event name aligned to the left and event type aligned to the right
   */
  @Override
  public View getView(int position, View view, ViewGroup parent) {
    // TODO Auto-generated method stub
    if (view == null)
        view = RelativeLayout.inflate(context, R.layout.activity_edit_charts_event_item, null);
    
    TextView metricName = (TextView) view.findViewById(R.id.metric_name);
    TextView metricType = (TextView) view.findViewById(R.id.metric_type);
    
    Pair<String, EventType> metric = eventsList.get(position);
    
    metricName.setText(metric.first);
    metricType.setText(metric.second.toString());
    
    return view;
  }
  /**
   * Getter for total number of events in the list
   */
  @Override
  public int getCount() {
   return eventsList.size();
  }
  
  /**
   * Gets a event from the event list
   * @return event
   */
  @Override
  public Object getItem(int position) {
    return eventsList.get(position);
  }
  
  /**
   * Gets item id -- always returns 0 since the class doesn't
   * keep unique id for each event in the list
   */
  @Override
  public long getItemId(int position) {
    // TODO Auto-generated method stub
    return 0;
  }
  
  /**
   * Returns the position of an event in the events list of -1 if not
   * found
   * @param eventsList
   * @return
   */
  public int getPosition (Pair<String, EventType> eventsList) {
      return this.eventsList.indexOf(eventsList);
  }
  
}
