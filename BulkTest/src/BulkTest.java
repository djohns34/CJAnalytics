import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.sforce.async.AsyncApiException;
import com.sforce.async.BatchInfo;
import com.sforce.async.BulkConnection;
import com.sforce.async.ContentType;
import com.sforce.async.JobInfo;
import com.sforce.async.JobStateEnum;
import com.sforce.async.OperationEnum;
import com.sforce.ws.ConnectorConfig;
 

public class BulkTest {
	static String instance = "na9";
	static String restEndpoint = "https://"+instance+".salesforce.com/services/async/27.0";
	static String sessionId = "00DE0000000dgAF!AQwAQGkL4VsV_YL9qP.6_hhrhXxd4ozx1WiObC5jP09TzjZzbsUv40BRFOERIcf0L6uP9QEd3hXZiYT.Tdk4W0NMUP0mu65N";
    static String contactCsv = "data.csv";
	public static void main(String agrs[]) {
		try {
			BulkConnection connection = getConnection();
			String sobjectType = "Contact";
			JobInfo job = createJob(sobjectType, connection);
			URL res = BulkTest.class.getResource(contactCsv);
			List<BatchInfo> batchInfoList = createBatchesFromCSVFile(connection, job,
					res);
		        closeJob(connection, job.getId());
		} catch (AsyncApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	  private static void  closeJob(BulkConnection connection, String jobId)
	          throws AsyncApiException {
	        JobInfo job = new JobInfo();
	        job.setId(jobId);
	        job.setState(JobStateEnum.Closed);
	        connection.updateJob(job);
	    }
	
	public static BulkConnection getConnection() throws AsyncApiException {
        ConnectorConfig config = new ConnectorConfig();
      config.setUsername("codast@jegga.com");
       config.setPassword("c0dastjegga");
        config.setAuthEndpoint("https://www.salesforce.com/services/Soap/u/27.0");
        config.setRestEndpoint(restEndpoint);
        config.setCompression(true);
        config.setSessionId(sessionId);
        config.setTraceMessage(false);
        return new BulkConnection(config);
       
    }
    
    /**
     * Create a new job using the Bulk API.
     * 
     * @param sobjectType
     *            The object type being loaded, such as "Account"
     * @param connection
     *            BulkConnection used to create the new job.
     * @return The JobInfo for the new job.
     * @throws AsyncApiException
     */
    private static JobInfo createJob(String sobjectType, BulkConnection connection)
          throws AsyncApiException {
        JobInfo job = new JobInfo();
        job.setObject(sobjectType);
        job.setOperation(OperationEnum.insert);
        job.setContentType(ContentType.CSV);
        job = connection.createJob(job);
        System.out.println(job);
        return job;
    }
    
    /**
     * Create and upload batches using a CSV file.
     * The file into the appropriate size batch files.
     * 
     * @param connection
     *            Connection to use for creating batches
     * @param jobInfo
     *            Job associated with new batches
     * @param csvFileName
     *            The source file for batch data
     */
    private static List<BatchInfo> createBatchesFromCSVFile(BulkConnection connection,
          JobInfo jobInfo, URL csvFileName)
            throws IOException, AsyncApiException {
        List<BatchInfo> batchInfos = new ArrayList<BatchInfo>();
        BufferedReader rdr = new BufferedReader(
            new InputStreamReader(csvFileName.openStream())
        );
        // read the CSV header row
        byte[] headerBytes = (rdr.readLine() + "\n").getBytes("UTF-8");
        int headerBytesLength = headerBytes.length;
        File tmpFile = File.createTempFile("bulkAPIInsert", ".csv");

        // Split the CSV file into multiple batches
        try {
            FileOutputStream tmpOut = new FileOutputStream(tmpFile);
            int maxBytesPerBatch = 10000000; // 10 million bytes per batch
            int maxRowsPerBatch = 10000; // 10 thousand rows per batch
            int currentBytes = 0;
            int currentLines = 0;
            String nextLine;
            while ((nextLine = rdr.readLine()) != null) {
                byte[] bytes = (nextLine + "\n").getBytes("UTF-8");
                // Create a new batch when our batch size limit is reached
                if (currentBytes + bytes.length > maxBytesPerBatch
                  || currentLines > maxRowsPerBatch) {
                    createBatch(tmpOut, tmpFile, batchInfos, connection, jobInfo);
                    currentBytes = 0;
                    currentLines = 0;
                }
                if (currentBytes == 0) {
                    tmpOut = new FileOutputStream(tmpFile);
                    tmpOut.write(headerBytes);
                    currentBytes = headerBytesLength;
                    currentLines = 1;
                }
                tmpOut.write(bytes);
                currentBytes += bytes.length;
                currentLines++;
            }
            // Finished processing all rows
            // Create a final batch for any remaining data
            if (currentLines > 1) {
                createBatch(tmpOut, tmpFile, batchInfos, connection, jobInfo);
            }
        } finally {
            tmpFile.delete();
        }
        return batchInfos;
    }

    /**
     * Create a batch by uploading the contents of the file.
     * This closes the output stream.
     * 
     * @param tmpOut
     *            The output stream used to write the CSV data for a single batch.
     * @param tmpFile
     *            The file associated with the above stream.
     * @param batchInfos
     *            The batch info for the newly created batch is added to this list.
     * @param connection
     *            The BulkConnection used to create the new batch.
     * @param jobInfo
     *            The JobInfo associated with the new batch.
     */
    private static void createBatch(FileOutputStream tmpOut, File tmpFile,
      List<BatchInfo> batchInfos, BulkConnection connection, JobInfo jobInfo)
              throws IOException, AsyncApiException {
        tmpOut.flush();
        tmpOut.close();
        FileInputStream tmpInputStream = new FileInputStream(tmpFile);
        try {
            BatchInfo batchInfo =
              connection.createBatchFromStream(jobInfo, tmpInputStream);
            System.out.println(batchInfo);
            batchInfos.add(batchInfo);

        } finally {
            tmpInputStream.close();
        }
    }


	
}
