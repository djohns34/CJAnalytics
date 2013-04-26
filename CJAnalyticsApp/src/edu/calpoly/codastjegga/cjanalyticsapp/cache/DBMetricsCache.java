package edu.calpoly.codastjegga.cjanalyticsapp.cache;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import edu.calpoly.codastjegga.cjanalyticsapp.event.EventType;

/* Database Metric caching class */
public class DBMetricsCache {
  private static final WeakHashMap<String, List<Map.Entry<String, EventType>>>
    mDBMeticsCache = new WeakHashMap<String, List<Map.Entry<String, EventType>>>();
  
  /* Getter for metrics 
   * @param dashabord whose metrics to get.
   * @return list of metrics of null, if there isn't stored list of metrics.
   */
  public static List<Map.Entry<String, EventType>> 
    getCachedMetrics (String dashboard) {
    return mDBMeticsCache.get(dashboard);
  }
  
  /* caches list of metrics 
   * @param dashboard name of dashboard.
   * @param metrics list of metrics to cache
   */
  public static void cacheMetrics (String dashboard, 
      List<Map.Entry<String, EventType>> metrics) {
    mDBMeticsCache.put(dashboard, metrics);
  }
}
