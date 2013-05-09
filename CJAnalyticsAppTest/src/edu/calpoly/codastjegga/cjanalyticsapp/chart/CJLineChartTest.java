package edu.calpoly.codastjegga.cjanalyticsapp.chart;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.achartengine.GraphicalView;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.TimeSeries;
import org.mockito.Mockito;

import edu.calpoly.codastjegga.cjanalyticsapp.chart.settings.ChartSettings;
import edu.calpoly.codastjegga.cjanalyticsapp.event.Event;
import edu.calpoly.codastjegga.cjanalyticsapp.event.FloatEvent;
import edu.calpoly.codastjegga.cjanalyticsapp.event.TextEvent;

import android.content.Context;
import android.test.mock.MockContext;

import junit.framework.TestCase;

public class CJLineChartTest extends TestCase {

    private CJLineChart lineChart;
    private Context context;
    private ChartSettings chartSettings;

    final String eventName = "myEvent";
    final String deviceId = "0000000";
    final String databaseName = "myDatabase";

    public void setUp() {
        lineChart = new CJLineChart();
        context = new MockContext();
        chartSettings = Mockito.mock(ChartSettings.class);
    }

    private List<Event> createEvents() {
        LinkedList<Event> events = new LinkedList<Event>();

        events.add(new FloatEvent(eventName, deviceId,
                "2013-01-29T08:00:00.000+0000", databaseName, Float
                        .valueOf("5.2")));
        events.add(new FloatEvent(eventName, deviceId,
                "2013-01-30T08:00:00.000+0000", databaseName, Float
                        .valueOf("7.9")));
        events.add(new FloatEvent(eventName, deviceId,
                "2013-01-30T08:00:00.000+0000", databaseName, Float
                        .valueOf("8.9")));

        return events;
    }
}