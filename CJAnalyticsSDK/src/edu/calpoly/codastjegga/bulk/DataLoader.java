package edu.calpoly.codastjegga.bulk;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

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

/*
 * Wrapper around Salesforce Web Connector which allows easy
 * access to Salesforce REST BULK API. The class allows one
 * to upload data in form of CSV to a Saleforce account.
 * @author Gagandeep Kohli
 */
public class DataLoader {

  /*
   * Defines Job types
   */
  enum JobType {
    create, close;
  }

  /* constant for API version */
  private static final String API_VER = "27.0";
  /* BULK API endpoint */
  private static final String BULK_ENDPOINT = //"https://%s.salesforce.com/" +
      "/services/async/" + API_VER;

  /* MAX number of bytes a batch can have */    
  private static final int MAX_BYTES_PER_BATCH = 10000000; 
  /* MAX number of rows a batch can have */
  private static final int MAX_ROWS_PER_BATCH = 10000; 
  /* info about a client */
  private final ClientInfo mClientInfo;
  /* Salesforce object where data is to be uploaded to */
  private final String mSObjType;
  /* user connection to salesforce */
  private BulkConnection mConnection;
  /* information about the job */
  private JobInfo mJobInfo;
  /* list of batch info */
  private List<BatchInfo> mBatchInfoList;

  /** Constructs a Dataloader obj and initializes a job for operation
   * inserting data to sObjType.
   * @param clientInfo information about a client.
   * @param sObjType type of Saleforce object to insert data to.
   * @throws AsyncApiException if a job fails to be created.
   */
  public DataLoader (ClientInfo clientInfo, String sObjType) 
      throws AsyncApiException {
    assert(clientInfo != null);
    assert(clientInfo.getInstanceUrl() != null);
    assert(clientInfo.getAccessToken() != null);
    this.mBatchInfoList = new LinkedList<BatchInfo>();
    this.mClientInfo = clientInfo;
    this.mSObjType = sObjType;
    /* set up a connection */
    createBulkConnection();
    /* create a job */
    mJobInfo = createJob(OperationEnum.insert);
  }

  /** 
   * Getter for Job info {@link JobInfo}.
   * @return job information
   */
  public JobInfo getJobInfo () {
    return mJobInfo;
  }

  /**
   * Getter for list of batch info {@link BatchInfo}. 
   * @return list of batch info.
   */
  public List<BatchInfo> getBatchInfo () {
    return mBatchInfoList;
  }

  /**
   * Creates a job with the operation
   * @param operation {@link OperationEnum}.
   * @throws AsyncApiException when fails to be created.
   */
  private JobInfo createJob(OperationEnum operation) throws AsyncApiException {
    JobInfo job = new JobInfo();
    job.setObject(mSObjType);
    job.setOperation(operation);
    job.setContentType(ContentType.CSV);
    return performJob(JobType.create, job);

  }

  /**
   * Closes the job and instructs the Saleforce server to
   * begin processing batch(s).
   * 
   * @throws AsyncApiException job could not be close.
   */
  public void closeUploadJob() throws AsyncApiException {
    if (!mJobInfo.getState().equals(JobStateEnum.Closed)) {
      JobInfo closeJob = performJob(JobType.close, mJobInfo);
      assert(closeJob != null);
      mJobInfo = closeJob;
      Log.i("DataLoader", "Job created: " + mJobInfo);
    }
  }

  /**
   * Helper method to perform a job of job type.
   * @param type type of job type
   * @param info information about the job (id)
   * @throws AsyncApiException {@link AsyncApiException}
   */
  private JobInfo performJob (JobType type, JobInfo info) 
      throws AsyncApiException {
    return performJob (type, info, false);
  }

  /**
   * Sends data in CSV format to Saleforce server and marks as
   * pending to be processed.
   * @param stream csv data.
   * @return list of Batch info {@link BatchInfo}
   * @throws UnsupportedEncodingException if the stream is incorrectly 
   * formatted (not UTF-8).
   */
  public List<BatchInfo> sendCSV (InputStream stream) 
      throws UnsupportedEncodingException {
    List<BatchInfo> batch = batchUpAndSendCSV(stream);
    mBatchInfoList.addAll(batch);
    return batch;
  }

  /**
   * Helper method to split up the stream into batches and upload the data
   * to Salesforce.
   * @param stream csv data
   * @return list of batch info.
   * @throws UnsupportedEncodingException if the stream is incorrectly 
   * formatted (not UTF-8).
   */
  private List<BatchInfo> batchUpAndSendCSV (InputStream stream) throws 
  UnsupportedEncodingException {
    List<BatchInfo> batchInfos = new LinkedList<BatchInfo>();

    //BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

    Scanner reader = new Scanner(stream);
    /* csv header */
    byte[] csvHeader = (reader.nextLine() + "\n").getBytes("UTF-8");

    ByteArrayOutputStream batchStream = 
        new ByteArrayOutputStream(MAX_BYTES_PER_BATCH);

    int recordsInBatch = 0;

    try {
      /* write the header */
      batchStream.write(csvHeader);
    } catch (IOException e1) {
      Log.e("DataLoader", "unable to read/write csv header");
      return null; //abort
    }

    /* WHILE the stream contains data */
    while(reader.hasNext()) {
      /* get a record */
      byte[] record = (reader.nextLine() + "\n").getBytes("UTF-8");

      /* IF there is room for the record to be added to the batch */
      if (recordsInBatch + 1 <= MAX_ROWS_PER_BATCH && 
          batchStream.size() + record.length <= MAX_BYTES_PER_BATCH) {
        try {
          batchStream.write(record);
          recordsInBatch++;
        } catch (IOException e) {
          Log.e("DataLoader", "unable to read/write input stream", e);
        }
      }
      
      if (recordsInBatch == MAX_ROWS_PER_BATCH || 
          batchStream.size() == MAX_BYTES_PER_BATCH || !reader.hasNext()){ 
        BatchInfo info = null;
        try {
          info = sendBatch (new ByteArrayInputStream(batchStream.toByteArray()));

        } catch (AsyncApiException e) {
          Log.e("DataLoader", "failed to send a batch.." +
              "moving on to next batch", e);
          info = new BatchInfo();
          info.setNumberRecordsFailed(recordsInBatch);
        }
        
          batchInfos.add(info);
          recordsInBatch = 0;
          batchStream = new ByteArrayOutputStream(MAX_BYTES_PER_BATCH);
          try {
            batchStream.write(csvHeader);
          } catch (IOException e) {
            Log.e("DataLoader", "unable to read/write csv header");
            return null; //abort
          }
        }
      
      
      
    }
    return batchInfos;
  }

  /**
   * Helper method to send a batch to Salesforce.
   * @param stream csv data
   * @throws AsyncApiException @{@link AsyncApiException} 
   */
  private BatchInfo sendBatch(InputStream stream) throws AsyncApiException {
    return sendBatch(stream, false);
  }

  /**
   * Helper method to send a batch to Salesforce and if the token has
   * expired it will try again.
   * @param stream csv data
   * @param tried if connection has already be attempted to uploaded 
   * @throws AsyncApiException @{@link AsyncApiException} 
   */
  private BatchInfo sendBatch(InputStream stream, boolean tried) 
      throws AsyncApiException {
    try {
      BatchInfo info = mConnection.createBatchFromStream(mJobInfo, stream);
      return info;
    }
    catch (AsyncApiException excep) {
      if (tried) throw excep;
      else {
        /* If the token has expired */
        if (excep.getExceptionCode().
            equals(AsyncExceptionCode.InvalidSessionId)) {
          obtainNewTokenAndConnection();
          return sendBatch(stream, true);
        }
      }
    }
    catch (Exception ex) {
      Log.e("DataLoader", "unable to send a batch", ex);
    }
    return null;
  }

  /**
   * Helper method to perform a job of job type and if the token expires
   * it attempts to get a new job and try again.
   * @param type type of job type
   * @param info information about the job (id)
   * @param tried the job has already been performed
   * @throws AsyncApiException {@link AsyncApiException}
   */
  private JobInfo performJob (JobType type, JobInfo info, boolean tried) 
      throws AsyncApiException {
    try {
      switch(type) {
      case create:
        return mConnection.createJob(info);
      case close:
        return mConnection.closeJob(info.getId());
      default:
        Log.e("DataLoader", "invalid jobtype: " + type);
        return null; 

      }
    }
    catch (AsyncApiException excep) {
      if (tried) throw excep;
      else {
        /* IF the token has expired */
        if (excep.getExceptionCode().
            equals(AsyncExceptionCode.InvalidSessionId)) {
          obtainNewTokenAndConnection();
          /* try the job again */
          return performJob(type, info, true);
        }
      }
    }
    return null;
  }
  
  /**
   * Helper method to obtain a new access tokena and
   * reset the connection.
   */
  private void obtainNewTokenAndConnection () {
    /* get a new access token */
    String newToken = mClientInfo.getNewAccessToken();
//    assert(newToken != null);
    createBulkConnection(); /* recreate the connection */  
  }

  /**
   * Helper method to create a bulk connection.
   */
  private void createBulkConnection ()  {
    ConnectorConfig config = new ConnectorConfig();
    config.setSessionId(mClientInfo.getAccessToken());
    config.setRestEndpoint(mClientInfo.getInstanceUrl() + BULK_ENDPOINT);
    config.setCompression(true);
    /* only for debugging purposes */
    config.setTraceMessage(false);
    try {
      mConnection =  new BulkConnection(config);
    } catch (AsyncApiException e) {
      /* this should never happen */
      Log.e("DataLoader: either rest endpoint is null or there isn't" +
          "a session id", e.getExceptionMessage());
      mConnection = null;
    }

  }
}
