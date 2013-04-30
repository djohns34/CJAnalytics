package edu.calpoly.codastjegga.bulk;

import static edu.calpoly.codastjegga.sdk.SalesforceDBAdapter.KEY_ROWID;
import static edu.calpoly.codastjegga.sdk.SalesforceDBAdapter.deviceId;
import static edu.calpoly.codastjegga.sdk.SalesforceDBAdapter.eventName;
import static edu.calpoly.codastjegga.sdk.SalesforceDBAdapter.timeStamp;
import static edu.calpoly.codastjegga.sdk.SalesforceDBAdapter.value;
import static edu.calpoly.codastjegga.sdk.SalesforceDBAdapter.valueRow;
import static edu.calpoly.codastjegga.sdk.SalesforceDBAdapter.valueType;
import static edu.calpoly.codastjegga.sdk.SalesforceDBAdapter.DatabaseName;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import junit.framework.TestCase;
import android.test.mock.MockCursor;
import android.util.Log;
import edu.calpoly.codastjegga.sdk.EventType;
import edu.calpoly.codastjegga.sdk.SalesforceDBAdapter;


public class CJDBCsvInputStreamTest extends TestCase {
	private static String[] columns = new String[] { KEY_ROWID,
		eventName, timeStamp, deviceId, value, valueRow, valueType, DatabaseName};

	private static String DBNAME = "MOCK DB";
	private static List<String[]> rows = new LinkedList<String[]>();
	private static List<String> rowsCsvFormatted = new LinkedList<String>();
	static {
		int rowId = 0;
		for (EventType eventType : EventType.values()) {
			String[] row = new String[columns.length];
			row[0] =  "" + rowId;
			row[1] = "name" + rowId;
			row[2] = "timestamp" + rowId;
			row[3] = "device id" + rowId;
			row[4] = "value" + rowId;
			row[5] = eventType.getField();
			row[6] = eventType.getFieldType();
			row[7] = DBNAME;
			rows.add(row);
			String csv = "\"" + row[1] + "\"," + //event name
			             "\"" + row[2] + "\"," + //time stamp
			             "\"" + row[3] + "\"," + //device id
			             "\"" + row[6] + "\"," + //event type
			             "\"" + DBNAME + "\"," + //database name/app name
			             "\"" + (rowId == 0 ? row[4] : "") + "\"," + //EventType.value()[0]
			             "\"" + (rowId == 1 ? row[4] : "") + "\"," + //EventType.value()[1]
			             "\"" + (rowId == 2 ? row[4] : "") + "\"," + //EventType.value()[2]
			             "\"" + (rowId == 3 ? row[4] : "") + "\"," + //EventType.value()[3]
			             "\"" + (rowId == 4 ? row[4] : "") + "\"";  //EventType.value()[4]
			rowsCsvFormatted.add(csv);
			rowId++;
		}
	}

	private MockCursor mockCursor;
	private DBCsvInputStream inputStream;

	public void setUp() {
		mockCursor = new CJMockCursor();
		inputStream = new CJDBCsvInputStream(mockCursor);
	}

	public void testReadingCsv() {
		Scanner scanner = new Scanner(inputStream);
		assert(scanner.hasNext());
		String header = scanner.nextLine() + "\n";
		assertEquals(CsvWriter.writeRecord(SalesforceDBAdapter.header), header);

		for (String row :rowsCsvFormatted) {
			assert(scanner.hasNext());
			String rowRead = scanner.nextLine();
			assertEquals(row, rowRead);
		}
		assertFalse(scanner.hasNext());
	}

	private class CJMockCursor extends MockCursor {
		private int rowCursor;

		public CJMockCursor() {
			rowCursor = -1;
		}

		public String getString(int columnIndex) {
			if (rowCursor > -1 && rowCursor < rows.size()) {
				return rows.get(rowCursor)[columnIndex];
			}
			return null;
		}

		public int getInt(int columnIndex) {
			if (rowCursor > -1 && rowCursor < rows.size()) {
				return Integer.parseInt(rows.get(rowCursor)[columnIndex]);
			}
			return -1;
		}

		public boolean moveToNext() {
			if (++rowCursor >= rows.size())
				return false;
			return true;
		}

		public int getColumnIndex(String columnName) {
			for (int index = 0; index < columns.length; index++) {
				if (columns[index].equals(columnName)) {
					return index;
				}
			}
			return -1;
		}

		public String getColumnName(int columnIndex) {
			if (columnIndex >= 0 && columnIndex < columns.length) 
				return columns[columnIndex];

			return null;
		}

		public String[]  getColumnNames() {
			return columns;
		}

		public void	close() {

		}


	}
}
