package edu.calpoly.codastjegga.bulk;

import java.io.InputStream;
import java.util.List;

public abstract class DBCsvInputStream extends InputStream{

  /**
   * Getter for list of row id that were read from the stream
   * @return list of rows read from the stream
   */
  public abstract
      List<Integer> getRowsIds();

}