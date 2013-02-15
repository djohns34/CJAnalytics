package edu.calpoly.codastjegga.cjanalyticsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;

public class EditActivity extends Activity {

  private ToggleButton lineButton;
  private ToggleButton barButton;
  private ToggleButton pieButton;
  private ChartSettings chartSettings;

  private static final String CONFIRM_SAVE = "Are you sure you want to save?";
  private static final String SAVE = "Save Changes";

  protected void onCreate(Bundle savedInstanceState) {
    Intent intent = this.getIntent();

    super.onCreate(savedInstanceState);
    setContentView(R.layout.editchart);

    lineButton = (ToggleButton) this.findViewById(R.id.line);
    barButton = (ToggleButton) this.findViewById(R.id.bar);
    pieButton = (ToggleButton) this.findViewById(R.id.pie);

    // If the intent contains chart data, load it, otherwise, create a new chart
    if (intent.hasExtra(ChartType.class.getName())) {
      chartSettings = ChartSettings.load(intent);
    } else {
      chartSettings = new ChartSettings();
    }
  }

  public void changeType(View v) {
    // This is pointless since the buttons are toggle switches, there
    // state are visible to the user when one edits (changes states)
    // Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
    if (v.getId() != R.id.line) {
      lineButton.setChecked(false);
    }
    if (v.getId() != R.id.bar) {
      barButton.setChecked(false);
    }
    if (v.getId() != R.id.pie) {
      pieButton.setChecked(false);
    }

  }

  public void save(View v) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

    alertDialogBuilder.setTitle(SAVE);

    alertDialogBuilder.setMessage(CONFIRM_SAVE);

    alertDialogBuilder.setCancelable(false);

    alertDialogBuilder.setPositiveButton("Yes",
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            saveChart();
          }
        });

    alertDialogBuilder.setNegativeButton("No",
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
          }
        });

    AlertDialog alertDialog = alertDialogBuilder.create();

    alertDialog.show();
  }

  private void saveChart() {
    Intent i = new Intent();
    
    chartSettings.setType(getSelectedType());
    chartSettings.saveToIntent(i);

    setResult(RESULT_OK, i);
    finish();
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
