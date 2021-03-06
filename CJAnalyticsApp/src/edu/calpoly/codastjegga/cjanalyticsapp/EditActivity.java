package edu.calpoly.codastjegga.cjanalyticsapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.salesforce.androidsdk.app.ForceApp;

import edu.calpoly.codastjegga.cjanalyticsapp.cache.DBMetricsCache;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.TimeInterval;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettingsProvider;
import edu.calpoly.codastjegga.cjanalyticsapp.datafetcher.DataFetcher;
import edu.calpoly.codastjegga.cjanalyticsapp.event.EventType;
import edu.calpoly.codastjegga.cjanalyticsapp.DatePickerFragment.DateType;

import static edu.calpoly.codastjegga.cjanalyticsapp.DatePickerFragment.*;

public class EditActivity extends FragmentActivity {


  private static final String SAVED_TO_DATE = "savedtodate";
  private static final String SAVED_FROM_DATE = "savedfromdate";
  private static final String DATE_PICKER_TITLE = "Date Picker";
  private static final String ON_SAVE_INVALID_NAME_MESSAGE_ERROR = "Invalid Chart Name, please enter a chart name.";
  private static final String ON_SAVE_INVALID_EVENT_TYPE = "Cannot render line graph with non-numeric event type";

  private static final DateFormat dateFormater = new SimpleDateFormat(
      "MM-dd-yyyy");

  private boolean isSecondaryMetricVisible;
  private boolean isSecondaryMetricEnabled;

  private View loading;
  private View view;

  private ToggleButton lineButton;
  private ToggleButton barButton;
  private ToggleButton pieButton;
  private ChartSettings chartSettings;
  private EditText chartName;
  private Spinner metricSpinnerPrimary, metricSpinnerSecondary;
  private Spinner intervalSpinner;
  private TextView toDateText, fromDateText;
  private Calendar toDate, fromDate;
  // private ArrayAdapter<String> metricsAdapter;
  private EventAdapter eventAdapter;
  private ImageView toggleMetricButton;

  private DatePickerFragment datePickerFrag;
  private MetricsFetecherTask eventFetcherTask;

  private LinearLayout timeIntervalView;
  private LinearLayout colorPalette;

  @SuppressWarnings("unchecked")
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    Intent intent = this.getIntent();
    setContentView(R.layout.activity_edit_chart);

    lineButton = (ToggleButton) this.findViewById(R.id.line);
    barButton = (ToggleButton) this.findViewById(R.id.bar);
    pieButton = (ToggleButton) this.findViewById(R.id.pie);
    chartName = (EditText) this.findViewById(R.id.chartName);
    metricSpinnerPrimary = (Spinner) this.findViewById(R.id.metricsListPrimary);
    metricSpinnerSecondary = (Spinner) this
        .findViewById(R.id.metricsListSecondary);
    intervalSpinner = (Spinner) this.findViewById(R.id.intervalList);
    toDateText = (TextView) this.findViewById(R.id.toDate);
    fromDateText = (TextView) this.findViewById(R.id.fromDate);
    toggleMetricButton = (ImageView) this.findViewById(R.id.toggleMetricButton);
    timeIntervalView = (LinearLayout) this.findViewById(R.id.timeIntervalView);
    colorPalette = (LinearLayout) this.findViewById(R.id.colorPalette);

    intervalSpinner.setAdapter(new ArrayAdapter<TimeInterval>(this,
        android.R.layout.simple_spinner_item, TimeInterval.values()));

    // This class should always contain chartSetting in its intent
    // If the intent contains chart data, load it, otherwise, create a new chart
    if (intent.hasExtra(ChartType.class.getName())) {
      chartSettings = ChartSettings.load(intent);
    } else {
      chartSettings = new ChartSettings();
    }

    if (savedInstanceState != null) {
      toDate = (Calendar) savedInstanceState.get(SAVED_TO_DATE);
      fromDate = (Calendar) savedInstanceState.get(SAVED_FROM_DATE);
    } else {
      setChartToggle(chartSettings.getType());
      initChartSettingInView();
    }

    initEventsList();
    updateDateView();
  }

  private void setAdapter(List<Map.Entry<String, EventType>> metrics) {
    eventAdapter.setEventsList(metrics);
    metricSpinnerPrimary.setAdapter(eventAdapter);
    metricSpinnerSecondary.setAdapter(eventAdapter);
    selectEventInSpinner();
  }

  @Override
  protected void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();
    if (eventFetcherTask != null) {
      eventFetcherTask.cancel(true);
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    // TODO Auto-generated method stub
    super.onSaveInstanceState(outState);
    outState.putSerializable(SAVED_TO_DATE, toDate);
    outState.putSerializable(SAVED_FROM_DATE, fromDate);
  }

  private void setChartToggle(ChartType chartType) {
    uncheckAllToggleSwitches();
    switch (chartType) {
    case Bar:
      barButton.toggle();
      enableSecondaryMetric();
      timeIntervalView.setVisibility(LinearLayout.GONE);
      break;
    case Line:
      lineButton.toggle();
      enableSecondaryMetric();
      timeIntervalView.setVisibility(LinearLayout.VISIBLE);
      break;
    case Pie:
      pieButton.toggle();
      disableSecondaryMetric();
      timeIntervalView.setVisibility(LinearLayout.GONE);
      break;
    default:
      break;

    }
  }

  private void initChartSettingInView() {
    // set the chart name
    chartName.setText(chartSettings.getChartName());
    // get the start date
    Date start = chartSettings.getStartDate();
    // get the end date
    Date end = chartSettings.getEndDate();
    toDate = Calendar.getInstance();
    fromDate = Calendar.getInstance();
    // IF there's a start date
    if (start != null) {
      // set the startDate to the one in chart setting
      fromDate.setTime(start);
    }
    // IF there's an end date
    if (end != null) {
      // set the end date from the one in the chart setting
      toDate.setTime(end);
    }

    // set the interval
    TimeInterval interval = chartSettings.getTimeInterval();
    int index = Arrays.asList(TimeInterval.values()).indexOf(interval);
    intervalSpinner.setSelection(index);

  }

  protected void onStop() {
    // datePickerFrag.dismiss();
    super.onStop();
  }

  private void initEventsList() {
    // create new event adapter
    eventAdapter = new EventAdapter(this);
    // set up the adapter to events list
    metricSpinnerPrimary.setAdapter(eventAdapter);
    metricSpinnerSecondary.setAdapter(eventAdapter);
    // load the metrics list
    String dbName = chartSettings.getDatabase();
    List<Map.Entry<String, EventType>> metrics = DBMetricsCache
        .getCachedMetrics(dbName);
    if (metrics == null) {
      fetchMetrics();
    } else {
      setAdapter(metrics);
    }

  }

  private void fetchMetrics() {
    eventFetcherTask = new MetricsFetecherTask(this,
        chartSettings.getDatabase());
    eventFetcherTask.execute();
  }

  private void selectEventInSpinner() {
    // select the metric name and type that is set to the current chart/chart
    // setting (Note there cannot be duplicate metric by the same name in the
    // list)
    String eventName = chartSettings.getEventName();
    String eventName2 = chartSettings.getEventName2();
    EventType metricType = chartSettings.getEventType();
    EventType metricType2 = chartSettings.getEventType2();
    ChartType chartType = chartSettings.getType();
    int position = eventAdapter
        .getPosition(new AbstractMap.SimpleEntry<String, EventType>(eventName,
            metricType));

    metricSpinnerPrimary.setSelection(position);

    isSecondaryMetricVisible = false;
    isSecondaryMetricEnabled = chartType != ChartType.Pie;

    if (eventName2 != null) {
      position = eventAdapter
          .getPosition(new AbstractMap.SimpleEntry<String, EventType>(
              eventName2, metricType2));
      isSecondaryMetricVisible = true;
      metricSpinnerSecondary.setSelection(position);
      if (isSecondaryMetricEnabled) {
        enableSecondaryMetric();
        toggleMetricButton.setBackgroundResource(R.drawable.minus_btn);
      }
    }
  }

  private void setColorPalette() {

  }

  /**
   * A helper method that returns a async task to fetch metrics from Salesforce
   * 
   * @return asyncTask to fetch metrics from Salesforce
   */
  private class MetricsFetecherTask extends
      AsyncTask<Void, Void, List<Map.Entry<String, EventType>>> {

    private ProgressDialog dialog;
    private Activity activity;
    private static final String LOADING_METRICS = "Loading Metrics...";
    private String dbName;

    public MetricsFetecherTask(Activity activity, String databaseName) {
      this.activity = activity;
      this.dbName = databaseName;
      this.dialog = new ProgressDialog(activity);
      this.dialog.setCancelable(false);
      this.dialog.setCanceledOnTouchOutside(false);
    }

    protected void onPreExecute() {
      loading = findViewById(R.id.loading);
      view = findViewById(R.id.view);

      loading.setVisibility(View.VISIBLE);
      view.setVisibility(View.GONE);
    }

    @Override
    protected List<Map.Entry<String, EventType>> doInBackground(Void... params) {
      CJAnalyticsApp cjAnalyApp = (CJAnalyticsApp) activity
          .getApplicationContext();
      try {
        return DataFetcher.getDatabaseMetrics(getString(R.string.api_version),
            cjAnalyApp.getRestClient(), dbName);

      } catch (Exception e) {

        Log.e("Edit Activity loading metrics", "unable to load metrics", e);
        // if an error occurs, return empty list
        return null;
      }
    }

    @Override
    protected void onPostExecute(List<Map.Entry<String, EventType>> result) {
      if (activity != null) {
        if (result != null) {
          DBMetricsCache.cacheMetrics(dbName, result);
        } else {
          Toast toast = Toast.makeText(getApplicationContext(),
              "Unable to load metrics", Toast.LENGTH_SHORT);
          toast.show();
        }

        setAdapter(result);
      }
      // remove the progress bar
      loading.setVisibility(View.GONE);
      view.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onCancelled() {
      // TODO Auto-generated method stub
      super.onCancelled();
      loading.setVisibility(View.GONE);
      view.setVisibility(View.VISIBLE);
    }

  };

  public void changeType(View v) {

    ChartType type = ChartType.getTypeById(v.getId());
    if (type != null)
      setChartToggle(type);
  }

  private void uncheckAllToggleSwitches() {
    lineButton.setChecked(false);
    barButton.setChecked(false);
    pieButton.setChecked(false);
  }

  public boolean isPrimaryMetricNumeric() {
    return getPrimaryMetric().getValue() == EventType.Float
        || getPrimaryMetric().getValue() == EventType.Currency
        || getPrimaryMetric().getValue() == EventType.Number;
  }

  public boolean isSecondaryMetricNumeric() {
    return getSecondaryMetric().getValue() == EventType.Float
        || getSecondaryMetric().getValue() == EventType.Currency
        || getSecondaryMetric().getValue() == EventType.Number;
  }

  public void onClickSave(MenuItem item) {
    // IF the chart name is empty
    if (chartName.getText().length() == 0) {
      Toast.makeText(this, ON_SAVE_INVALID_NAME_MESSAGE_ERROR,
          Toast.LENGTH_SHORT).show();
      // TODO: TOTALLY HACKED BY JEREMY. I'm adding in getValue to
      // getSelectedEvent
    } else if (getPrimarySelectedType() == ChartType.Line
        && !isPrimaryMetricNumeric()) {
      Toast.makeText(this, ON_SAVE_INVALID_EVENT_TYPE, Toast.LENGTH_SHORT)
          .show();
    } else if (isSecondaryMetricEnabled && isSecondaryMetricVisible
        && getSecondarySelectedType() == ChartType.Line
        && !isSecondaryMetricNumeric()) {
      Toast.makeText(this, ON_SAVE_INVALID_EVENT_TYPE, Toast.LENGTH_SHORT)
      .show();
    } else {
      saveChart();
    }
  }

  private void saveChart() {
    Intent intent = new Intent();
    chartSettings.setChartName(chartName.getText().toString());
    chartSettings.setType(getPrimarySelectedType());
    chartSettings.setStartDate(fromDate.getTime());
    chartSettings.setEndDate(toDate.getTime());
    // get the selected event name and type
    Map.Entry<String, EventType> eventInfo = getPrimaryMetric();
    chartSettings.setEventName(eventInfo.getKey());
    chartSettings.setEventType(eventInfo.getValue());
    chartSettings.setUsername(((CJAnalyticsApp) getApplicationContext())
        .getCurrentUserName());
    chartSettings.setTimeInterval((TimeInterval) intervalSpinner
        .getSelectedItem());
    chartSettings.saveToIntent(intent);

    eventInfo = getSecondaryMetric();
    if (isSecondaryMetricEnabled && isSecondaryMetricVisible) {
      chartSettings.setEventName2(eventInfo.getKey());
      chartSettings.setEventType2(eventInfo.getValue());
    } else {
      chartSettings.setEventName2(null);
      chartSettings.setEventType2(EventType.None);
    }

    // save the chart to db
    ChartSettingsProvider.saveSettings(getContentResolver(), chartSettings);
    // result the intent with ok
    setResult(RESULT_OK, intent);
    finish();
  }

  public void onClickCancel(MenuItem item) {
    setResult(RESULT_CANCELED);
    finish();

  }

  public void onClickRefreshButton(MenuItem menu) {
    String dbName = chartSettings.getDatabase();
    if (dbName != null) {
      fetchMetrics();
    }
  }

  @SuppressWarnings("unchecked")
  private Map.Entry<String, EventType> getPrimaryMetric() {
    return (Map.Entry<String, EventType>) metricSpinnerPrimary
        .getSelectedItem();
  }

  @SuppressWarnings("unchecked")
  private Map.Entry<String, EventType> getSecondaryMetric() {
    return (Map.Entry<String, EventType>) metricSpinnerSecondary
        .getSelectedItem();
  }

  public void setToFromDate(View view) {
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
   * http://androidtrainningcenter.blogspot.com/2012/10/creating
   * -datepicker-using.html
   * 
   * @param calendar
   */
  protected void showDatePickerDialog(Calendar calendar, DateType dateType) {
    // create a date picker fragment
    datePickerFrag = new DatePickerFragment();
    Bundle args = new Bundle();
    // add year, month, and day to a bundle to pass to date picker frag
    args.putInt(YEAR, calendar.get(Calendar.YEAR));

    args.putInt(MONTH, calendar.get(Calendar.MONTH));
    args.putInt(DAY, calendar.get(Calendar.DAY_OF_MONTH));
    // set the arguments
    datePickerFrag.setArguments(args);
    // set the callback for date picker, which is called when user clicks set
    datePickerFrag.setCallBack(getDatePickerCallback(dateType));
    onAttachFragment(datePickerFrag);
    // show the date picker dialog
    datePickerFrag.show(getFragmentManager(), DATE_PICKER_TITLE);

  }



  /**
   * Returns a date picker callback listener that sets the date in the calendar
   * and updates the text on the screen
   * 
   * @param calendar
   *          calendar whose date to set after the user clicks 'set' in the date
   *          picker
   * @return a date picker callback listener that updates the view with the new
   *         date
   */
  private DatePickerDialog.OnDateSetListener getDatePickerCallback(
      final DateType dateType) {
    return new DatePickerDialog.OnDateSetListener() {

      @Override
      public void onDateSet(DatePicker view, int year, int monthOfYear,
          int dayOfMonth) {
        Calendar newDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        switch (dateType) {
        case FROM:
          if (newDate.after(toDate)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                "Invalid From-Date. From-date cannot occur after to-Date",
                Toast.LENGTH_SHORT);
            toast.show();
          } else {
            fromDate = newDate;
            updateDateView();
          }
          break;
        case TO:
          if (newDate.before(fromDate)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                "Invalid To-Date. To date cannot occur before from-Date",
                Toast.LENGTH_SHORT);
            toast.show();
          } else {
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



  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_edit_chart, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case android.R.id.home:
      onClickCancel(item);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private ChartType getPrimarySelectedType() {
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

  private ChartType getSecondarySelectedType() {
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

  // Required in any activity that requires authentication
  @Override
  protected void onPause() {
    super.onPause();
    PasscodeProtected.onPause(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    PasscodeProtected.onResume(this);
  }

  @Override
  public void onUserInteraction() {
    super.onUserInteraction();
    PasscodeProtected.onUserInteraction();
  }

  // End required sections

  public void onLogoutClick(MenuItem menu) {
    ForceApp.APP.logout(this);
  }

  public void onToggleMetricButtonClick(View v) {
    if (isSecondaryMetricVisible) {
      toggleMetricButton.setBackgroundResource(R.drawable.plus_btn);
      metricSpinnerSecondary.setVisibility(View.GONE);
    } else {
      toggleMetricButton.setBackgroundResource(R.drawable.minus_btn);
      metricSpinnerSecondary.setVisibility(View.VISIBLE);
    }
    isSecondaryMetricVisible = !isSecondaryMetricVisible;
  }

  public void enableSecondaryMetric() {
    toggleMetricButton.setVisibility(View.VISIBLE);
    if (isSecondaryMetricVisible) {
      metricSpinnerSecondary.setVisibility(View.VISIBLE);
    }
    isSecondaryMetricEnabled = true;
  }

  public void disableSecondaryMetric() {
    toggleMetricButton.setVisibility(View.INVISIBLE);
    metricSpinnerSecondary.setVisibility(View.GONE);
    isSecondaryMetricEnabled = false;
  }

  @Override
  public void finish() {
    super.finish();
    overridePendingTransition(R.anim.none, R.anim.slide_out_top);
  }
}
