package edu.calpoly.codastjegga.sdk;

import com.salesforce.androidsdk.rest.RestClient;

import edu.calpoly.codastjegga.sdk.impl.ICodastSDK;

public class CodastSDK implements ICodastSDK{
	private RestClientAdapter restClientAdapter;
	private RestClient client;

	CodastSDK(RestClientAdapter restClientAdapter)
	{
		this.restClientAdapter = restClientAdapter;
		this.client = this.restClientAdapter.getRestClient();
	}
}
