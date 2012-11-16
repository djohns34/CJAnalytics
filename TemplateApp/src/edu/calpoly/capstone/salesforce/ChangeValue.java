package edu.calpoly.capstone.salesforce;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.salesforce.androidsdk.app.ForceApp;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

public class ChangeValue extends SalesforceActivity {

	EditText et1;
	TextView et2;

	
	int getLayoutID() {
		return R.layout.changevalues;
	}
	@Override
	int getViewID() {
		return R.id.chnageVal;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutID());
		et1 = (EditText) findViewById(R.id.male);
		et2 = (TextView) findViewById(R.id.female);

		et1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				try {
					Integer m = Integer.parseInt(s.toString());
					et2.setText("" + (100 - m));

				} catch (Exception e) {

				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	public void submitValues(View view) {
		sendRequest();
	}

	private void sendRequest() {
		boolean valid=true;

		Double male=null;
		Double female=null;
		try{
			male =Double.parseDouble(et1.getText().toString());
			female = Double.parseDouble(et2.getText().toString());
			
			if(male <0 || male >100){
				valid =false;
			}
		}catch (NumberFormatException e){
			valid =false;
		}
		
		
		if(valid){

			String objectType = "Gender__c";
			String externalIdField = "key__c";
			String externalId = "123";
			Map<String, Object> fields = new HashMap<String, Object>();
			fields.put("male__c",male);
			fields.put("female__c",female);

			RestRequest request;
			try {
				request = RestRequest.getRequestForUpsert(getString(R.string.api_version),
						objectType,
						externalIdField, externalId, fields);


				RestHelper.getRestClient().sendAsync(request, new AsyncRequestCallback() {
					@Override
					public void onSuccess(RestRequest request, RestResponse result) {
						try {
							result.toString();
							toast("Values Male: "+et1.getText()+"Female: "+et2.getText());

						} catch (Exception e) {
							onError(e);
						}
					}

					@Override
					public void onError(Exception exception) {
						Log.e("Problem", exception.toString());
						toast(getString(ForceApp.APP.getSalesforceR()
								.stringGenericError(), exception.toString()));
					}
				});


			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else{
			toast("Invalid Values Male: "+et1.getText()+"Female: "+et2.getText());
		}

	}

	private void toast(String msg){					
		Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
			
		
	}


}
