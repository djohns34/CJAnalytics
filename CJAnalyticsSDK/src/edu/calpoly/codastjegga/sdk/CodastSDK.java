package edu.calpoly.codastjegga.sdk;

import com.salesforce.androidsdk.rest.RestClient;


public class CodastSDK implements ICodastSDK{
	private RestClientAdapter restClientAdapter;
	private RestClient client;

	public CodastSDK(RestClientAdapter restClientAdapter)
	{
		this.restClientAdapter = restClientAdapter;
		this.client = this.restClientAdapter.getRestClient();
	}
}
