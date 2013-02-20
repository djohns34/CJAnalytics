package edu.calpoly.codastjegga.cjanalyticsapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettingsProvider;
import edu.calpoly.codastjegga.cjanalyticsapp.datafetcher.DataFetcher;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventType;

public class EditActivity extends FragmentActivity {

  private static final String DAY = "day";
  private static final String MONTH = "month";
  private static final String YEAR = "year";
  private static final String SAVED_METRICS_LIST = "savedmetricslist";
  private static final String SAVED_TO_DATE = "savedtodate";
  private static final String SAVED_FROM_DATE = "savedfromdate";
  
  private static final String DATE_PICKER_TITLE = "Date Picker";

  private static final DateFormat dateFormater = new SimpleDateFormat("MM-dd-yyyy");

  private static final String LOADING_METRICS = "Loading Metrics...";
  private ToggleButton lineButton;
  private ToggleButton barButton;
  private ToggleButton pieButton;
  private ChartSettings chartSettings;
  private EditText chartName;
  private Spinner metricsSPinner;
  private TextView toDateText, fromDateText;
  private Calendar toDate, fromDate;
  private Calendar prevToDate, prevFromDate;
  private LinearLayout editChartLayout;
  private ProgressDialog progressBar;
  private ArrayAdapter<String> metricsAdapter;
  private ArrayList<String> metricsList;
  private DatePickerFragment datePickerFrag;
  private int DIALOG_ID = 1;

  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    
    Intent intent = this.getIntent();
    setContentView(R.layout.editchart);
    

    lineButton = (ToggleButton) this.findViewById(R.id.line);
    barButton = (ToggleButton) this.findViewById(R.id.bar);
    pieButton = (ToggleButton) this.findViewById(R.id.pie);
    chartName = (EditText) this.findViewById(R.id.graphName);
    metricsSPinner = (Spinner) this.findViewById(R.id.metricsList);
    toDateText = (TextView) this.findViewById(R.id.toDate);
    fromDateText = (TextView) this.findViewById(R.id.fromDate);
    editChartLayout = (LinearLayout) this.findViewById(R.id.editChartParentLayout);
    progressBar = new ProgressDialog(this);

    //create new metrics adapter
    metricsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
    metricsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //set up the adapter to metrics list
    metricsSPinner.setAdapter(metricsAdapter);

    // This class should always contain chartSetting in its intent
    // If the intent contains chart data, load it, otherwise, create a new chart
    if (intent.hasExtra(ChartType.class.getName())) {
      chartSettings = ChartSettings.load(intent);
    } else {
      chartSettings = new ChartSettings();
    }
    if (savedInstanceState != null) {
      metricsList = savedInstanceState.getStringArrayList(SAVED_METRICS_LIST);
      toDate = (Calendar) savedInstanceState.get(SAVED_TO_DATE);
      fromDate = (Calendar) savedInstanceState.get(SAVED_FROM_DATE);
      metricsAdapter.addAll(metricsList);
      metricsSPinner.setAdapter(metricsAdapter);
      selectMetricInSpinner();
    } else {      
      initToggle();
      initMetricList();
      initChartSettingInView();
    }
    updateDateView();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    // TODO Auto-generated method stub
    super.onSaveInstanceState(outState);
    outState.putStringArrayList(SAVED_METRICS_LIST, metricsList);
    outState.putSerializable(SAVED_TO_DATE, toDate);
    outState.putSerializable(SAVED_FROM_DATE, fromDate);
  }

  private void initToggle() {
    uncheckAllToggleSwitches();
    ChartType chartType = chartSettings.getType();

    switch (chartType) {
    case Bar:
      barButton.toggle();
      break;
    case Line:
      lineButton.toggle();
      break;
    case Pie:
      pieButton.toggle();
      break;
    default:
      break;

    }
  }


  private void initChartSettingInView() {
    //set the chart name
    chartName.setText(chartSettings.getChartName());
    //get the start date
    Date start = chartSettings.getStartDate();
    //get the end date
    Date end = chartSettings.getEndDate();
    toDate = Calendar.getInstance();
    fromDate = Calendar.getInstance();
    //IF there a start date
    if (start != null) {
      //set the startDate to the one in chart setting
      toDate.setTime(start);
      //IF there is a end date
      if (end != null)
        //set the end date from the one in the chart setting
        fromDate.setTime(end);
    }

  } 
  
  protected void onStop () {
   // datePickerFrag.dismiss();
    super.onStop();
  }

  private void initMetricList() {
      //load the metrics list
      String dbName = chartSettings.getDatabase();
      getMetricsFromSalesforce().execute(dbName);
    }
  
  
  private void selectMetricInSpinner() {
    //select the metric that is set to the current chart/graph setting
    String metric = chartSettings.getMetric();
    int position = metricsAdapter.getPosition(metric);
    metricsSPinner.setSelection(position);
  }

  /**
   * A helper method that returns a async task to fetch metrics from Salesforce
   * @return asyncTask to fetch metrics from Salesforce
   */
  private AsyncTask<String, Void, List<String>> getMetricsFromSalesforce() {
    return new AsyncTask<String, Void, List<String>> () {

      protected void onPreExecute() {
        progressBar.setMessage(LOADING_METRICS);

        progressBar.show();
      }
      @Override
      protected List<String> doInBackground(String... params) {
        CJAnalyticsApp cjAnalyApp = (CJAnalyticsApp)getApplicationContext();
        String dbName = params[0];
        try {
          Map<String, EventType> metrics = DataFetcher.getDatabaseMetrics(getString(R.string.api_version), cjAnalyApp.getRestClient(), dbName);
          List<String> dbList = new LinkedList<String> (); 
          for (String metric : metrics.keySet()) {
            dbList.add(metric);

          }

          return dbList;
        } catch (Exception e) {
          Log.e("Edit Activity loading metrics", "unable to load metrics", e);
          Toast toast = Toast.makeText(getApplicationContext(), "Unable to load metrics",  Toast.LENGTH_SHORT);
          toast.show();
          //if an error occurs, return empty list
          return new LinkedList<String>();
        }
      }

      @Override
      protected void onPostExecute(List<String> result) {
        metricsList = new ArrayList<String>(result);
        metricsAdapter.addAll(result);
        metricsSPinner.setAdapter(metricsAdapter);
        selectMetricInSpinner();
        //remove the progress bar
        progressBar.dismiss();

      }
    };

  }


  public void changeType(View v) {
    // This is pointless since the buttons are toggle switches, there
    // state are visible to the user when one edits (changes states)
    // Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
    uncheckAllToggleSwitches();
    switch (v.getId()){
    case R.id.line:
      lineButton.toggle();
      break;
    case R.id.bar:
      barButton.toggle();
      break;
    case R.id.pie:
      pieButton.toggle();
      break;
    default:
      Log.i(EditActivity.class.getName(), "toggle switch's change type was called on unknown graph/chart type");
    }
  }

  private void uncheckAllToggleSwitches() {
    lineButton.setChecked(false);
    barButton.setChecked(false);
    pieButton.setChecked(false);    
  }

  public void save(View v) {
    saveChart();
  }

  private void saveChart() {
    Intent intent = new Intent();
    chartSettings.setChartName(chartName.getText().toString());
    chartSettings.setType(getSelectedType());
    chartSettings.setStartDate(toDate.getTime());
    chartSettings.setEndDate(fromDate.getTime());
    chartSettings.setMetric(getMetric());
    chartSettings.saveToIntent(intent);

    //save the graph to db
    ChartSettingsProvider.saveSettings(getContentResolver(), chartSettings);
    //result the intent with ok 
    setResult(RESULT_OK, intent);
    finish();
  }

  private String getMetric() {
    return (String)metricsSPinner.getSelectedItem();
  }

  public void setToFromDate (View view) {
    switch (view.getId()) {
    case R.id.toDate:
      showDatePickerDialog(toDate, DateType.TO);
      break;
    case R.id.fromDate:
      showDatePickerDialog(fromDate, DateType.FROM);
      break;
    }

  }

  /**
   * From tutorial at 
   * http://androidtrainningcenter.blogspot.com/2012/10/creating-datepicker-using.html
   * @param calendar
   */
  protected void showDatePickerDialog(Calendar calendar, DateType dateType) {
    //create a date picker fragment
    datePickerFrag = new DatePickerFragment();
    Bundle args = new Bundle();
    //add year, month, and day to a bundle to pass to date picker frag
    args.putInt(YEAR, calendar.get(Calendar.YEAR));

    args.putInt(MONTH, calendar.get(Calendar.MONTH));
    args.putInt(DAY, calendar.get(Calendar.DAY_OF_MONTH));
    //set the arguments
    datePickerFrag.setArguments(args);
    DatePickerDialog.OnDateSetListener callback;
    //set the callback for date picker, which is called when user clicks set 
    datePickerFrag.setCallBack(getDatePickerCallback(dateType));
    onAttachFragment(datePickerFrag);
    //show the date picker dialog
    datePickerFrag.show(getFragmentManager(), DATE_PICKER_TITLE);

  }

  enum DateType {
    FROM, TO;
  }
  /**
   * Returns a date picker callback listener that sets the date in the calendar
   * and updates the text on the screen
   * @param calendar calendar whose date to set after the user clicks 'set' in the date picker
   * @return a date picker callback listener that updates the view with the new date
   */
  private DatePickerDialog.OnDateSetListener getDatePickerCallback (final DateType dateType) {
    return new DatePickerDialog.OnDateSetListener() {

      @Override
      public void onDateSet(DatePicker view, int year, int monthOfYear,
          int dayOfMonth) {
        Calendar newDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        switch (dateType) {
        case FROM:
          if (newDate.after(toDate)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid From-Date. From-date cannot occur after to-Date",  Toast.LENGTH_SHORT);
            toast.show();
          }else {
            fromDate = newDate;
            updateDateView();
          }
          break;
        case TO:
          if (newDate.before(fromDate)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid To-Date. To date cannot occur before from-Date",  Toast.LENGTH_SHORT);
            toast.show();
          }else {
            toDate = newDate;
            updateDateView();
          }
          break;
        }

      }
    };
  }


  /**
   * Updates the to and from date fields to reflect the date in toDate and
   * fromDate
   */
  private void updateDateView() {
    String td = dateFormater.format(toDate.getTime());
    String fd = dateFormater.format(fromDate.getTime());
    toDateText.setText(td);
    fromDateText.setText(fd);

  }


  /**
   * DatePickerFragment that set ups the DialogFragement for showing 
   * the date picker dialog.
   * Followed from tutorial at 
   * http://androidtrainningcenter.blogspot.com/2012/10/creating-datepicker-using.html
   * @author gagandeep
   *
   */
  public static class DatePickerFragment extends DialogFragment {
    //callback for datepicker dialog, called after user clicks set
    private DatePickerDialog.OnDateSetListener callback;
    //field for year, month, and day
    private int year, month, day;

    public DatePickerFragment() {
      super();
      //Empty constructor is needed for DialogFragment class
    };

    /**
     * Setter for DatePickerDialog callback
     * @param callback function to call after user clicks set on the 
     * date picker dialog
     */
    public void setCallBack(DatePickerDialog.OnDateSetListener callback) {
      this.callback = callback;
    }
    /**
     * Sets the arguments from the bundle such as year, month, and day
     */
    @Override
    public void setArguments (Bundle args) {
      super.setArguments(args);
      year = args.getInt(YEAR);
      month = args.getInt(MONTH);
      day = args.getInt(DAY);
    }

    /**
     * Creates and returns a new DatePickerDialog
     */
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
      return new DatePickerDialog(getActivity(), callback, year, month, day);
    }    
  }

  public void cancel(View v) {
    // s.setType(getSelectedType());
    // Intent i=new Intent();
    // s.save(i);
    //
    // setResult(RESULT_CANCELED,i);

    // setResult(RESULT_CANCELED);
    finish();

  }

  private ChartType getSelectedType() {
    if (lineButton.isChecked()) {
      return ChartType.Line;
    }
    if (barButton.isChecked()) {
      return ChartType.Bar;
    }
    if (pieButton.isChecked()) {
      return ChartType.Pie;
    }

    // TODO: Handle this some better way than returning null
    return null;
  }
}
