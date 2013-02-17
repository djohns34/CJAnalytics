package edu.calpoly.codastjegga.cjanalyticsapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import android.widget.ToggleButton;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.datafetcher.DataFetcher;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventType;

public class EditActivity extends FragmentActivity {

  private static final String DAY = "day";
  private static final String MONTH = "month";
  private static final String YEAR = "year";

  private static final String DATE_PICKER_TITLE = "Date Picker";

  private static final DateFormat dateFormater = new SimpleDateFormat("MM-dd-yyyy");
  private ToggleButton lineButton;
  private ToggleButton barButton;
  private ToggleButton pieButton;
  private ChartSettings chartSettings;
  private EditText chartName;
  private Spinner metricsList;
  private TextView toDateText, fromDateText;
  private Calendar toDate, fromDate;
  private LinearLayout editChartLayout;
  private ProgressDialog progressBar;
  private ArrayAdapter<String> metricsAdapter;

  protected void onCreate(Bundle savedInstanceState) {
    Intent intent = this.getIntent();

    super.onCreate(savedInstanceState);
    setContentView(R.layout.editchart);

    lineButton = (ToggleButton) this.findViewById(R.id.line);
    barButton = (ToggleButton) this.findViewById(R.id.bar);
    pieButton = (ToggleButton) this.findViewById(R.id.pie);
    chartName = (EditText) this.findViewById(R.id.graphName);
    metricsList = (Spinner) this.findViewById(R.id.metricsList);
    toDateText = (TextView) this.findViewById(R.id.toDate);
    fromDateText = (TextView) this.findViewById(R.id.fromDate);
    editChartLayout = (LinearLayout) this.findViewById(R.id.editChartParentLayout);
    progressBar = new ProgressDialog(this);

    toDate = Calendar.getInstance();
    fromDate = Calendar.getInstance();

    // This class should always contain chartSetting in its intent
    // If the intent contains chart data, load it, otherwise, create a new chart
    if (intent.hasExtra(ChartType.class.getName())) {
      chartSettings = ChartSettings.load(intent);
    } else {
      chartSettings = new ChartSettings();
    }
    initMetricAdapter();
    initMetricsList();
    initChartSettingInView();
    updateDateView();

  }


  private void initChartSettingInView() {
    //set the chart name
    chartName.setText(chartSettings.getChartName());
    //get the start date
    Date start = chartSettings.getStartDate();
    //get the end date
    Date end = chartSettings.getEndDate();
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
  
  private void initMetricAdapter() {
    metricsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
    metricsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    metricsList.setAdapter(metricsAdapter);

  }

  private void initMetricsList() {
    String dbName = chartSettings.getDatabase();
    getMetricsMap().execute(dbName);

  }

  private AsyncTask<String, Void, List<String>> getMetricsMap() {
    return new AsyncTask<String, Void, List<String>> () {

      protected void onPreExecute() {
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
          //if an error occurs, return empty list
          return new LinkedList<String>();
        }
      }

      @Override
      protected void onPostExecute(List<String> result) {
        metricsAdapter.addAll(result);
        //select the metric that is set to the current chart/graph setting
        String metric = chartSettings.getMetric();
        int position = metricsAdapter.getPosition(metric);
        metricsList.setSelection(position);
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
      lineButton.setChecked(true);
      break;
    case R.id.bar:
      barButton.setChecked(true);
      break;
    case R.id.pie:
      pieButton.setChecked(true);
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
    Intent i = new Intent();

    chartSettings.setType(getSelectedType());
    chartSettings.saveToIntent(i);

    setResult(RESULT_OK, i);
    finish();
  }

  public void setToFromDate (View view) {
    switch (view.getId()) {
    case R.id.toDate:
      showDatePickerDialog(toDate);
      break;
    case R.id.fromDate:
      showDatePickerDialog(fromDate);
      break;
    }

  }

  /**
   * From tutorial at 
   * http://androidtrainningcenter.blogspot.com/2012/10/creating-datepicker-using.html
   * @param calendar
   */
  protected void showDatePickerDialog(Calendar calendar) {
    //create a date picker fragment
    DatePickerFragment datePickerFrag = new DatePickerFragment();
    Bundle args = new Bundle();
    //add year, month, and day to a bundle to pass to date picker frag
    args.putInt(YEAR, calendar.get(Calendar.YEAR));

    args.putInt(MONTH, calendar.get(Calendar.MONTH));
    args.putInt(DAY, calendar.get(Calendar.DAY_OF_MONTH));
    //set the arguments
    datePickerFrag.setArguments(args);
    //set the callback for date picker, which is called when user clicks set
    datePickerFrag.setCallBack(getDatePickerCallback(calendar));
    //show the date picker dialog
    datePickerFrag.show(getFragmentManager(), DATE_PICKER_TITLE);

  }

  /**
   * Returns a date picker callback listener that sets the date in the calendar
   * and updates the text on the screen
   * @param calendar calendar whose date to set after the user clicks 'set' in the date picker
   * @return a date picker callback listener that updates the view with the new date
   */
  private DatePickerDialog.OnDateSetListener getDatePickerCallback (final Calendar calendar) {
    return new DatePickerDialog.OnDateSetListener() {

      @Override
      public void onDateSet(DatePicker view, int year, int monthOfYear,
          int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        updateDateView();
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
  private class DatePickerFragment extends DialogFragment {
    //callback for datepicker dialog, called after user clicks set
    private DatePickerDialog.OnDateSetListener callback;
    //field for year, month, and day
    private int year, month, day;

    public DatePickerFragment() {
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
