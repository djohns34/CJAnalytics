package edu.calpoly.codastjegga.cjanalyticsapp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class DateUtils {
  //data formatter

  private static final DateFormat df;
  
  static{
    df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
  }

  public static Date parse(String dateString) {

    Date date=null;

    if(dateString!=null){
      try {
        date=df.parse(dateString);
      }  catch (ParseException e) {}
    }
    if(date==null){
      Log.e("Event", "Unable to parse dateString, returning null");
    }
    return date;
  }
  
  public static String format(Date date) {
    String dateString=null;
    if(date!=null){
      dateString= df.format(date);
      Log.e("Event", "Unable to format date, returning null");
    }
    return dateString;
  }

}
