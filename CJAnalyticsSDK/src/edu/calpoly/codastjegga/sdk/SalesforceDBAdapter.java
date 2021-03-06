/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package edu.calpoly.codastjegga.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings.Secure;

import com.sforce.bulk.CsvWriter;

import edu.calpoly.codastjegga.bulk.CJDBCsvInputStream;
import edu.calpoly.codastjegga.bulk.DBCsvInputStream;

/**
 * Simple database access helper class. Allows persistent storage of event
 * objects Defines operations to add, list and remove events.
 * 
 * @author Daniel
 * @author Gagandeep adding method to retrieve data as Input stream
 * 
 */
public class SalesforceDBAdapter {

	public static final String KEY_ROWID = "_id";

	/* The actual event value */
	public static final String value = "value";

	/* Salesforce row names */
	public static final String eventName = "codastjegga__EventName__c";
	public static final String timeStamp = "codastjegga__Timestamp__c";
	public static final String deviceId = "codastjegga__Device_Id__c";
	public static final String valueType = "codastjegga__ValueType__c";
	public static final String DatabaseName = "codastjegga__DatabaseName__c";
	/*
	 * The row name in the salesforce table to insert the value. This is
	 * different than valueType which is a seperate row
	 */
	public static final String valueRow = "valueRow";
	
	public static final String[] header;

	static {
		header =new String[5+ EventType.values().length];
		header[0] = eventName;
		header[1] = timeStamp;
		header[2] = deviceId;
		header[3] = valueType;
		header[4] = DatabaseName;
		
		int column = 0;
		for (EventType type : EventType.values()) {
			header[5 + column] = EventType.values()[column].getField();
			column++;
		}
	}


	/* Default visibility for test cases */
	static final String DATABASE_NAME = "salesforceData";

	private static final String DATABASE_TABLE = "salesforceEvents";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + KEY_ROWID
			+ " integer primary key autoincrement, " + eventName
			+ " text not null," + timeStamp + " text not null," + deviceId
			+ " text not null," + value + " text not null," + valueRow
			+ " text not null," + valueType + " text not null,"+ DatabaseName + " text not null);";

	private static final int DATABASE_VERSION = 1;

	private final Context mCtx;

	final String androidId;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			/* Not implemented, for now */

			/*
			 * Log.w(TAG, "Upgrading database from version " + oldVersion +
			 * " to " newVersion + ", which will destroy all old data");
			 * db.execSQL("DROP TABLE IF EXISTS notes"); onCreate(db);
			 */
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public SalesforceDBAdapter(Context ctx) {
		this.mCtx = ctx;
		androidId = Secure.getString(mCtx.getContentResolver(),
				Secure.ANDROID_ID);
	}

	/**
	 * Open the database. If it cannot be opened, try to create a new instance
	 * of the database. If it cannot be created, throw an exception to signal
	 * the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public SalesforceDBAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	/**
	 * Insert the specified Event into the database. If the Event is
	 * successfully added return the new rowId, otherwise return a -1 to
	 * indicate failure.
	 * 
	 * @param Event
	 *            the event to add to the database
	 * @return rowId or -1 if failed
	 */
	public long insertEvent(Event<?> event, String database) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(eventName, event.getKey());
		initialValues.put(timeStamp, event.getTimeStamp());
		initialValues.put(deviceId, androidId);
		initialValues.put(value, event.getRESTValue());
		initialValues.put(valueRow, event.getEventType().getField());
		initialValues.put(valueType, event.getEventType().getFieldType());
		initialValues.put(DatabaseName, database);

		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Delete the Event with the given rowId
	 * 
	 * @param rowId
	 *            id of Event to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteEvent(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Call this method to retrieve a copy of all the events in the local DB
	 * 
	 * @return Return a Map containing the android row id, for delete upon
	 *         success, and a Map of the Salesforce rowNames to the value to be
	 *         inserted
	 * 
	 * 
	 */
	public Map<Integer, Map<String, Object>> fetchAllEvents() {
		Map<Integer, Map<String, Object>> result = new TreeMap<Integer, Map<String, Object>>();

		Cursor c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				eventName, timeStamp, deviceId, value, valueRow, valueType },
				null, null, null, null, null);

		while (c.moveToNext()) {
			int id = c.getInt(c.getColumnIndex(KEY_ROWID));

			// The second type should always be strings, but salesforce expects
			// <String,Object>
			Map<String, Object> event = new HashMap<String, Object>();

			event.put(eventName, c.getString(c.getColumnIndex(eventName)));
			event.put(timeStamp, c.getString(c.getColumnIndex(timeStamp)));
			event.put(deviceId, c.getString(c.getColumnIndex(deviceId)));

			/*
			 * When sending to salesforce we want to put the value in the right
			 * row in the table
			 */
			event.put(c.getString(c.getColumnIndex(valueRow)),
					c.getString(c.getColumnIndex(value)));
			event.put(valueType, c.getString(c.getColumnIndex(valueType)));

			result.put(id, event);

		}
		c.close();
		return result;
	}

	/**
	 * Getter for list of events in csv format 
	 * @return inputs stream to list of events
	 */
	public DBCsvInputStream getAllEventsAsCSVInputStream() {

		Cursor cursor = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				eventName, timeStamp, deviceId, value, valueRow, valueType,
				DatabaseName},
				null, null, null, null, null);
		
		return new CJDBCsvInputStream(cursor);
	}

	public boolean clear() {
		return mDb.delete(DATABASE_TABLE, null, null) > 0;

	}
}
