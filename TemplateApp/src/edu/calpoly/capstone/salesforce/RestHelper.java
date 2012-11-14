package edu.calpoly.capstone.salesforce;

import com.salesforce.androidsdk.rest.RestClient;

public final class RestHelper {
	private static RestClient client;
	
	private RestHelper(RestClient restClient)
	{
		this.client = restClient;
	}
	
	public static void setRestClient(RestClient restClient)
	{
		client = restClient;
	}
	
	public static RestClient getRestClient() { return client; };
	
}
