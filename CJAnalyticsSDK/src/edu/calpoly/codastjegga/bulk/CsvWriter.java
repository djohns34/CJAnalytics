package edu.calpoly.codastjegga.bulk;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * CSV writer
 *
 * <p/>
 * User: mcheenath
 * Date: Nov 1, 2010
 * 
 * Gagandeep S. Kohli
 * Modified on April 27, 2013
 */
public class CsvWriter {

    public static String writeRecord(String[] values) {
        assert values != null;
        StringBuilder builder = new StringBuilder();
        builder.append(values[0]);

        for (int i=1; i<values.length; i++) {
            writeField(builder, values[i]);
        }

        endRecord(builder);
        return builder.toString();
    }

    private static void endRecord(StringBuilder builder) {
        builder.append("\n");
    }

    private static void writeField(StringBuilder builder, String value) {
        builder.append(",");
        writeFirstField(builder, value);
    }

    private static void writeFirstField(StringBuilder builder, String value) {
        if (value == null) {
            return;
        }

        builder.append("\"");
        value = value.replaceAll("\"", "\"\"");
        builder.append(value);
        builder.append("\"");
    }
}