/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.calpoly.capstone.salesforce;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

import com.salesforce.androidsdk.app.ForceApp;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PieChartActivity extends SalesforceActivity {

	private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN };

	private CategorySeries mSeries = new CategorySeries("");

	private DefaultRenderer mRenderer = new DefaultRenderer();

	private String mDateFormat;



	private GraphicalView mChartView;
	
	int getLayoutID() {
		// TODO Auto-generated method stub
		return R.layout.xy_chart;
	}
	
	@Override
	int getViewID() {
		return R.id.chartView;
	}
	

	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);
		mSeries = (CategorySeries) savedState.getSerializable("current_series");
		mRenderer = (DefaultRenderer) savedState.getSerializable("current_renderer");
		mDateFormat = savedState.getString("date_format");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("current_series", mSeries);
		outState.putSerializable("current_renderer", mRenderer);
		outState.putString("date_format", mDateFormat);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		mSeries.add("Male", 50);
		mSeries.add("Female", 50);

		SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
		renderer.setColor(Color.RED);
		mRenderer.addSeriesRenderer(renderer);

		renderer = new SimpleSeriesRenderer();
		renderer.setColor(Color.BLUE);
		mRenderer.addSeriesRenderer(renderer);

		setContentView(getLayoutID());
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.BLACK);
		mRenderer.setChartTitleTextSize(20);
		mRenderer.setLabelsTextSize(15);
		mRenderer.setLegendTextSize(15);
		mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
		mRenderer.setZoomButtonsVisible(false);
		mRenderer.setPanEnabled(false);
		mRenderer.setStartAngle(90);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart);

			mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);

			
			mRenderer.setClickEnabled(true);
			mRenderer.setSelectableBuffer(10);
			mChartView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
					if (seriesSelection == null) {
						Toast
						.makeText(PieChartActivity.this, "No chart element was clicked", Toast.LENGTH_SHORT)
						.show();
					} else {
						Toast.makeText(
								PieChartActivity.this,
								"Chart element data point index " + seriesSelection.getPointIndex()
								+ " was clicked" + " point value=" + seriesSelection.getValue(),
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			mChartView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
					if (seriesSelection == null) {
						Toast.makeText(PieChartActivity.this, "No chart element was long pressed",
								Toast.LENGTH_SHORT);
						return false; // no chart element was long pressed, so let something
						// else handle the event
					} else {
						Toast.makeText(PieChartActivity.this, "Chart element data point index "
								+ seriesSelection.getPointIndex() + " was long pressed", Toast.LENGTH_SHORT);
						return true; // the element was long pressed - the event has been
						// handled
					}
				}
			});
			layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
		} else {
			mChartView.repaint();
		}


	}
	public void exportImage(View view) {
		Bitmap bit = mChartView.toBitmap();

		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		OutputStream outStream = null;
		File file = new File(extStorageDirectory, "CodastJegga.PNG");
		try {
			outStream = new FileOutputStream(file);
			bit.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
			Toast.makeText(this, "Image Saved to "+ file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
		}
		catch(Exception e){
			System.out.println("srn");
		}
	}
	public void poolData(View view) {

		 final ProgressDialog progresss=ProgressDialog.show(this, "Polling Data",
		            "Please wait....", true);
		 
		 String query="SELECT Id, male__c, female__c FROM Gender__c";
		 
		 RestRequest request;
		try {
			request = RestRequest.getRequestForQuery(getString(R.string.api_version), query);
		 
			RestHelper.getRestClient().sendAsync(request, new AsyncRequestCallback() {
				@Override
				public void onSuccess(RestRequest request, RestResponse result) {
					try {
						JSONObject records = result.asJSONObject().getJSONArray("records").getJSONObject(0);
						Double male=records.getDouble("male__c");
						Double female=records.getDouble("female__c");
						
						mSeries.clear();
						mSeries.add("Male", male);
						mSeries.add("Female", female);		
						mChartView.repaint();
						progresss.dismiss();
					} catch (Exception e) {
						onError(e);
					}
				}

				@Override
				public void onError(Exception exception) {
					Log.e("Problem", exception.toString());
					Toast.makeText(
							PieChartActivity.this,
							getString(ForceApp.APP.getSalesforceR()
									.stringGenericError(), exception.toString()),
							Toast.LENGTH_LONG).show();
				}
			});
		 
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		 
	}


}
