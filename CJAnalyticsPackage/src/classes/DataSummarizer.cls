//Used only to summaraize integer, float, and currency data entries
public class DataSummarizer {
    static public String DT_FORMAT = 'yyyy-MM-dd\'T\'HH:mm:ss\'Z\'';
    
    public class ValueTypeException extends Exception {}

    public List<Long> times;
    public List<Double> averages;
    public List<Integer> entries;
    
    public DataSummarizer() {
        clearSummaryData();
    }
    
    private void clearSummaryData() {
        times = new List<Long>();
        averages = new List<Double>();
        entries = new List<Integer>();
    }
    
    //Fills in time slots and populates lists with zeroes
    private void prepLists(Long startTime, Long timeStep, Integer numEntries) {
        for (Integer i = 0; i < numEntries; i++) {
            times.add(startTime + i*timeStep);
            averages.add(0);
            entries.add(0);
        }
    }
    
    private boolean summarizePrep(DateTime startDT, DateTime endDT, Long stepSize) {
        Long range = endDT.getTime() - startDT.getTime();
        if (range < 0) return false;
        
        clearSummaryData();
        
        Integer summaryEntries = (Integer)(range/stepSize);
        if (math.mod(range, stepSize) != 0) summaryEntries++;
        
        prepLists(startDT.getTime(), stepSize, summaryEntries);
        return true;
    }
    
    private Integer getTimeSlot(Long eventTime, Long startTime, Long timeStep) {
        Integer timeSlot = (Integer)((eventTime - startTime) / timeStep);
        return timeSlot;
    }

    private Double getValueFromEvent(TrackedEvents__c event, String valueType) {
        if (valueType == 'Currency') return (Double)(event.codastjegga__CurrencyValue__c);
        else if (valueType == 'Float') return (Double)(event.codastjegga__FloatValue__c);
        else if (valueType == 'Number') return (Double)(event.codastjegga__NumberValue__c);
        else throw new ValueTypeException('valueType in getValueFromEvent() was not \'Currency\' \'Float\' or \'Number\'');
    }

    
    public void summarize(String appName,
                          String eventName,
                          String valueType,
                          DateTime startDT,
                          DateTime endDT,
                          Long stepSize) {
                          
        if (!summarizePrep(startDT, endDT, stepSize)) return;
        
        //Get all events between the start(inclusive) and end(exclusive) date that store valueType
        Integer timeSlot;
        Double newAverage;
        Double oldAverage;
        for (TrackedEvents__c event : Database.query('SELECT codastjegga__Timestamp__c, codastjegga__' + valueType + 'Value__c ' +
                                                     'FROM codastjegga__TrackedEvents__c ' +
                                                     'WHERE codastjegga__ValueType__c = :valueType ' +
                                                       'AND codastjegga__DatabaseName__c = :appName ' +
                                                       'AND codastjegga__EventName__c = :eventName ' +
                                                       'AND codastjegga__Timestamp__c >= :startDT ' +
                                                       'AND codastjegga__Timestamp__c < :endDT')) {
            timeSlot = getTimeSlot(event.Timestamp__c.getTime(), startDT.getTime(), stepSize);
            //Increment entry count
            Integer oldValue = entries.get(timeSlot);
            entries.set(timeSlot,  oldValue + 1);
            oldAverage = averages.get(timeSlot);
            newAverage = oldAverage + ((getValueFromEvent(event, valueType) - oldAverage) / entries.get(timeSlot));
            
            averages.set(timeSlot, newAverage);
        }
    }
}