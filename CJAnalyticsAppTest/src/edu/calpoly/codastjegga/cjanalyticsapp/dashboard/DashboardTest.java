package edu.calpoly.codastjegga.cjanalyticsapp.dashboard;

import junit.framework.TestCase;

/**
 * Test class for Dashboard
 * @author Gagandeep S. Kohli
 *
 */
public class DashboardTest extends TestCase {
  private Dashboard db;
  private static final String DBNAME = "DBNAME";
  
  
  protected void setUp() throws Exception {
    super.setUp();
    db = new Dashboard(DBNAME);
  }
  
  public void testGetter() {
    assertEquals(DBNAME, db.getDashboardName());
  }

}
