package edu.calpoly.codastjegga.bulk;

import static edu.calpoly.codastjegga.sdk.SalesforceDBAdapter.header;
import static edu.calpoly.codastjegga.sdk.SalesforceDBAdapter.value;
import static edu.calpoly.codastjegga.sdk.SalesforceDBAdapter.valueRow;

import java.io.IOException;
import java.io.InputStream;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Converts a DBCursor to local salesforce data storage to a
 * InputStream.
 * @author Gagandeep S. Kohli
 *
 */
public class DBCsvInputStream extends InputStream{

  byte[] csvHeader = CsvWriter.writeRecord(header).getBytes();

  private SQLiteDatabase mDB;
  private Cursor mCursor;
  private byte[] rowBuffer;
  private int rowBufferCursor;

  public DBCsvInputStream (Cursor dbCursor) {
    //this.mDB = dB;
    mCursor = dbCursor;
    rowBuffer = csvHeader;
    rowBufferCursor = 0;
  }


  @Override
  public int read() throws IOException {
    //IF there isn't anything to read from the buffer
    if (rowBufferCursor == -1)
      return 0; //EOF
    //IF everything is read from the buffer
    if (rowBufferCursor >= rowBuffer.length) {
      //get the next row and add it to the buffer
      appendNextRowToBuffer();
    }
    return rowBuffer[rowBufferCursor++];
  }

  /**
   * Appends the next row to the buffer so it can be read.
   * Initially implemented by Daniel, modified by Gagandeep.
   */
  private void appendNextRowToBuffer() {
    //IF there another event/row move the cusor to it
    if(mCursor.moveToNext()) {
      //the value of each field that corresponds to the header
      String[] fields = new String[header.length];
      //get the name of the valueColumn such as ..codast_jegga_TextValue__c
      String columnName = mCursor.getString(mCursor.getColumnIndex(valueRow));
      //FOR each header field 
      for (int i = 0; i < header.length; i++) {
        /* On SF there is a row for every one of the EventType.getField() strings
         * locally we have a single row to store the value, the valueRow/columnName
         * tells which remote row we want if we are preparing the data to send for
         * that row we need to get the value.
         * */
        //get the custom name of the field (header)
        String rowName = header[i];
        //IF the value field type is the same as the header field type
        if(rowName.equals(columnName)){
          //set rowName to be the local db column name of where the value is
          //stored
          rowName = value;
        }
        /*index will be -1 for all the other EventType.getField()'s in that case print an empty string*/
        int index = mCursor.getColumnIndex(rowName);
        String value = (index == -1 ) ? "" : mCursor.getString(index);
        //write the field value to the fields 
        fields[i] = value;
      }
      //write the fields to the rowBuffer
      rowBuffer = CsvWriter.writeRecord(fields).getBytes();
      //reset the buffer cursor
      rowBufferCursor = 0;
    }
    //ELSE there are no other rows to read
    else {
      //Set the cursor to EOF
      rowBufferCursor = -1;
    }
  }

  @Override
  public boolean markSupported() {
    return false;
  }
  
  @Override
  public int available() {
    return rowBuffer.length - rowBufferCursor;
  }
  
  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    // read the next byte
    byte byteRead = (byte)read();
    //If EOF
    if (byteRead == -1)
      return -1; //EOF
    //append the byte to the buffer
    b[off] = byteRead;
    //set total read to 1
    int totalRead = 1;
    //FOR each byte to read
    for(int toRead = 1; toRead < len; toRead++) {
        //read the byte
        byteRead = (byte)read();
        //If EOF
        if (byteRead == -1)
          return totalRead; 
        else
          b[toRead] = byteRead;  
    }
    //total read
    return totalRead;
  }
  
  @Override
  public int read(byte[] b) throws IOException {
    return read(b, 0, b.length);
  }
}
