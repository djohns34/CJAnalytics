package edu.calpoly.codastjegga.cjanalyticsapp.dashboard;

import java.io.Serializable;

/**
 * Dashboard is a database name.
 * For example, there can be a Dashboard of name 'Temple Run' or 
 * 'Angry Bird' which may contain metrics that track each of the
 * dashboard app metioned above.
 * @author Gagandeep S. Kohl
 *
 */
public class Dashboard implements Serializable {
  //Name of the dashboard
  private String dashboardName;
  
  /**
   * Constructs a Dashboard with the given name
   * @param dashboardName name of the dashboard
   */
  public Dashboard(String dashboardName) {
    this.dashboardName = dashboardName;
  }
  
  /**
   * Getter for dashboard name
   * @return name of the dashboard
   */
  public String getDashboardName () {
    return this.dashboardName;
  }
  
  /**
   * Returns the name of the dashboard
   * @return name of the dashboard
   */
  @Override 
  public String toString () {
    return getDashboardName();
  }
}
