package edu.calpoly.codastjegga.cjanalyticsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.chart.ChartType;

public class EditActivity extends Activity {
  

  private ToggleButton line;
  private ToggleButton bar;
  private ToggleButton pie;
  private ChartSettings s;
  

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.editchart);
    
    line=(ToggleButton)findViewById(R.id.line);
    bar=(ToggleButton)findViewById(R.id.bar);
    pie=(ToggleButton)findViewById(R.id.pie);
    
    s=ChartSettings.load(getIntent());

  }
  
  
  

  public void changeType(View v) {
    Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
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
    s.setType(getSelectedType());
    Intent i=new Intent();
    s.save(i);
    
    setResult(RESULT_OK,i);
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
