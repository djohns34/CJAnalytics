package com.example.buttonapp;

import java.net.URISyntaxException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import edu.calpoly.codastjegga.auth.Token;
import edu.calpoly.codastjegga.sdk.CodastSDK;
import edu.calpoly.codastjegga.sdk.EventType;

public class MainActivity extends Activity {

  static final String api = "v23.0";
  static final String loginInstance = "https://na9.salesforce.com";
  static final Token token = new Token(
      "00DE0001000dgAF!AQwAQIpSdhxe_ncKl_4bN2cc8q.e89gqxk0Julhd4SiZszOxupA.r12tkcbejBRWixyuvKOX5zDJVQ_sThHcQpRAmjHSJSFo",
      "5Aep861rEpScxnNE64IogmmXsb72ONfi9xneXYd7423HJqVESg_uE.dE.85z.8DhrhkoQed5DSFKw==");
  static final String clientId = "3MVG9y6x0357HlefAMuoGiOstBQg3r5rHpEodDBNQ6GIjd5Fh.RPRX4YA6Ax_sg69Jq8tpuFU76NlE0z5LTbc";

  CodastSDK sdk;
  EditText stringField, floatField;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    stringField = (EditText) findViewById(R.id.editText1);
    floatField = (EditText) findViewById(R.id.editText2);

    try {
      sdk = new CodastSDK(getApplication(), loginInstance, token, "Button-App",
          api, clientId);// , api, clientId);
    } catch (URISyntaxException ex) {
      Log.i("MainActivity", "Invalid LoginInstance URI", ex);
    }

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }

  public void onSendTextEventClick(View view) {
    String value = stringField.getText().toString();
    if (value.length() > 0) {
      sdk.trackData(EventType.Text, "my-strings", value);
      sdk.sendData();
    } else {
      Toast.makeText(getApplicationContext(), "No string inputted",
          Toast.LENGTH_SHORT).show();
    }
  }

  public void onSendFloatEventClick(View view) {
    Float value = Float.parseFloat(floatField.getText().toString());
    sdk.trackData(EventType.Float, "my-floats", value);
    sdk.sendData();
  }
}
