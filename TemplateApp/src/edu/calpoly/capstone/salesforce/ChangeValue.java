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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.salesforce.androidsdk.app.ForceApp;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

public class ChangeValue extends Activity {

	EditText et1;
	TextView et2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changevalues);
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
		Toast t = Toast.makeText(this, "Male " + et1.getText() + " Female "
				+ et2.getText(), Toast.LENGTH_SHORT);
		t.show();
		sendRequest();
	}

	private void sendRequest() {

		String soql = "SELECT Owner FROM Gender_c";

		 String objectType = "Gender__c";
		 String externalIdField = "key__c";
		 String externalId = "123";
		 Map<String, Object> fields = new HashMap<String, Object>();
		 fields.put("male__c",et1.getText());
		 fields.put("female__c",et2.getText());

		RestRequest request;
		try {
//			request = RestRequest.getRequestForQuery(
//					getString(R.string.api_version), soql);
			 request = RestRequest.getRequestForUpsert(getString(R.string.api_version),
			 objectType,
			 externalIdField, externalId, fields);
			 

			 RestHelper.getRestClient().sendAsync(request, new AsyncRequestCallback() {
				@Override
				public void onSuccess(RestRequest request, RestResponse result) {
					try {
						result.toString();

					} catch (Exception e) {
						onError(e);
					}
				}

				@Override
				public void onError(Exception exception) {
					Log.e("Problem", exception.toString());
					Toast.makeText(
							ChangeValue.this,
							getString(ForceApp.APP.getSalesforceR()
									.stringGenericError(), exception.toString()),
							Toast.LENGTH_LONG).show();
				}
			});
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
