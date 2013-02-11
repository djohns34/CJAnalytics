package edu.calpoly.codastjegga.cjanalyticsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;

public class EditActivity extends Activity {


  private ToggleButton line;
  private ToggleButton bar;
  private ToggleButton pie;
  private ChartSettings s;

  private static final String CONFIRM_SAVE = "Are you sure you want to save?";
  private static final String SAVE = "Save Changes";

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.editchart);

    line=(ToggleButton)findViewById(R.id.line);
    bar=(ToggleButton)findViewById(R.id.bar);
    pie=(ToggleButton)findViewById(R.id.pie);

    s=ChartSettings.load(getIntent());

  }




  public void changeType(View v) {
    //This is pointless since the buttons are toggle switches, there
    //state are visible to the user when one edits (changes states)
    // Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
    if(v.getId()!=R.id.line){
      line.setChecked(false);
    }
    if(v.getId()!=R.id.bar){
      bar.setChecked(false);
    }
    if(v.getId()!=R.id.pie){
      pie.setChecked(false);
    }

  }

  public void save(View v) {

    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

    // set title
    alertDialogBuilder.setTitle(SAVE);

    // set dialog message
    alertDialogBuilder
    .setMessage(CONFIRM_SAVE)
    .setCancelable(false)
    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog,int id) {
        // if this button is clicked, close
        // current activity
        save();
      }
    })
    .setNegativeButton("No",new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog,int id) {
        // if this button is clicked, just close
        // the dialog box and do nothing
        dialog.cancel();
      }
    });

    // create alert dialog
    AlertDialog alertDialog = alertDialogBuilder.create();

    // show it
    alertDialog.show();
  }


  private void save () {
    s.setType(getSelectedType());
    Intent i=new Intent();
    s.save(i);

    setResult(RESULT_OK,i);
    finish(); 
  }

  public void cancel(View v) {
    //    s.setType(getSelectedType());
    //    Intent i=new Intent();
    //    s.save(i);
    //    
    //    setResult(RESULT_CANCELED,i);

    // setResult(RESULT_CANCELED);
    finish();

  }


  private ChartType getSelectedType(){
    if(line.isChecked()){
      return ChartType.Line;
    }   
    if(bar.isChecked()){
      return ChartType.Bar;
    }
    if(pie.isChecked()){
      return ChartType.Pie;
    }
    return null;
  }


}
