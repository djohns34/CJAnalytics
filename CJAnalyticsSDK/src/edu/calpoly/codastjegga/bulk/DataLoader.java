package edu.calpoly.codastjegga.bulk;


import java.util.List;

import android.util.Log;

import com.sforce.async.AsyncApiException;
import com.sforce.async.AsyncExceptionCode;
import com.sforce.async.BatchInfo;
import com.sforce.async.BulkConnection;
import com.sforce.async.ContentType;
import com.sforce.async.JobInfo;
import com.sforce.async.JobStateEnum;
import com.sforce.async.OperationEnum;
import com.sforce.ws.ConnectorConfig;

import edu.calpoly.codastjegga.auth.ClientInfo;

public class DataLoader {

	enum JobType {
		create, update;
	}
	private static final String API_VER = "27.0";
	private static final String BULK_ENDPOINT = //"https://%s.salesforce.com/" +
			"/services/async/API_VER";

	private final ClientInfo mClientInfo;
	private final String mSObjType;

	private BulkConnection mConnection;
	private JobInfo mJobInfo;
	private List<BatchInfo> mBatchInfoList;

	public DataLoader (ClientInfo clientInfo, String sObjType, OperationEnum operation) 
			throws AsyncApiException {
		assert(clientInfo != null);
		assert(clientInfo.instanceUrl != null);
		assert(clientInfo.getAccessToken() != null);

		this.mClientInfo = clientInfo;
		this.mSObjType = sObjType;
		createBulkConnection();
		createJob(operation);
	}

	private void createJob(OperationEnum operation) throws AsyncApiException {
		JobInfo job = new JobInfo();
		job.setObject(mSObjType);
		job.setOperation(operation);
		job.setContentType(ContentType.CSV);
		JobInfo info = performJob(JobType.create, job, false);
		assert(info != null);
		mJobInfo = info;
		Log.i("DataLoader", "Job created: " + mJobInfo);
	}

	public void closeJobAndUpload() throws AsyncApiException {
		if (!mJobInfo.getState().equals(JobStateEnum.Closed)) {
			JobInfo job = new JobInfo();
			job.setId(mJobInfo.getId());
			job.setState(JobStateEnum.Closed);
			mJobInfo.setState(JobStateEnum.Closed);
			JobInfo closeJob = performJob(JobType.update, job, false);
			assert(closeJob != null);
			mJobInfo = closeJob;
			Log.i("DataLoader", "Job created: " + mJobInfo);
		}
	}

	private JobInfo performJob (JobType type, JobInfo info, boolean tried) throws AsyncApiException {
		try {
			switch(type) {
			case create:
				return mConnection.createJob(info);
			case update:
				return mConnection.updateJob(info);
			default:
				return null; 

			}
		}
		catch (AsyncApiException excep) {
			if (tried) throw excep;
			else {
				if (excep.getExceptionCode().equals(AsyncExceptionCode.InvalidSessionId)) {
					assert(mClientInfo.getNewAccessToken() != null);
					createBulkConnection();
					performJob(type, info, true);
				}
			}
		}
		return null;
	}


	private void createBulkConnection ()  {
		ConnectorConfig config = new ConnectorConfig();
		config.setSessionId(mClientInfo.getAccessToken());
		config.setRestEndpoint(mClientInfo.instanceUrl + BULK_ENDPOINT);
		config.setCompression(true);
		/* only for debugging purposes */
		config.setTraceMessage(false);
		try {
			mConnection =  new BulkConnection(config);
		} catch (AsyncApiException e) {
			/* this is never happen */
			Log.e("DataLoader: either rest endpoint is null or there isn't" +
					"a session id", e.getExceptionMessage());
			mConnection = null;
		}

	}



}
