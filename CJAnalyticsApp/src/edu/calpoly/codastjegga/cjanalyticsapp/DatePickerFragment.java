package edu.calpoly.codastjegga.cjanalyticsapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * DatePickerFragment that set ups the DialogFragement for showing the date
 * picker dialog. Followed from tutorial at
 * http://androidtrainningcenter.blogspot
 * .com/2012/10/creating-datepicker-using.html
 * 
 * @author gagandeep
 */
public class DatePickerFragment extends DialogFragment{

  public enum DateType {
    FROM, TO;
  }
  public static final String DAY = "day";
  public static final String MONTH = "month";
  public static final String YEAR = "year";
  
    // callback for datepicker dialog, called after user clicks set
    private DatePickerDialog.OnDateSetListener callback;
    // field for year, month, and day
    private int year, month, day;

    public DatePickerFragment() {
      super();
      // Empty constructor is needed for DialogFragment class
    };
    
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//        Bundle savedInstanceState) {
//      View view = inflater.inflate(R.layout.fragment_date_picker, container);
//      return view;
//    }
    

    /**
     * Setter for DatePickerDialog callback
     * 
     * @param callback
     *          function to call after user clicks set on the date picker dialog
     */
    public void setCallBack(DatePickerDialog.OnDateSetListener callback) {
      this.callback = callback;
    }

    /**
     * Sets the arguments from the bundle such as year, month, and day
     */
    @Override
    public void setArguments(Bundle args) {
      super.setArguments(args);
      year = args.getInt(YEAR);
      month = args.getInt(MONTH);
      day = args.getInt(DAY);
    }

    /**
     * Creates and returns a new DatePickerDialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      return new DatePickerDialog(getActivity(), callback, year, month, day);
    }
  
}
