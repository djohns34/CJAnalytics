package edu.calpoly.codastjegga.cjanalyticsapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.EnumSet;

import junit.framework.TestCase;

import org.apache.http.protocol.HTTP;
import org.mockito.Mockito;

import com.salesforce.androidsdk.auth.HttpAccess;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;

import edu.calpoly.codastjegga.cjanalyticsapp.event.EventFields;


public class DataFetcherTest extends TestCase {

	RestRequest request;
	AsyncRequestCallback callback;
	RestClient client;
	final String trackObj = "TrackedEvent__c";
	final EventFields field = EventFields.CurrencyV;
	final String apiV = "TestApi";

	//set up the mocks
	protected void setUp() throws Exception {
		super.setUp();
		request = Mockito.mock(RestRequest.class);
		callback = Mockito.mock(AsyncRequestCallback.class);
		client = new RestClientMock (null, null, null,null);

	}
	
	public void testQueryBuilder() {
		try {
			DataFetcher.queryBuilder(null, null);
			fail("DataFetcher queryBuilder failed to throw illegal Argument excpetion since both param are null");
		}catch (IllegalArgumentException exp) {
			//passed
		}
		
		try {
			DataFetcher.queryBuilder(null, EnumSet.noneOf(EventFields.class));
			fail("DataFetcher queryBuilder failed to throw illegal Argument excpetion since api ver param is null");
		}catch (IllegalArgumentException exp) {
			//passed
		}
		
		try {
			DataFetcher.queryBuilder("1", null);
			fail("DataFetcher queryBuilder failed to throw illegal Argument excpetion since field param is null");
		}catch (IllegalArgumentException exp) {
			//passed
		}
		
	}
	public void testIllegalDFConstructor () {

		try {
			//create a data fetcher
			DataFetcher dataFetcher = new DataFetcher(null, apiV);
			fail("DataFetcher didn't throw illegalArgumentException since client is null");
		}
		catch (IllegalArgumentException exp) {
			//passed
		}

		try {
			//create a data fetcher
			DataFetcher dataFetcher = new DataFetcher(client, null);
			fail("DataFetcher didn't throw illegalArgumentException since api version is null.");
		}
		catch (IllegalArgumentException exp) {
			//passed
		}

		try {
			//create a data fetcher
			DataFetcher dataFetcher = new DataFetcher(client, "");
			fail("DataFetcher didn't throw illegalArgumentException since api version is empty");
		}
		catch (IllegalArgumentException exp) {
			//passed
		}

	}


	//test on retrieve
	public void testOnRetrieve() {
		final String apiV = "TestApi";
		//create a data fetcher
		DataFetcher dataFetcher = new DataFetcher(client, apiV);
		String soql = null;
		//test onRetrieve
		try {
			soql = DataFetcher.queryBuilder(trackObj, EnumSet.of(EventFields.CurrencyV));
			dataFetcher.onRetrieve(soql, callback);
		} catch (Exception ex) {
			fail ("DataFetcher should not throw an exception");
		}

		//check that callback isn't null
		assertNotNull(((RestClientMock)client).callback);
		//check if callback mock was send --check by reference
		assertEquals(callback, ((RestClientMock)client).callback);
		//obtain encoding of soql query

		String path = ((RestClientMock)client).restRequest.getPath();
		//check if Datafetcher request build the right path
		try {
			assertEquals(RestRequest.getRequestForQuery(apiV, soql).getPath(), path);
		} catch (UnsupportedEncodingException e) {
			fail("Internal error: RestRequest unable to create a query");
		}
	}

	//inner class for mocking RestClient
	private class RestClientMock extends RestClient {

		private RestRequest restRequest;
		private AsyncRequestCallback callback;
		public RestClientMock(ClientInfo clientInfo, String authToken,
				HttpAccess httpAccessor, AuthTokenProvider authTokenProvider) {
			super(clientInfo, authToken, httpAccessor, authTokenProvider);
			// TODO Auto-generated constructor stub
		}

		public void sendAsync(RestRequest restRequest, RestClient.AsyncRequestCallback callback) 
		{
			this.restRequest = restRequest;
			this.callback = callback;
		}
	}
}
