package edu.calpoly.codastjegga.cjanalyticsapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.inputmethodservice.Keyboard.Key;
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
 * Custom class to display event name and event type in edit activity spinner
 * (Note there cannot be duplicate metric by the same name in the list)
 * @author gagandeep S. Kohli
 *
 */
public class EventAdapter extends BaseAdapter{

<<<<<<< HEAD
  //List of events
  List<Map.Entry<String, EventType>> eventsList;
=======
  //list of metrics
  ArrayList<String> eventMetrics;
  //list of event types
  ArrayList<EventType> eventTypes;

>>>>>>> b66f3c8594464c70928ef54bf95ac8d1469adbdb
  //context to link to the parent activity
  Context context;

  /**
   * Constructs an EventAdapter
   * @param context see {@link Context}
   */
  public EventAdapter(Context context) {
    this.context = context;
  }

  /**
   * Constructs an EventAdapter with the given event list
   * @param context
   * @param eventsList
   */
<<<<<<< HEAD
  public EventAdapter(Context context, List<Map.Entry<String, EventType>> eventsList){
=======
  public EventAdapter(Context context, ArrayList<String> eventsMetrics, ArrayList<EventType> eventTypes){
>>>>>>> b66f3c8594464c70928ef54bf95ac8d1469adbdb
    this.context = context;
    this.eventMetrics = eventsMetrics;
    this.eventTypes = eventTypes;
  }

  /**
   * Getter for Events List
   * @return list of metrics
   */
  public ArrayList<String> getMetricsList() {
    return eventMetrics;
  }

  /**
   * Getter for Event types List
   * @return list of event types
   */
<<<<<<< HEAD
  public List<Map.Entry<String, EventType>> getEventsList() {
    return eventsList;
=======
  public ArrayList<EventType> getEventTypeList() {
    return eventTypes;
>>>>>>> b66f3c8594464c70928ef54bf95ac8d1469adbdb
  }

  /**
   * Setter for event list and event types
   * @param eventsList list of events 
   * @param eventsTypes list of event types
   */
<<<<<<< HEAD
  public void setEventsList(List<Map.Entry<String, EventType>> eventsList) {
    this.eventsList = eventsList;
=======
  public void setEventsList(ArrayList<String> eventsMetrics, ArrayList<EventType> eventsTypes) {
    this.eventMetrics = eventsMetrics;
    this.eventTypes = eventsTypes;
>>>>>>> b66f3c8594464c70928ef54bf95ac8d1469adbdb
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

    if (view == null)
      view = RelativeLayout.inflate(context, R.layout.activity_edit_charts_event_item, null);

    TextView metricName = (TextView) view.findViewById(R.id.metric_name);
    TextView metricType = (TextView) view.findViewById(R.id.metric_type);
<<<<<<< HEAD
    
    if (eventsList != null) {
     Map.Entry<String, EventType> metric = eventsList.get(position);
    
    metricName.setText(metric.getKey());
    metricType.setText(metric.getValue().toString());
    }
=======


    if (eventMetrics != null && eventTypes != null) {
      metricName.setText(this.eventMetrics.get(position));
      metricType.setText(this.eventTypes.get(position).toString());
    }

>>>>>>> b66f3c8594464c70928ef54bf95ac8d1469adbdb
    return view;
  }
  /**
   * Getter for total number of events in the list
   */
  @Override
  public int getCount() {
<<<<<<< HEAD
    if (eventsList == null)
      return 0;
   return eventsList.size();
=======
    if (eventMetrics == null) return 0; 
    
    return eventMetrics.size();
>>>>>>> b66f3c8594464c70928ef54bf95ac8d1469adbdb
  }

  /**
   * Gets a event from the event list
   * @return event
   */
  @Override
  public Object getItem(int position) {
    if (eventMetrics == null)
      return null;
    return Pair.create(eventMetrics.get(position), eventTypes.get(position));
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
   * Returns the position of an event in the events list or -1 if not
   * found 
   * @param metric metric name
   * @return position of the event
   */
<<<<<<< HEAD
  public int getPosition (Map.Entry<String, EventType> eventsList) {
      return this.eventsList.indexOf(eventsList);
=======
  public int getPosition (String metric) {
    return this.eventMetrics.indexOf(metric);
>>>>>>> b66f3c8594464c70928ef54bf95ac8d1469adbdb
  }
}
